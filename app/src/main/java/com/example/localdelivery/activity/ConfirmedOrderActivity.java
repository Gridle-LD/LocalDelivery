package com.example.localdelivery.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.OrderItemAdapter;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.model.CancelOrderData;
import com.example.localdelivery.model.OrdersResponse;
import com.example.localdelivery.model.StocksData;
import com.example.localdelivery.viewModel.OrderViewModel;
import java.util.ArrayList;
import java.util.List;

public class ConfirmedOrderActivity extends AppCompatActivity {

    public static final int position = 0;
    private int pos;

    private TextView textViewOtp;
    private TextView textViewShopName;
    private TextView textViewShopType;
    private TextView textViewDate;
    private TextView textViewTimePlaced;
    private TextView textViewTimeCompleted;
    private TextView textViewNumber;
    private TextView textViewAddress;
    private TextView textViewStatus;
    private TextView textViewTotalBill;
    private ImageView imageViewCompleted;
    private ImageView imageViewPending;
    private ImageView imageViewCancelled;
    private ImageView imageViewBackButton;
    private ImageView imageViewCancelButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private OrderViewModel orderViewModel;
    private List<OrdersResponse.Result.Orders> allOrders;
    private List<StocksData> cartList;
    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order);

        setView();
        setClickListeners();
        getOrders();
    }

    private void setView() {
        textViewOtp = findViewById(R.id.textViewOrderOtp);
        textViewShopName = findViewById(R.id.textViewShopNameConfirmedOrder);
        textViewShopType = findViewById(R.id.textViewTypeConfirmedOrder);
        textViewDate = findViewById(R.id.textViewDateConfirmedOrder);
        textViewTimePlaced = findViewById(R.id.textViewTimePlacedOrder);
        textViewTimeCompleted = findViewById(R.id.textViewTimeCompletedOrder);
        textViewNumber = findViewById(R.id.textViewNumberConfirmedOrder);
        textViewAddress = findViewById(R.id.textViewAddressConfirmedOrder);
        textViewStatus = findViewById(R.id.textViewStatusConfirmedOrder);
        textViewTotalBill = findViewById(R.id.text_view_total_bill_confirmed_order);
        imageViewCompleted = findViewById(R.id.imageViewTickConfirmedOrder);
        imageViewPending = findViewById(R.id.imageViewPendingConfirmedOrder);
        imageViewCancelled = findViewById(R.id.imageViewCrossConfirmedOrder);
        imageViewBackButton = findViewById(R.id.imageViewBackButtonConfirmedOrder);
        imageViewCancelButton = findViewById(R.id.imageViewCancelOrderButton);
        progressBar = findViewById(R.id.progressBarConfirmedOrder);
        recyclerView = findViewById(R.id.recycler_view_order_item_confirmed_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        orderItemAdapter = new OrderItemAdapter(cartList, ConfirmedOrderActivity.this);
        recyclerView.setAdapter(orderItemAdapter);
    }

    private void placeView() {
        pos = getIntent().getIntExtra(String.valueOf(position), 0);
        OrdersResponse.Result.Orders.Order order = allOrders.get(pos).getOrder().get(0);
        textViewOtp.setText("OTP : " + order.getOtp());
        textViewStatus.setText("Order " + order.getStatus());
        if(order.getStatus().equals("Completed")) {
            imageViewCompleted.setVisibility(View.VISIBLE);
            imageViewPending.setVisibility(View.GONE);
            imageViewCancelled.setVisibility(View.GONE);
        }
        if(order.getStatus().equals("Cancelled")) {
            imageViewCompleted.setVisibility(View.GONE);
            imageViewPending.setVisibility(View.GONE);
            imageViewCancelled.setVisibility(View.VISIBLE);
        }
        if(order.getStatus().equals("Pending")) {
            imageViewCompleted.setVisibility(View.GONE);
            imageViewPending.setVisibility(View.VISIBLE);
            imageViewCancelled.setVisibility(View.GONE);
        }
        textViewShopName.setText(order.getShopId().getShopDetails().getShopName());
        if(order.isPickUp()) {
            textViewShopType.setText("PickUp");
        }
        else {
            textViewShopType.setText("Delivery");
        }

        String date = "", time ="";
        for(int i=0; i<order.getCreatedAt().length(); i++) {
            if(order.getCreatedAt().charAt(i) == 'T') {
                time = order.getCreatedAt().substring(i+1, i+6);
                break;
            }
            date += order.getCreatedAt().charAt(i);
        }
        textViewDate.setText(date);
        textViewTimePlaced.setText(getActualTime(time));
        //TODO : set shop phone number
        textViewAddress.setText(order.getShopId().getShopDetails().getAddress());
        int price = Integer.parseInt(allOrders.get(pos).getOrder().get(0).getTotalPrice());
        textViewTotalBill.setText("Bill Total : " + price);

        progressBar.setVisibility(View.GONE);
    }

    private String getActualTime(String time) {
        String[] parts = time.split(":");
        String hours = parts[0];
        String minutes = parts[1];
        int hrs = Integer.parseInt(hours);
        int min = Integer.parseInt(minutes);
        min = min + 30;
        if(min>60) {
            ++hrs;
            min = min - 60;
        }
        hrs = hrs + 5;
        if(hrs>=24) {
            hrs = hrs - 24;
        }

        if(hrs<10) {
            hours = "0" + hrs;
        }
        else {
            hours = String.valueOf(hrs);
        }

        if(min<10) {
            minutes = "0" + min;
        }
        else {
            minutes = String.valueOf(min);
        }

        return hours + ":" + minutes;
    }

    private void setAlertBox(String title, String message) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelOrder();
            }});
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setClickListeners() {

        imageViewCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertBox("Cancel Order", "Are you sure you want to cancel this order ?");
            }
        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getOrders() {
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(this, new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                if(orderViewModel.getCompleteOrderList()!=null) {
                    allOrders = orderViewModel.getCompleteOrderList().getResult().getOrders();
                    placeView();
                    getStocks();

                    //set stocks list
                    orderItemAdapter = new OrderItemAdapter(cartList, ConfirmedOrderActivity.this);
                    recyclerView.setAdapter(orderItemAdapter);
                }
            }
        });
    }

    private void getStocks() {
        cartList = new ArrayList<>();
        for (OrdersResponse.Result.Orders.Order.Items items : allOrders.get(pos).getOrder()
                .get(0).getItems()) {
            StocksData stocksData = new StocksData(
                    items.getItem().get_id(),
                    items.getItem().getName(),
                    items.getItem().getPrice(),
                    items.getItem().getType(),
                    items.getItem().getImage(),
                    items.getItem().isAvailable(),
                    items.getItem().getShop(),
                    items.getItem().getCreatedAt()
            );
            stocksData.setQuantity(Integer.parseInt(items.getQuantity()));

            cartList.add(stocksData);
        }
    }

    private void cancelOrder() {
        CancelOrderData cancelOrderData = new CancelOrderData(allOrders.get(pos).getOrder().get(0).get_id(),
                "Cancelled");
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        //order successfully cancelled
        if(orderViewModel.cancelOrder(cancelOrderData)) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}