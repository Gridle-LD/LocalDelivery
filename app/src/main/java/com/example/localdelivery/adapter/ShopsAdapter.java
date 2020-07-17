package com.example.localdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localdelivery.R;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.model.NearbyShopsResponse;

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
        holder.textViewShopRating.setText(calculateRating(nearbyShopsObject.getReviewList()));

        if(Double.parseDouble(calculateRating(nearbyShopsObject.getReviewList())) >= 4.0) {
            holder.cardViewRating.setCardBackgroundColor(Color.argb(255, 17, 168, 0));
        }
        if(Double.parseDouble(calculateRating(nearbyShopsObject.getReviewList())) >= 3.0) {
            holder.cardViewRating.setCardBackgroundColor(Color.argb(255, 255, 230, 0));
        }
        else {
            holder.cardViewRating.setCardBackgroundColor(Color.argb(255, 255, 0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return nearbyShopsResponses.size();
    }

    @SuppressLint("DefaultLocale")
    private String calculateRating(List<NearbyShopsResponse.NearbyShopsObject.ReviewObject> reviewList) {
        int ratingSum = 0;
        double ratingAverage;
        for(NearbyShopsResponse.NearbyShopsObject.ReviewObject reviewObject : reviewList) {
            ratingSum += reviewObject.getRating();
        }
        ratingAverage = ((double) ratingSum)/reviewList.size();
        return String.format("%.1f", ratingAverage);
    }

    public static class NearbyShopsViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewShop;
        public TextView textViewShopName;
        public TextView textViewShopType;
        public TextView textViewShopDistance;
        public TextView textViewShopRating;
        private CardView cardViewRating;

        public NearbyShopsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewShop = itemView.findViewById(R.id.imageViewShop);
            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewShopType = itemView.findViewById(R.id.textViewShopType);
            textViewShopDistance = itemView.findViewById(R.id.textViewShopDistance);
            textViewShopRating = itemView.findViewById(R.id.textViewShopRating);
            cardViewRating = itemView.findViewById(R.id.card_view_rating_box);

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
