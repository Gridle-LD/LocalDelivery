package com.example.localdelivery.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.activity.MapsActivity;
import com.example.localdelivery.adapter.OrderItemAdapter;
import com.example.localdelivery.model.OrdersResponse;
import com.example.localdelivery.model.PayTmCheckSumData;
import com.example.localdelivery.model.PayTmCheckSumResponse;
import com.example.localdelivery.model.PlaceOrderData;
import com.example.localdelivery.model.StocksData;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderItemAdapter orderItemAdapter;
    private List<StocksData> shop;
    private List<StocksData> cartList;
    private TextView textViewTotalBill;
    private TextView textViewAddAnotherItem;
    private int price;
    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private String shopId;
    private String shopName;
    private PrefUtils prefUtils;
    private ImageView imageViewPlaceOrder;
    private ImageView imageViewLike;
    private ImageView imageViewUnlike;
    private ImageView imageViewBackButton;
    private TextView textViewShopName;
    private boolean isPickup;
    private TextView textViewPickup;
    private TextView textViewDelivery;
    private View dividerPickup;
    private View dividerDelivery;
    private RadioGroup radioGroup;
    private boolean isFav;
    private int pos;
    private NearbyShopsViewModel viewModel;
    private String orderType="cash";
    private ConstraintLayout constraintLayoutAddress;
    private TextView textViewAddress;
    private TextView textViewAddressChange;
    private ProgressBar progressBar;
    private View viewBlurr;

    private Integer ActivityRequestCode = 200;

    public OrderFragment(List<StocksData> shop, List<StocksData> cartList, String shopId, String shopName,
                         boolean isPickup,boolean isFav, int pos) {
        this.shop = shop;
        this.cartList = cartList;
        this.shopId = shopId;
        this.shopName = shopName;
        this.isPickup = isPickup;
        this.isFav = isFav;
        this.pos = pos;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);

        setView(view);
        calculateTotalPrice();
        setClickListeners();

        //set total price
        textViewTotalBill.setText("Bill Total : Rs " + price);
        return view;
    }

    private void setView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_order_item_list);
        textViewTotalBill = view.findViewById(R.id.textViewBillTotal);
        textViewAddAnotherItem = view.findViewById(R.id.textViewAddAnotherItem);
        imageViewPlaceOrder = view.findViewById(R.id.imageButtonPlaceOrder);
        imageViewLike = view.findViewById(R.id.imageViewFavLikeOrder);
        imageViewUnlike = view.findViewById(R.id.imageViewFavUnlikeOrder);
        imageViewBackButton = view.findViewById(R.id.imageViewBackButtonOrder);
        textViewShopName = view.findViewById(R.id.textViewShopNameTitleOrder);
        textViewPickup = view.findViewById(R.id.textViewPickupOrderType);
        textViewDelivery = view.findViewById(R.id.textViewDeliveryOrderType);
        dividerPickup = view.findViewById(R.id.dividerPickup);
        dividerDelivery = view.findViewById(R.id.dividerDelivery);
//        radioGroup = view.findViewById(R.id.radio_group);
//        radioGroup.clearCheck();
        constraintLayoutAddress = view.findViewById(R.id.constraintLayoutAddress);
        textViewAddress = view.findViewById(R.id.textViewAddressOrder);
        textViewAddressChange = view.findViewById(R.id.textViewChange);
        progressBar = view.findViewById(R.id.progressBar3);
        viewBlurr = view.findViewById(R.id.blurr_screen_order);

        textViewAddress.setText(prefUtils.getAddress());

        textViewShopName.setText(shopName);

        if(isPickup) {
            dividerPickup.setVisibility(View.VISIBLE);
            dividerDelivery.setVisibility(View.GONE);
            constraintLayoutAddress.setVisibility(View.GONE);
        }
        else {
            dividerPickup.setVisibility(View.GONE);
            dividerDelivery.setVisibility(View.VISIBLE);
            constraintLayoutAddress.setVisibility(View.VISIBLE);
        }

        if(isFav) {
            imageViewLike.setVisibility(View.VISIBLE);
            imageViewUnlike.setVisibility(View.GONE);
        }
        else {
            imageViewLike.setVisibility(View.GONE);
            imageViewUnlike.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        orderItemAdapter = new OrderItemAdapter(cartList, mContext);
        recyclerView.setAdapter(orderItemAdapter);
    }

    private void calculateTotalPrice() {
        price = 0;
        for(StocksData stocksData : cartList) {
            price = price + (stocksData.getQuantity() * Integer.parseInt(stocksData.getPrice()));
        }
    }

    private void setClickListeners() {

        viewModel = ViewModelProviders.of(OrderFragment.this).get(NearbyShopsViewModel.class);

        imageViewUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fav(pos, 1);
                imageViewLike.setVisibility(View.VISIBLE);
                imageViewUnlike.setVisibility(View.GONE);
            }
        });

        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fav(pos, 0);
                imageViewLike.setVisibility(View.GONE);
                imageViewUnlike.setVisibility(View.VISIBLE);
            }
        });

        textViewPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickup = true;
                dividerPickup.setVisibility(View.VISIBLE);
                dividerDelivery.setVisibility(View.GONE);
                constraintLayoutAddress.setVisibility(View.GONE);
            }
        });

        textViewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickup = false;
                dividerPickup.setVisibility(View.GONE);
                dividerDelivery.setVisibility(View.VISIBLE);
                constraintLayoutAddress.setVisibility(View.VISIBLE);
            }
        });

        textViewAddressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MapsActivity.class);
                startActivity(intent);
            }
        });

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton rb = group.findViewById(checkedId);
//                if (null != rb) {
//                    orderType = String.valueOf(rb.getText());
//                }
//            }
//        });

        orderItemAdapter.setOnItemClickListener(new OrderItemAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                cartList.get(position).setQuantity(++count);
                orderItemAdapter.notifyDataSetChanged();
                price += Integer.parseInt(cartList.get(position).getPrice());
                textViewTotalBill.setText("Bill Total : Rs " + price);
                changeOriginialList(count, cartList.get(position).get_id());
            }

            @Override
            public void onRemoveClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                if(count!=0) {
                    cartList.get(position).setQuantity(--count);
                    orderItemAdapter.notifyDataSetChanged();
                    price -= Integer.parseInt(cartList.get(position).getPrice());
                    textViewTotalBill.setText("Bill Total : Rs " + price);
                    changeOriginialList(count, cartList.get(position).get_id());
                }
            }
        });

        textViewAddAnotherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                        new StocksFragment(shop, shopId, shopName, isPickup, isFav, pos)).commit();
            }
        });

        imageViewPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(orderType.equals("Cash")) {
//                    //place order route
//                    placeOrder();
//                }
//                else if(orderType.equals("Paytm")) {
//                    //testing payTm payment gateway
//                    getToken();
//                }
//                else {
//                    Toast.makeText(mContext, "Select Payment Option !", Toast.LENGTH_SHORT).show();
//                }
                if(price!=0) {
                    placeOrder();
                }
                else {
                    Toast.makeText(mContext, "Bill can't be of 0 rupees", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
    }

    private void changeOriginialList(int count, String id) {
        for(StocksData stocksData : shop) {
            if(stocksData.get_id().equals(id)) {
                stocksData.setQuantity(count);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void placeOrder() {
        progressBar.setVisibility(View.VISIBLE);
        viewBlurr.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        List<PlaceOrderData.Order.Shop.Items> itemsList = new ArrayList<>();
        for(StocksData stocksData : cartList) {
            PlaceOrderData.Order.Shop.Items items = new PlaceOrderData.Order.Shop.Items(
                    String.valueOf(stocksData.getQuantity()), stocksData.get_id(), stocksData.getName(),
                    stocksData.getPrice(), stocksData.getType());
            itemsList.add(items);
        }

        String currentTime = new SimpleDateFormat("HH:mm:ss_dd-MM-yyyy").format(new Date());
        PlaceOrderData.Order.Shop shop = new PlaceOrderData.Order.Shop(shopId, itemsList, String.valueOf(price),
                isPickup, orderType, currentTime);
        PlaceOrderData.Order order = new PlaceOrderData.Order(shop);
        List<PlaceOrderData.Order> orderList = new ArrayList<>();
        orderList.add(order);
        PlaceOrderData placeOrderData = new PlaceOrderData(orderList);

        disposable.add(
                jsonApiHolder.placeOrder(placeOrderData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody responseBody) {
//                                Toast.makeText(mContext, "Placed Order !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                viewBlurr.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                                        new RequestSentFragment()).commit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                viewBlurr.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        })
        );
    }

    @SuppressLint("SimpleDateFormat")
    private String getOrderId() {
        String date = new SimpleDateFormat("yyyyMMdd_MMmmss").format(new Date());
        Random rand = new Random();
        int min =1000, max= 9999;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return date + randomNum;
    }

    private void getToken() {
        final String orderId = getOrderId();
        PayTmCheckSumData payTmCheckSumData = new PayTmCheckSumData("MiIosW26581877503718",
                orderId, String.valueOf(price));

        disposable.add(
                jsonApiHolder.getPayTmCheckSum(payTmCheckSumData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<PayTmCheckSumResponse>() {
                            @Override
                            public void onSuccess(PayTmCheckSumResponse payTmCheckSumResponse) {
                                if(payTmCheckSumResponse!=null) {
                                    if(payTmCheckSumResponse.getResult().getVerified()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(payTmCheckSumResponse
                                                    .getPaytmResponse());
                                            JSONObject jsonObjectBody = jsonObject.getJSONObject("body");
                                            startPayTmPayment(jsonObjectBody.getString("txnToken"), orderId);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    private void startPayTmPayment(String token, String orderIdString) {
//        String host = "https://securegw-stage.paytm.in/";
//        String callBackUrl = host + "theia/paytmCallback?ORDER_ID="+orderIdString;
        String host = "https://securegw.paytm.in/";
//        String callBackUrl = host + "theia/api/v1/initiateTransaction?mid=" + "MiIosW26581877503718" +
//                "&orderId=" + orderIdString;
        String callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + orderIdString;
        PaytmOrder paytmOrder = new PaytmOrder(orderIdString, "MiIosW26581877503718", token, String.valueOf(price),
                callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.e("paytm", "Response (onTransactionResponse) : "+bundle.toString());
            }

            @Override
            public void networkNotAvailable() {
                Log.e("paytm", "network not available ");
            }

            @Override
            public void onErrorProceed(String s) {
                Log.e("paytm", " onErrorProcess "+s.toString());
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e("paytm", "Clientauth "+s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e("paytm", " UI error "+s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e("paytm", " error loading web "+s+"--"+s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.e("paytm", "backPress ");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e("paytm", " transaction cancel "+s);
            }
        });

        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(mActivity, ActivityRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("paytm", "onActivityResult: " + requestCode);
        if (requestCode == ActivityRequestCode && data != null) {
            Log.e("paytn", "onActivityResult: " + "paytm opened activity");
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e("paytm", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Log.e("paytm", " data "+  data.getStringExtra("nativeSdkForMerchantMessage"));
            Log.e("paytm", " data response - "+data.getStringExtra("response"));
/*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1394221115",
 "CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmM+VHP5pH0ekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcP3138556","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"2020061011121280011018328631290118"}
  */
            Toast.makeText(mContext, data.getStringExtra("nativeSdkForMerchantMessage")
                    + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }else{
            Log.e("paytm", " payment failed");
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        textViewAddress.setText(prefUtils.getAddress());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}