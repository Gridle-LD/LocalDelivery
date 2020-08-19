package com.example.localdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localdelivery.R;
import com.example.localdelivery.local.Entity.OrderEntity;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderEntity> allOrders;
    private OnItemClickListener mListener;

    //for checking if its the next day
    private boolean isNextDay;

    public OrderAdapter(List<OrderEntity> allOrders) {
        this.allOrders = allOrders;
        isNextDay = false;
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
        holder.textViewPrice.setText(": Rs " + orderEntity.getTotalPrice());
        String date = "", time="";
        for(int i=0; i<orderEntity.getCreatedAt().length(); i++) {
            if(orderEntity.getCreatedAt().charAt(i) == 'T') {
                time = orderEntity.getCreatedAt().substring(i+1, i+6);
                break;
            }
            date += orderEntity.getCreatedAt().charAt(i);
        }
        holder.textViewDate.setText(": " + date);
        if(orderEntity.getStatus().equals("Pending")) {
            holder.imageViewPending.setVisibility(View.VISIBLE);
            holder.imageViewCompleted.setVisibility(View.INVISIBLE);
            holder.imageViewCancelled.setVisibility(View.INVISIBLE);
        }
        if(orderEntity.getStatus().equals("Completed")) {
            holder.imageViewPending.setVisibility(View.INVISIBLE);
            holder.imageViewCompleted.setVisibility(View.VISIBLE);
            holder.imageViewCancelled.setVisibility(View.INVISIBLE);
        }
        if(orderEntity.getStatus().equals("Cancelled")) {
            holder.imageViewPending.setVisibility(View.INVISIBLE);
            holder.imageViewCompleted.setVisibility(View.INVISIBLE);
            holder.imageViewCancelled.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(allOrders!=null) {
            return allOrders.size();
        }
        else {
            return 0;
        }
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

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameAlphabet;
        private TextView textViewUsername;
        private TextView textViewPrice;
        private TextView textViewDate;
        private ImageView imageViewPending;
        private ImageView imageViewCompleted;
        private ImageView imageViewCancelled;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewNameAlphabet = itemView.findViewById(R.id.text_view_name_alphabet_order);
            textViewUsername = itemView.findViewById(R.id.text_view_name_order);
            textViewPrice = itemView.findViewById(R.id.text_view_price_order);
            textViewDate = itemView.findViewById(R.id.textViewDateOrder);
            imageViewPending = itemView.findViewById(R.id.imageViewPendingOrder);
            imageViewCompleted = itemView.findViewById(R.id.imageViewCompletedOrder);
            imageViewCancelled = itemView.findViewById(R.id.imageViewCancelledOrder);

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
