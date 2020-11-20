package com.gridle.localdelivery.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.gridle.localdelivery.R;
import com.gridle.localdelivery.model.NearbyShopsResponse;
import java.util.List;
import java.util.Random;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList;
    private OnItemClickListener mListener;
    private boolean flag;

    public ReviewAdapter(List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList, boolean flag) {
        this.reviewList = reviewList;
        this.flag = flag;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_review,
                parent, false);
        return new ReviewViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject reviewObject = reviewList.get(position);

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));
        holder.cardView.setCardBackgroundColor(color);

        Log.e("TAG" ,"onBindViewHolder: "  + reviewObject.getUserId().getUsername());
        String name = reviewObject.getUserId().getUsername();
        holder.textViewUsernameAlphabet.setText(String.valueOf(name.toUpperCase().charAt(0)));
        holder.textViewUsername.setText(name);
        holder.textViewString.setText(reviewObject.getComment());
        int rating = reviewObject.getRating();
        if(rating == 1) {
            holder.imageViewStar2.setVisibility(View.INVISIBLE);
            holder.imageViewStar3.setVisibility(View.INVISIBLE);
            holder.imageViewStar4.setVisibility(View.INVISIBLE);
            holder.imageViewStar5.setVisibility(View.INVISIBLE);
        }
        else if(rating == 2) {
            holder.imageViewStar3.setVisibility(View.INVISIBLE);
            holder.imageViewStar4.setVisibility(View.INVISIBLE);
            holder.imageViewStar5.setVisibility(View.INVISIBLE);
        }
        else if(rating == 3) {
            holder.imageViewStar4.setVisibility(View.INVISIBLE);
            holder.imageViewStar5.setVisibility(View.INVISIBLE);
        }
        else if(rating == 4) {
            holder.imageViewStar5.setVisibility(View.INVISIBLE);
        }

        if(!flag) {
            holder.divider.setVisibility(View.VISIBLE);
            holder.cardViewReview.setRadius(0);
        }
    }

    @Override
    public int getItemCount() {
        if(reviewList!=null) {
            if(!flag) {
                return Math.min(reviewList.size(), 4);
            }
            else {
                return reviewList.size();
            }
        }
        else {
            return 0;
        }
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textViewUsernameAlphabet;
        private TextView textViewUsername;
        private TextView textViewString;
        private ImageView imageViewStar1;
        private ImageView imageViewStar2;
        private ImageView imageViewStar3;
        private ImageView imageViewStar4;
        private ImageView imageViewStar5;
        private View divider;
        private CardView cardViewReview;

        public ReviewViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_profile_image_all_reviews);
            textViewUsernameAlphabet = itemView.findViewById(R.id.text_view_profile_alphabet_all_reviews);
            textViewUsername = itemView.findViewById(R.id.textViewUsernameReview);
            textViewString = itemView.findViewById(R.id.textViewStringReview);
            imageViewStar1 = itemView.findViewById(R.id.imageViewStarFillReview1);
            imageViewStar2 = itemView.findViewById(R.id.imageViewStarFillReview2);
            imageViewStar3 = itemView.findViewById(R.id.imageViewStarFillReview3);
            imageViewStar4 = itemView.findViewById(R.id.imageViewStarFillReview4);
            imageViewStar5 = itemView.findViewById(R.id.imageViewStarFillReview5);
            divider = itemView.findViewById(R.id.divider16);
            cardViewReview = itemView.findViewById(R.id.card_view_review);

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
