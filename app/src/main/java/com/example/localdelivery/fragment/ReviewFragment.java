package com.example.localdelivery.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.localdelivery.R;

public class ReviewFragment extends Fragment {
    private ImageView imageViewStarClear1;
    private ImageView imageViewStarClear2;
    private ImageView imageViewStarClear3;
    private ImageView imageViewStarClear4;
    private ImageView imageViewStarClear5;

    private ImageView imageViewStarFill1;
    private ImageView imageViewStarFill2;
    private ImageView imageViewStarFill3;
    private ImageView imageViewStarFill4;
    private ImageView imageViewStarFill5;

    private int clicked = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        imageViewStarClear1 = view.findViewById(R.id.imageViewStarClear1);
        imageViewStarClear2 = view.findViewById(R.id.imageViewStarClear2);
        imageViewStarClear3 = view.findViewById(R.id.imageViewStarClear3);
        imageViewStarClear4 = view.findViewById(R.id.imageViewStarClear4);
        imageViewStarClear5 = view.findViewById(R.id.imageViewStarClear5);

        imageViewStarFill1 = view.findViewById(R.id.imageViewStarFill1);
        imageViewStarFill2 = view.findViewById(R.id.imageViewStarFill2);
        imageViewStarFill3 = view.findViewById(R.id.imageViewStarFill3);
        imageViewStarFill4 = view.findViewById(R.id.imageViewStarFill4);
        imageViewStarFill5 = view.findViewById(R.id.imageViewStarFill5);
    }

    private void setClickListeners() {
        imageViewStarClear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewStarFill1.setVisibility(View.VISIBLE);
                imageViewStarFill2.setVisibility(View.INVISIBLE);
                imageViewStarFill3.setVisibility(View.INVISIBLE);
                imageViewStarFill4.setVisibility(View.INVISIBLE);
                imageViewStarFill5.setVisibility(View.INVISIBLE);
                clicked = 1;
            }
        });

        imageViewStarClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewStarFill1.setVisibility(View.VISIBLE);
                imageViewStarFill2.setVisibility(View.VISIBLE);
                imageViewStarFill3.setVisibility(View.INVISIBLE);
                imageViewStarFill4.setVisibility(View.INVISIBLE);
                imageViewStarFill5.setVisibility(View.INVISIBLE);
                clicked = 2;
            }
        });

        imageViewStarClear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewStarFill1.setVisibility(View.VISIBLE);
                imageViewStarFill2.setVisibility(View.VISIBLE);
                imageViewStarFill3.setVisibility(View.VISIBLE);
                imageViewStarFill4.setVisibility(View.INVISIBLE);
                imageViewStarFill5.setVisibility(View.INVISIBLE);
                clicked = 3;
            }
        });

        imageViewStarClear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewStarFill1.setVisibility(View.VISIBLE);
                imageViewStarFill2.setVisibility(View.VISIBLE);
                imageViewStarFill3.setVisibility(View.VISIBLE);
                imageViewStarFill4.setVisibility(View.VISIBLE);
                imageViewStarFill5.setVisibility(View.INVISIBLE);
                clicked = 4;
            }
        });

        imageViewStarClear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewStarFill1.setVisibility(View.VISIBLE);
                imageViewStarFill2.setVisibility(View.VISIBLE);
                imageViewStarFill3.setVisibility(View.VISIBLE);
                imageViewStarFill4.setVisibility(View.VISIBLE);
                imageViewStarFill5.setVisibility(View.VISIBLE);
                clicked = 5;
            }
        });
    }
}