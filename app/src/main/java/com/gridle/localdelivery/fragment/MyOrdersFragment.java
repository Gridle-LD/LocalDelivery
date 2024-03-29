package com.gridle.localdelivery.fragment;

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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.gridle.localdelivery.R;
import com.gridle.localdelivery.activity.ConfirmedOrderActivity;
import com.gridle.localdelivery.adapter.OrderAdapter;
import com.gridle.localdelivery.local.Entity.OrderEntity;
import com.gridle.localdelivery.model.OrdersResponse;
import com.gridle.localdelivery.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private List<OrderEntity> allOrders;
    private List<OrderEntity> filteredList;
    private List<OrdersResponse.Result.Orders> ordersList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Context mContext;
    private Activity mActivity;
    private ImageView imageViewNoOrders;
    private EditText editTextSearch;

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
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_order);
        imageViewNoOrders = view.findViewById(R.id.imageViewNoOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        editTextSearch = view.findViewById(R.id.searchViewOrders);
    }

    private void getOrders() {
        orderViewModel = ViewModelProviders.of(MyOrdersFragment.this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(getViewLifecycleOwner(), new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                allOrders = orderEntities;
                Collections.reverse(allOrders);
                setAdapter(allOrders);
            }
        });
    }

    private void setClickListeners() {

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();

        for(OrderEntity orderEntity : allOrders) {
            if(orderEntity.getShopName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(orderEntity);
            }
        }
        setAdapter(filteredList);
    }

    private void setAdapter(final List<OrderEntity> allOrders) {
        orderAdapter = new OrderAdapter(allOrders);
        recyclerView.setAdapter(orderAdapter);

        if(allOrders.size()==0) {
            imageViewNoOrders.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNoOrders.setVisibility(View.GONE);
        }

        if(orderViewModel.getCompleteOrderList()!=null) {
            ordersList = orderViewModel.getCompleteOrderList().getResult().getOrders();

            orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(mContext, ConfirmedOrderActivity.class);

                    //position is given according to the reversed list
                    intent.putExtra(String.valueOf(ConfirmedOrderActivity.position), (ordersList.size()
                            -getPos(position, allOrders) -1));
                    startActivity(intent);
                }
            });
        }
    }

    private int getPos(int pos, List<OrderEntity> orderEntityList) {
        if(orderEntityList.size()!=0) {
            String id = orderEntityList.get(pos).get_id();
            int position = 0;
            for(OrderEntity orderEntity : allOrders) {
                if(orderEntity.get_id().equals(id)) {
                    break;
                }
                ++position;
            }
            return position;
        }
        return pos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderViewModel.getDisposable().dispose();
    }
}