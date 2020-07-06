package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.OrderAdapter;
import com.example.localdelivery.model.PlaceOrderData;
import com.example.localdelivery.model.StocksData;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
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
    private PrefUtils prefUtils;
    private ImageButton imageViewPlaceOrder;

    public OrderFragment(List<StocksData> shop, List<StocksData> cartList, String shopId) {
        this.shop = shop;
        this.cartList = cartList;
        this.shopId = shopId;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        orderAdapter = new OrderAdapter(cartList);
        recyclerView.setAdapter(orderAdapter);
    }

    private void calculateTotalPrice() {
        price = 0;
        for(StocksData stocksData : cartList) {
            price = price + (stocksData.getQuantity() * Integer.parseInt(stocksData.getPrice()));
        }
    }

    private void setClickListeners() {
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                cartList.get(position).setQuantity(++count);
                orderAdapter.notifyDataSetChanged();
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
                    orderAdapter.notifyDataSetChanged();
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
                        new StocksFragment(shop, shopId)).commit();
            }
        });

        imageViewPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
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

    private void placeOrder() {
        List<PlaceOrderData.Order.Shop.Items> itemsList = new ArrayList<>();
        for(StocksData stocksData : cartList) {
            PlaceOrderData.Order.Shop.Items items = new PlaceOrderData.Order.Shop.Items(
                    String.valueOf(stocksData.getQuantity()), stocksData.get_id());
            itemsList.add(items);
        }
        PlaceOrderData.Order.Shop shop = new PlaceOrderData.Order.Shop(shopId, itemsList, String.valueOf(price));
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
                                Toast.makeText(mContext, "Placed Order !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }
}