package com.gridle.localdelivery.fragment;

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

import com.gridle.localdelivery.Interface.FilterSortClickListener;
import com.gridle.localdelivery.R;
import com.google.android.material.button.MaterialButton;

public class FilterFragment extends Fragment {

    private TextView textViewGrocery;
    private TextView textViewOthers;
    private TextView textViewDeliveryAvailable;
    private MaterialButton materialButtonApply;
    private TextView textViewClearAll;
    private ImageView imageViewTickGrocery;
    private ImageView imageViewTickOthers;
    private ImageView imageViewTickDeliveryAvailable;
    private boolean isGrocerySelected;
    private boolean isOthersSelected;
    private boolean isDeliveryAvailableSelected;
    private Context mContext;
    private Activity mActivity;
    private FilterSortClickListener listener;

    public FilterFragment() {
        // Required empty public constructor
        isGrocerySelected = false;
        isOthersSelected = false;
        isDeliveryAvailableSelected = false;
    }

    public FilterFragment(boolean isGrocerySelected, boolean isOthersSelected, boolean isDeliveryAvailableSelected) {
        this.isGrocerySelected = isGrocerySelected;
        this.isOthersSelected = isOthersSelected;
        this.isDeliveryAvailableSelected = isDeliveryAvailableSelected;
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
        View view =  inflater.inflate(R.layout.fragment_filter, container, false);

        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        textViewGrocery = view.findViewById(R.id.text_view_grocery);
        textViewOthers = view.findViewById(R.id.text_view_others);
        textViewDeliveryAvailable = view.findViewById(R.id.text_view_delivery_available);
        materialButtonApply = view.findViewById(R.id.materialButtonApplyFilter);
        textViewClearAll = view.findViewById(R.id.textViewClearFilter);
        imageViewTickGrocery = view.findViewById(R.id.imageViewTickGeneral);
        imageViewTickOthers = view.findViewById(R.id.imageViewTickOthers);
        imageViewTickDeliveryAvailable = view.findViewById(R.id.imageViewTickDeliveryAvailable);

        if(isGrocerySelected) {
            imageViewTickGrocery.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickGrocery.setVisibility(View.INVISIBLE);
        }

        if(isOthersSelected) {
            imageViewTickOthers.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickOthers.setVisibility(View.INVISIBLE);
        }

        if(isDeliveryAvailableSelected) {
            imageViewTickDeliveryAvailable.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickDeliveryAvailable.setVisibility(View.INVISIBLE);
        }
    }

    private void setClickListeners() {
        textViewGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGrocerySelected) {
                    imageViewTickGrocery.setVisibility(View.VISIBLE);
                    isGrocerySelected = true;
                }
                else {
                    imageViewTickGrocery.setVisibility(View.INVISIBLE);
                    isGrocerySelected = false;
                }
            }
        });

        textViewOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOthersSelected) {
                    imageViewTickOthers.setVisibility(View.VISIBLE);
                    isOthersSelected = true;
                }
                else {
                    imageViewTickOthers.setVisibility(View.INVISIBLE);
                    isOthersSelected = false;
                }
            }
        });

        textViewDeliveryAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDeliveryAvailableSelected) {
                    imageViewTickDeliveryAvailable.setVisibility(View.VISIBLE);
                    isDeliveryAvailableSelected = true;
                }
                else {
                    imageViewTickDeliveryAvailable.setVisibility(View.INVISIBLE);
                    isDeliveryAvailableSelected = false;
                }
            }
        });

        materialButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                listener.setFilterClick(isGrocerySelected, isOthersSelected, isDeliveryAvailableSelected);
            }
        });

        textViewClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewTickGrocery.setVisibility(View.INVISIBLE);
                imageViewTickOthers.setVisibility(View.INVISIBLE);
                imageViewTickDeliveryAvailable.setVisibility(View.INVISIBLE);
                isGrocerySelected = false;
                isOthersSelected = false;
                isDeliveryAvailableSelected = false;
            }
        });
    }

}