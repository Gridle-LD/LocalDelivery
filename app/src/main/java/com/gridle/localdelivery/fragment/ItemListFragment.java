package com.gridle.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gridle.localdelivery.Interface.ImageClickListener;
import com.gridle.localdelivery.R;
import com.gridle.localdelivery.adapter.ItemListAdapter;
import com.gridle.localdelivery.model.StocksData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemListFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;
    private List<StocksData> shop;
    private Set<String> stocksCategories;
    private RecyclerView recyclerView;
    private ItemListAdapter itemListAdapter;
    private List<StocksData> stocksDataList;
    private String type;
    private ImageView imageViewNoItem;

    //for image maximize
    private ImageClickListener listener;

    public ItemListFragment() {
        // Required empty public constructor
    }

    public ItemListFragment(List<StocksData> shop, String type) {
        this.shop = shop;
        this.type = type;
        stocksDataList = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            listener = (ImageClickListener) context;
        }
        catch (ClassCastException castException) {
            castException.printStackTrace();
        }
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        setView(view);
        getStocks();
        setListeners();
        return view;
    }

    private void setView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_item_list);
        imageViewNoItem = view.findViewById(R.id.imageViewNoItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        itemListAdapter = new ItemListAdapter(mContext, stocksDataList);
        recyclerView.setAdapter(itemListAdapter);
    }

    private void getStocks() {
        for(StocksData stocksData : shop) {
            if(stocksData.getType().equals(type)) {
                stocksDataList.add(stocksData);
            }
        }

        if(stocksDataList.size()==0) {
            imageViewNoItem.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNoItem.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        itemListAdapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                if(count < stocksDataList.get(position).getQuantity()) {
                    stocksDataList.get(position).setLocalQuantity(++count);
                    StocksFragment.changeQuantity(count, stocksDataList.get(position).get_id());
                    itemListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(mContext, "More item not in stock !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRemoveClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                if(count!=0) {
                    stocksDataList.get(position).setLocalQuantity(--count);
                    StocksFragment.changeQuantity(count, stocksDataList.get(position).get_id());
                    itemListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onImageClick(int position) {
                String imageUrl = stocksDataList.get(position).getImage();
                String itemName = stocksDataList.get(position).getName();
                listener.setImageClick(imageUrl, itemName);
            }
        });
    }
}
