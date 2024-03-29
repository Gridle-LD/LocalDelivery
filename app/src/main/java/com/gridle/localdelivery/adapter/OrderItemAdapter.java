package com.gridle.localdelivery.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gridle.localdelivery.R;
import com.gridle.localdelivery.model.StocksData;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderAdapterViewHolder> {

    private List<StocksData> cartList;
    private OrderItemAdapter.OnItemClickListener mListener;

    public OrderItemAdapter(List<StocksData> cartList, Context context) {
        this.cartList = cartList;
    }

    public interface OnItemClickListener {
        void onAddClick(int position, TextView textView);
        void onRemoveClick(int position, TextView textView);
    }

    public void setOnItemClickListener(OrderItemAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_order_item,
                parent, false);
        return new OrderAdapterViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterViewHolder holder, int position) {
        StocksData stocksData = cartList.get(position);
        holder.imageViewItem.setImageURI(Uri.parse(stocksData.getImage()));
        holder.textViewItemName.setText(stocksData.getName());
        holder.textViewItemType.setText("Type : " + stocksData.getType());
        holder.textViewItemPrice.setText("Price : Rs " + stocksData.getPrice());
        int price = stocksData.getLocalQuantity() * Integer.parseInt(stocksData.getPrice());
        holder.textViewItemTotalPrice.setText("Rs. " + price);
        holder.textViewItemQuantity.setText(String.valueOf(stocksData.getLocalQuantity()));
    }

    @Override
    public int getItemCount() {
        if(cartList!=null) {
            return cartList.size();
        }
        else {
            return 0;
        }
    }

    public static class OrderAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewItem;
        private TextView textViewItemName;
        private TextView textViewItemType;
        private TextView textViewItemPrice;
        private TextView textViewItemTotalPrice;
        private ImageView imageViewAdd;
        private ImageView imageViewRemove;
        private TextView textViewItemQuantity;
        private View divider;

        public OrderAdapterViewHolder(@NonNull View itemView, final OrderItemAdapter.OnItemClickListener listener) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.imageViewItemImageOrder);
            textViewItemName = itemView.findViewById(R.id.textViewItemNameOrder);
            textViewItemType = itemView.findViewById(R.id.textViewItemTypeOrder);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPriceOrder);
            textViewItemTotalPrice = itemView.findViewById(R.id.textViewTotalItemPrice);
            imageViewAdd = itemView.findViewById(R.id.imageViewAddItemOrder);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemoveItemOrder);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantityOrder);
            imageViewItem = itemView.findViewById(R.id.imageViewItemImageOrder);
            divider = itemView.findViewById(R.id.divider2Order);

            imageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onAddClick(position, textViewItemQuantity);
                        }
                    }
                }
            });

            imageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onRemoveClick(position, textViewItemQuantity);
                        }
                    }
                }
            });
        }
    }
}
