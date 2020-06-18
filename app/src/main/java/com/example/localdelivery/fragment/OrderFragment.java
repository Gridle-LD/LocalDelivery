package com.example.localdelivery.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.localdelivery.R;
import com.example.localdelivery.adapter.ItemListAdapter;
import com.example.localdelivery.adapter.OrderAdapter;
import com.example.localdelivery.model.StocksData;

import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<StocksData> cartList;
    private TextView textViewTotalBill;
    private int price;

    public OrderFragment(List<StocksData> cartList) {
        this.cartList = cartList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_order_item_list);
        textViewTotalBill = view.findViewById(R.id.textViewBillTotal);

        price = 0;
        for(StocksData stocksData : cartList) {
            price = price + (stocksData.getQuantity() * Integer.parseInt(stocksData.getPrice()));
        }
        textViewTotalBill.setText("Bill Total : Rs " + price);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        orderAdapter = new OrderAdapter(cartList);
        recyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position, TextView textView) {
                String number = textView.getText().toString();
                int count = Integer.parseInt(number);
                cartList.get(position).setQuantity(++count);
                orderAdapter.notifyDataSetChanged();
                price += Integer.parseInt(cartList.get(position).getPrice());
                textViewTotalBill.setText("Bill Total : Rs " + price);
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
                }
            }
        });
        return view;
    }
}