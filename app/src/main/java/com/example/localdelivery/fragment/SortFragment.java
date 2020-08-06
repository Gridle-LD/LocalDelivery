package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.localdelivery.Interface.FilterSortClickListener;
import com.example.localdelivery.R;
import com.google.android.material.button.MaterialButton;

public class SortFragment extends Fragment {

    private TextView textViewHighToLow;
    private TextView textViewPopularity;
    private TextView textViewDistance;
    private MaterialButton materialButtonApply;
    private TextView textViewClearAll;
    private ImageView imageViewTickHighToLow;
    private ImageView imageViewTickPopularity;
    private ImageView imageViewTickDistance;
    private boolean isHighToLowSelected;
    private boolean isPopularitySelected;
    private boolean isDistanceSelected;
    private Context mContext;
    private Activity mActivity;
    private FilterSortClickListener listener;

    public SortFragment() {
        isHighToLowSelected = false;
        isPopularitySelected = false;
        isDistanceSelected = false;
    }

    public SortFragment(boolean isHighToLowSelected, boolean isPopularitySelected, boolean isDistanceSelected) {
        this.isHighToLowSelected = isHighToLowSelected;
        this.isPopularitySelected = isPopularitySelected;
        this.isDistanceSelected = isDistanceSelected;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            listener = (FilterSortClickListener)mContext;
        }
        catch (ClassCastException castException) {
            castException.printStackTrace();
        }
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        textViewHighToLow = view.findViewById(R.id.text_view_high_to_low);
        textViewPopularity = view.findViewById(R.id.text_view_popularity);
        textViewDistance = view.findViewById(R.id.text_view_distance);
        materialButtonApply = view.findViewById(R.id.materialButtonApplySort);
        textViewClearAll = view.findViewById(R.id.textViewClearSort);
        imageViewTickHighToLow = view.findViewById(R.id.imageViewTickHighToLow);
        imageViewTickPopularity = view.findViewById(R.id.imageViewTickPopularity);
        imageViewTickDistance = view.findViewById(R.id.imageViewTickDistance);

        if(isHighToLowSelected) {
            imageViewTickHighToLow.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickHighToLow.setVisibility(View.INVISIBLE);
        }

        if(isPopularitySelected) {
            imageViewTickPopularity.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickPopularity.setVisibility(View.INVISIBLE);
        }

        if(isDistanceSelected) {
            imageViewTickDistance.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickDistance.setVisibility(View.INVISIBLE);
        }
    }

    private void setClickListeners() {
        textViewHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isHighToLowSelected) {
                    imageViewTickHighToLow.setVisibility(View.VISIBLE);
                    imageViewTickPopularity.setVisibility(View.INVISIBLE);
                    imageViewTickDistance.setVisibility(View.INVISIBLE);
                    isHighToLowSelected = true;
                    isPopularitySelected = false;
                    isDistanceSelected = false;
                }
                else {
                    imageViewTickHighToLow.setVisibility(View.INVISIBLE);
                    isHighToLowSelected = false;
                }
            }
        });

        textViewPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPopularitySelected) {
                    imageViewTickHighToLow.setVisibility(View.INVISIBLE);
                    imageViewTickPopularity.setVisibility(View.VISIBLE);
                    imageViewTickDistance.setVisibility(View.INVISIBLE);
                    isHighToLowSelected = false;
                    isPopularitySelected = true;
                    isDistanceSelected = false;
                }
                else {
                    imageViewTickPopularity.setVisibility(View.INVISIBLE);
                    isPopularitySelected = false;
                }
            }
        });

        textViewDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDistanceSelected) {
                    imageViewTickHighToLow.setVisibility(View.INVISIBLE);
                    imageViewTickPopularity.setVisibility(View.INVISIBLE);
                    imageViewTickDistance.setVisibility(View.VISIBLE);
                    isHighToLowSelected = false;
                    isPopularitySelected = false;
                    isDistanceSelected = true;
                }
                else {
                    imageViewTickDistance.setVisibility(View.INVISIBLE);
                    isDistanceSelected = false;
                }
            }
        });

        materialButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                listener.setSortClick(isHighToLowSelected, isPopularitySelected, isDistanceSelected);
            }
        });

        textViewClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewTickHighToLow.setVisibility(View.INVISIBLE);
                imageViewTickPopularity.setVisibility(View.INVISIBLE);
                imageViewTickDistance.setVisibility(View.INVISIBLE);
                isHighToLowSelected = false;
                isPopularitySelected = false;
                isDistanceSelected = false;
            }
        });
    }
}