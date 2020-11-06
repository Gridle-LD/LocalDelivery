package com.example.localdelivery.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localdelivery.R;
import com.example.localdelivery.adapter.OrderItemAdapter;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.model.CancelOrderData;
import com.example.localdelivery.model.OrdersResponse;
import com.example.localdelivery.model.StocksData;
import com.example.localdelivery.viewModel.OrderViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    private List<OrdersResponse.Result.Orders.Order.Items> itemsList;
    private OrderItemAdapter orderItemAdapter;
    private ConstraintLayout constraintLayoutTopBar;
    private TextView textViewTopBar;

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
        constraintLayoutTopBar = findViewById(R.id.order_details_top_bar);
        textViewTopBar = findViewById(R.id.textViewOrderDetailsTopBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        orderItemAdapter = new OrderItemAdapter(cartList, ConfirmedOrderActivity.this);
        recyclerView.setAdapter(orderItemAdapter);
    }

    private void placeView() {
        pos = getIntent().getIntExtra(String.valueOf(position), 0);
        OrdersResponse.Result.Orders.Order order = allOrders.get(pos).getOrder().get(0);
        if(!order.getStatus().equals("Pending")) {
            textViewOtp.setText("OTP : " + order.getOtp());
        }
        textViewStatus.setText("Order " + order.getStatus());
        if(order.getStatus().equals("Completed")) {
            setCompletedView(convertTime(getStandardTime(order.getUpdatedAt()).substring(11, 16)),
                    getStandardTime(order.getUpdatedAt()).substring(0, 10));
        }
        if(order.getStatus().equals("Cancelled")) {
            setCancelledView();
        }
        if(order.getStatus().equals("Pending")) {
            setPendingView();
        }
        textViewShopName.setText(order.getShopId().getShopDetails().getShopName());
        if(order.isPickUp()) {
            textViewShopType.setText("PickUp");
        }
        else {
            textViewShopType.setText("Delivery");
        }

        String time = convertTime(getStandardTime(order.getCreatedAt()).substring(11, 16));
        String date = getStandardTime(order.getCreatedAt()).substring(0, 10);
        textViewDate.setText(date);
        textViewTimePlaced.setText(time);
        //TODO : set shop phone number
        textViewAddress.setText(order.getShopId().getShopDetails().getAddress());
        int price = Integer.parseInt(allOrders.get(pos).getOrder().get(0).getTotalPrice());
        textViewTotalBill.setText("Bill Total : " + price);

        progressBar.setVisibility(View.GONE);
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
        itemsList = new ArrayList<>();
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

        itemsList.addAll(allOrders.get(pos).getOrder().get(0).getItems());
    }

    private void cancelOrder() {
        CancelOrderData cancelOrderData = new CancelOrderData(allOrders.get(pos).getOrder().get(0).get_id(),
                "Cancelled", itemsList, "customer");
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        //order successfully cancelled
        if(orderViewModel.cancelOrder(cancelOrderData)) {
            onBackPressed();
        }
        else {
            Toast.makeText(this, "An Error Occurred !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCompletedView(String completedTime, String completedDate) {
        imageViewCompleted.setVisibility(View.VISIBLE);
        imageViewPending.setVisibility(View.GONE);
        imageViewCancelled.setVisibility(View.GONE);

        constraintLayoutTopBar.setBackgroundColor(Color.argb(255, 17, 168, 0));
        textViewTopBar.setText("This order has been completed.");
        textViewTimeCompleted.setText(completedTime + "  " + completedDate);

        constraintLayoutTopBar.setVisibility(View.VISIBLE);
        imageViewCancelButton.setVisibility(View.GONE);
    }

    private void setCancelledView() {
        imageViewCompleted.setVisibility(View.GONE);
        imageViewPending.setVisibility(View.GONE);
        imageViewCancelled.setVisibility(View.VISIBLE);

        constraintLayoutTopBar.setBackgroundColor(Color.argb(255, 255, 0, 0));
        textViewTopBar.setText("This order has been cancelled.");

        constraintLayoutTopBar.setVisibility(View.VISIBLE);
        imageViewCancelButton.setVisibility(View.GONE);
    }

    private void setPendingView() {
        imageViewCompleted.setVisibility(View.GONE);
        imageViewPending.setVisibility(View.VISIBLE);
        imageViewCancelled.setVisibility(View.GONE);

        constraintLayoutTopBar.setVisibility(View.GONE);
        imageViewCancelButton.setVisibility(View.VISIBLE);
    }

    private String getStandardTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String formattedDate = df.format(date);
        return formattedDate;
    }

    private String convertTime(String str) {

        String time = "";

        // Get Hours
        int h1 = (int)str.charAt(0) - '0';
        int h2 = (int)str.charAt(1)- '0';

        int hh = h1 * 10 + h2;

        // Finding out the Meridien of time
        // ie. AM or PM
        String Meridien;
        if (hh < 12) {
            Meridien = "AM";
        }
        else {
            Meridien = "PM";
        }
        hh %= 12;

        // Handle 00 and 12 case separately
        if (hh == 0) {
            time = "12";
        }
        else {
            time = String.valueOf(hh);
        }
        for (int i = 2; i < 5; ++i) {
            time = time + str.charAt(i);
        }
        time = time + " " + Meridien;
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}