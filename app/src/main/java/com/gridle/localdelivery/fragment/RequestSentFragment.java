package com.gridle.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gridle.localdelivery.R;

public class RequestSentFragment extends Fragment {

    private ImageView imageViewFirstCircle;
    private ImageView imageViewSecondCircle;
    private ImageView imageViewThirdCircle;
    private ImageView imageViewOkay;
    private Context mContext;
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public RequestSentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_sent, container, false);

        setView(view);
        setHandler();
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        imageViewFirstCircle = view.findViewById(R.id.imageViewFirstCircle);
        imageViewSecondCircle = view.findViewById(R.id.imageViewSecondCircle);
        imageViewThirdCircle = view.findViewById(R.id.imageViewThirdCircle);
        imageViewOkay = view.findViewById(R.id.imageViewOkay);
    }

    private void setHandler() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;

                if (count == 1)
                {
                    imageViewFirstCircle.setVisibility(View.VISIBLE);
                        imageViewSecondCircle.setVisibility(View.INVISIBLE);
                        imageViewThirdCircle.setVisibility(View.INVISIBLE);
                }
                else if (count == 2)
                {
                    imageViewFirstCircle.setVisibility(View.VISIBLE);
                        imageViewSecondCircle.setVisibility(View.VISIBLE);
                        imageViewThirdCircle.setVisibility(View.INVISIBLE);
                }
                else if (count == 3)
                {
                    imageViewFirstCircle.setVisibility(View.VISIBLE);
                        imageViewSecondCircle.setVisibility(View.VISIBLE);
                        imageViewThirdCircle.setVisibility(View.VISIBLE);
                }

                if (count == 3)
                    count = 0;

                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private void setClickListeners() {
        imageViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }
}