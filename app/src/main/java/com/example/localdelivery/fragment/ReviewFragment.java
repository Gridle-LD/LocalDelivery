package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.model.ReviewData;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

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

    private TextView textViewPost;
    private EditText editTextReview;

    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;
    private int clicked = 0;
    private String comment = "";
    private String shopId = "";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public ReviewFragment(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
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

        editTextReview = view.findViewById(R.id.editTextReview);
        textViewPost = view.findViewById(R.id.textViewPostReview);
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

        editTextReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = editTextReview.getText().toString().trim();
            }
        });

        textViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postReview();
            }
        });
    }

    private void postReview() {

        disposable.add(
                jsonApiHolder.postReview(new ReviewData(clicked, comment, shopId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody responseBody) {
                                Toast.makeText(mContext, "Review Posted Successfully !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }
}