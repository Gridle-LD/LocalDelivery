package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localdelivery.R;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.adapter.ItemListAdapter;
import com.example.localdelivery.local.ShopsEntity;
import com.example.localdelivery.model.StocksData;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment {
    private int position;
    private Context mContext;
    private Activity mActivity;
    private List<StocksData> shop;
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;
    private List<StocksData> stocksDataList;
    private String type;
    private NearbyShopsViewModel viewModel;

    public ItemListFragment() {
        // Required empty public constructor
    }

    public ItemListFragment(int position, List<StocksData> shop, String type) {
        this.position = position;
        this.shop = shop;
        this.type = type;
        stocksDataList = new ArrayList<>();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        getStocks();
        itemListAdapter = new ItemListAdapter(mContext, stocksDataList);
        recyclerView.setAdapter(itemListAdapter);
        itemListAdapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                stocksDataList.get(position).setQuantity(++count);
                StocksFragment.changeQuantity(type, position, count, stocksDataList.get(position).getName());
                itemListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemoveClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                if(count!=0) {
                    stocksDataList.get(position).setQuantity(--count);
                    StocksFragment.changeQuantity(type, position, count, stocksDataList.get(position).getName());
                    itemListAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    private void getStocks() {
        for(StocksData stocksData : shop) {
            if(stocksData.getType().equals(type)) {
                stocksDataList.add(stocksData);
            }
        }
    }
}
