package com.example.localdelivery.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.localdelivery.R;
import com.google.android.material.button.MaterialButton;

public class FilterFragment extends Fragment {

    private TextView textViewGeneral;
    private TextView textViewDeliveryAvailable;
    private MaterialButton materialButtonApply;
    private TextView textViewClearAll;
    private ImageView imageViewTickGeneral;
    private ImageView imageViewTickDeliveryAvailable;
    private boolean isGeneralSelected;
    private boolean isDeliveryAvailableSelected;

    public FilterFragment() {
        // Required empty public constructor
        isGeneralSelected = false;
        isDeliveryAvailableSelected = false;
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
        textViewGeneral = view.findViewById(R.id.text_view_general);
        textViewDeliveryAvailable = view.findViewById(R.id.text_view_delivery_available);
        materialButtonApply = view.findViewById(R.id.materialButtonApplyFilter);
        textViewClearAll = view.findViewById(R.id.textViewClearFilter);
        imageViewTickGeneral = view.findViewById(R.id.imageViewTickGeneral);
        imageViewTickDeliveryAvailable = view.findViewById(R.id.imageViewTickDeliveryAvailable);

        if(isGeneralSelected) {
            imageViewTickGeneral.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickGeneral.setVisibility(View.INVISIBLE);
        }

        if(isDeliveryAvailableSelected) {
            imageViewTickDeliveryAvailable.setVisibility(View.VISIBLE);
        }
        else {
            imageViewTickDeliveryAvailable.setVisibility(View.INVISIBLE);
        }
    }

    private void setClickListeners() {
        textViewGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGeneralSelected) {
                    imageViewTickGeneral.setVisibility(View.VISIBLE);
                    isGeneralSelected = true;
                }
                else {
                    imageViewTickGeneral.setVisibility(View.INVISIBLE);
                    isGeneralSelected = false;
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

        textViewClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewTickGeneral.setVisibility(View.INVISIBLE);
                imageViewTickDeliveryAvailable.setVisibility(View.INVISIBLE);
                isGeneralSelected = false;
                isDeliveryAvailableSelected = false;
            }
        });
    }
}