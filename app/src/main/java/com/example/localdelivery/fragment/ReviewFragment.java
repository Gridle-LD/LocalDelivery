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
    private int clicked;
    private String comment = "";
    private String shopId = "";
    private boolean post;

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
        clicked = 0;
        post = true;
    }

    public ReviewFragment(int clicked, String comment) {
        this.clicked = clicked;
        this.comment = comment;
        post = false;
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

        if(clicked == 1) {
            firstStarSelected();
        }
        if(clicked == 2) {
            secondStarSelected();
        }
        if(clicked == 3) {
            thirdStarSelected();
        }
        if(clicked == 4) {
            fourthStarSelected();
        }
        if(clicked == 5) {
            fifthStarSelected();
        }
        editTextReview.setText(comment);
    }

    private void setClickListeners() {
        imageViewStarClear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstStarSelected();
            }
        });

        imageViewStarClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondStarSelected();
            }
        });

        imageViewStarClear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdStarSelected();
            }
        });

        imageViewStarClear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourthStarSelected();
            }
        });

        imageViewStarClear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifthStarSelected();
            }
        });

        textViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post) {
                    comment = editTextReview.getText().toString().trim();
                    if(clicked!=0) {
                        postReview();
                    }
                    else {
                        Toast.makeText(mContext, "Please give the rating !", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
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
                                Toast.makeText(mContext, "Review Posted Successfully !", Toast.LENGTH_SHORT)
                                        .show();
                                getParentFragmentManager().popBackStack();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void firstStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.INVISIBLE);
        imageViewStarFill3.setVisibility(View.INVISIBLE);
        imageViewStarFill4.setVisibility(View.INVISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 1;
    }

    private void secondStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.INVISIBLE);
        imageViewStarFill4.setVisibility(View.INVISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 2;
    }

    private void thirdStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.INVISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 3;
    }

    private void fourthStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.VISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 4;
    }

    private void fifthStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.VISIBLE);
        imageViewStarFill5.setVisibility(View.VISIBLE);
        clicked = 5;
    }
}