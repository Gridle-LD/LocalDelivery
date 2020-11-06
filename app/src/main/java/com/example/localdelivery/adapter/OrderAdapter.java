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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
        holder.textViewStatus.setText(": " + orderEntity.getStatus());
        holder.textViewPrice.setText(": Rs " + orderEntity.getTotalPrice());
        if(orderEntity.getCreatedAt()!=null) {
            String date = getStandardTime(orderEntity.getCreatedAt()).substring(0, 10);
            holder.textViewDate.setText(": " + date);
        }

        if(orderEntity.getStatus().equals("Pending") || orderEntity.getStatus().equals("Confirmed")) {
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

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameAlphabet;
        private TextView textViewUsername;
        private TextView textViewStatus;
        private TextView textViewPrice;
        private TextView textViewDate;
        private ImageView imageViewPending;
        private ImageView imageViewCompleted;
        private ImageView imageViewCancelled;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewNameAlphabet = itemView.findViewById(R.id.text_view_name_alphabet_order);
            textViewUsername = itemView.findViewById(R.id.text_view_name_order);
            textViewStatus = itemView.findViewById(R.id.text_view_status_order);
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
