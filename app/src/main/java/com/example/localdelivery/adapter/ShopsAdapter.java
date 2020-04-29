package com.example.localdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localdelivery.R;
import com.example.localdelivery.local.ShopsEntity;

import java.util.ArrayList;
import java.util.List;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.NearbyShopsViewHolder>{
    private Context context;
    private List<ShopsEntity> nearbyShopsResponses;
    private OnItemClickListener mListener;

    public ShopsAdapter(Context context,
                        List<ShopsEntity> nearbyShopsResponses) {
        this.context = context;
        this.nearbyShopsResponses = new ArrayList<>(nearbyShopsResponses);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public NearbyShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_nearby_shops,
                parent, false);
        return new NearbyShopsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyShopsViewHolder holder, int position) {
        ShopsEntity nearbyShopsObject = nearbyShopsResponses.get(position);
        holder.textViewShopName.setText(nearbyShopsObject.getShopName());
        holder.textViewShopType.setText(nearbyShopsObject.getShopType());
    }

    @Override
    public int getItemCount() {
        return nearbyShopsResponses.size();
    }

    public static class NearbyShopsViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewShop;
        public TextView textViewShopName;
        public TextView textViewShopType;
        public TextView textViewShopDistance;
        public TextView textViewShopRating;

        public NearbyShopsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewShop = itemView.findViewById(R.id.imageViewShop);
            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewShopType = itemView.findViewById(R.id.textViewShopType);
            textViewShopDistance = itemView.findViewById(R.id.textViewShopDistance);
            textViewShopRating = itemView.findViewById(R.id.textViewShopRating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
