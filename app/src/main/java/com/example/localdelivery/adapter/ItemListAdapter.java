package com.example.localdelivery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localdelivery.R;
import com.example.localdelivery.model.StocksData;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {

    private Context context;
    private List<StocksData> stocksDataList;
    private OnItemClickListener mListener;

    public ItemListAdapter(Context context, List<StocksData> stocksDataList) {
        this.context = context;
        this.stocksDataList = stocksDataList;
    }

    public interface OnItemClickListener {
        void onAddClick(int position, TextView textView);
        void onRemoveClick(int position, TextView textView);
    }

    public void setOnItemClickListener(ItemListAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item,
                parent, false);
        return new ItemListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        StocksData stocksData = stocksDataList.get(position);
        holder.textViewName.setText(stocksData.getName());
        holder.textViewPrice.setText("Price - Rs " + stocksData.getPrice());
        holder.textViewQuantity.setText(String.valueOf(stocksData.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if(stocksDataList!=null) {
            return stocksDataList.size();
        }
        else {
            return 0;
        }
    }

    public static class ItemListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewItem;
        private TextView textViewName;
        private TextView textViewPrice;
        private TextView textViewQuantity;
        private ImageView imageViewAddButton;
        private ImageView imageViewRemoveButton;
        private ConstraintLayout constraintLayout;

        public ItemListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.imageViewItemImage);
            textViewName = itemView.findViewById(R.id.textViewItemName);
            textViewPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            imageViewAddButton = itemView.findViewById(R.id.imageViewAddItem);
            imageViewRemoveButton = itemView.findViewById(R.id.imageViewRemoveItem);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_add_item_part);

            imageViewAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onAddClick(position, textViewQuantity);
                        }
                    }
                }
            });

            imageViewRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onRemoveClick(position, textViewQuantity);
                        }
                    }
                }
            });
        }
    }
}
