package com.example.localdelivery.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.OrderAdapter;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.viewModel.OrderViewModel;

import java.util.List;

public class MyOrdersFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private List<OrderEntity> allOrders;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        getOrders();
        return view;
    }

    private void getOrders() {
        orderViewModel = ViewModelProviders.of(MyOrdersFragment.this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(getViewLifecycleOwner(), new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                Log.e("MyOrdersFragment", "onChanged: "+ orderEntities.size() );
                allOrders = orderEntities;
                orderAdapter = new OrderAdapter(allOrders);
                recyclerView.setAdapter(orderAdapter);
            }
        });
    }
}