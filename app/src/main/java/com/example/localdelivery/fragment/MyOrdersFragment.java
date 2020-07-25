package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.ConfirmedOrderActivity;
import com.example.localdelivery.adapter.OrderAdapter;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.model.OrdersResponse;
import com.example.localdelivery.viewModel.OrderViewModel;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private List<OrderEntity> allOrders;
    private List<OrdersResponse.Result.Orders> ordersList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Context mContext;
    private Activity mActivity;

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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        setView(view);
        getOrders();
        return view;
    }

    private void setView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
    }

    private void getOrders() {
        orderViewModel = ViewModelProviders.of(MyOrdersFragment.this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(getViewLifecycleOwner(), new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                allOrders = orderEntities;
                orderAdapter = new OrderAdapter(allOrders);
                recyclerView.setAdapter(orderAdapter);
                setClickListeners();
            }
        });
    }

    private void setClickListeners() {
        if(orderViewModel.getCompleteOrderList()!=null) {
            ordersList = orderViewModel.getCompleteOrderList().getResult().getOrders();

            orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(mContext, ConfirmedOrderActivity.class);
                    intent.putExtra(String.valueOf(ConfirmedOrderActivity.position), position);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderViewModel.getDisposable().dispose();
    }
}