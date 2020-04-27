package com.example.localdelivery.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.localdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopsWhichDeliverFragment extends Fragment {

    public ShopsWhichDeliverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shops_which_deliver, container, false);
    }
}
