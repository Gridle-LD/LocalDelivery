package com.example.localdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localdelivery.R;
import com.example.localdelivery.local.Entity.OrderEntity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderEntity> allOrders;
    private OnItemClickListener mListener;

    public OrderAdapter(List<OrderEntity> allOrders) {
        this.allOrders = allOrders;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_order,
                parent, false);
        return new OrderViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity orderEntity = allOrders.get(position);
        String name = orderEntity.getShopName().toUpperCase();
        holder.textViewNameAlphabet.setText(String.valueOf(name.charAt(0)));
        holder.textViewUsername.setText(orderEntity.getShopName());
        holder.textViewPrice.setText(orderEntity.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return allOrders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameAlphabet;
        private TextView textViewUsername;
        private TextView textViewPrice;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewNameAlphabet = itemView.findViewById(R.id.text_view_name_alphabet_order);
            textViewUsername = itemView.findViewById(R.id.text_view_name_order);
            textViewPrice = itemView.findViewById(R.id.text_view_price_order);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });
        }
    }
}
