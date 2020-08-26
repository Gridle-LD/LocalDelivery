package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.activity.MapsActivity;
import com.example.localdelivery.adapter.ReviewAdapter;
import com.example.localdelivery.model.FeedbackData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.model.PlaceOrderData;
import com.example.localdelivery.model.ReviewData;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private CardView cardView;
    private CardView cardViewTopBar;
    private EditText editTextReview;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView textViewRated;
    private TextView textViewAndSays;
    private TextView textViewComment;
    private TextView textViewFeedback;
    private TextView textViewAlphabetName;
    private TextView textViewName;
    private ImageView imageViewBackButton;

    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;
    private int clicked;
    private String name = "";
    private String comment = "";
    private String shopId = "";
    private boolean post;
    private boolean feedback = false;
    private List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public ReviewFragment(String shopId, List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList) {
        this.shopId = shopId;
        this.reviewList = reviewList;
        clicked = 0;
        post = true;
//        Collections.reverse(reviewList);
    }

    public ReviewFragment(String name, int clicked, String comment) {
        this.name = name;
        this.clicked = clicked;
        this.comment = comment;
        post = false;
    }

    public ReviewFragment(boolean feedback) {
        this.feedback = feedback;
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
        if(post) {
            getReviewList();
        }
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
        cardViewTopBar = view.findViewById(R.id.card_view_top_bar_review);
        cardView = view.findViewById(R.id.cardViewEditTextReview);
        textViewPost = view.findViewById(R.id.textViewPostReview);
        progressBar = view.findViewById(R.id.progressBarReview);
        textViewRated = view.findViewById(R.id.textViewRated);
        textViewAndSays = view.findViewById(R.id.textViewAndSays);
        textViewComment = view.findViewById(R.id.textViewComment);
        textViewFeedback = view.findViewById(R.id.textViewFeedback);
        textViewAlphabetName = view.findViewById(R.id.textViewNameAlphabetReview);
        textViewName = view.findViewById(R.id.textViewNameReview);
        imageViewBackButton = view.findViewById(R.id.imageViewBackButtonReview);
        recyclerView = view.findViewById(R.id.recycler_view_reviews_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(reviewList, true);
        recyclerView.setAdapter(reviewAdapter);

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

        String firstAlphabet = String.valueOf(prefUtils.getNAME().charAt(0));
        textViewAlphabetName.setText(firstAlphabet.toUpperCase());
        textViewName.setText(prefUtils.getNAME());

        if(post) {
            editTextReview.setText(comment);
        }
        else if(feedback) {
            cardViewTopBar.setVisibility(View.GONE);
            textViewFeedback.setVisibility(View.VISIBLE);
        }
        else {
            textViewRated.setVisibility(View.VISIBLE);

            //It is only visible when their is any comment
            if(comment.length()>0) {
                textViewAndSays.setVisibility(View.VISIBLE);
            }
            textViewName.setText(name);
            textViewComment.setVisibility(View.VISIBLE);
            textViewComment.setText(comment);
            cardView.setVisibility(View.GONE);
            textViewPost.setVisibility(View.GONE);
        }
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

        if(post) {
            editTextReview.addTextChangedListener( new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(editTextReview.getText().toString().trim().length() == 0) {
                        textViewPost.setTextColor(Color.argb(255, 188, 183, 185));
                    }
                    else {
                        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
                    }
                }
            });
        }

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
                if(feedback) {
                        comment = editTextReview.getText().toString().trim();
                        if(clicked!=0 && comment.equals("")) {
                            postFeedback();
                        }
                        else {
                            Toast.makeText(mContext, "Please give the rating !", Toast.LENGTH_SHORT)
                                    .show();
                        }
                }
            }
        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void getReviewList() {
        reviewAdapter = new ReviewAdapter(reviewList, true);
        recyclerView.setAdapter(reviewAdapter);

        reviewAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String name = reviewList.get(position).getUserId().getUsername();
                int rating = reviewList.get(position).getRating();
                String comment = reviewList.get(position).getComment();
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                        new ReviewFragment(name, rating, comment)).addToBackStack(null).commit();
            }
        });
    }

    private void postReview() {
        progressBar.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.postReview(new ReviewData(clicked, comment, shopId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody responseBody) {
                                progressBar.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(mContext, "Review Posted Successfully !", Toast.LENGTH_SHORT)
                                        .show();
                                getParentFragmentManager().popBackStack();
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressBar.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void postFeedback() {
        progressBar.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.postFeedback(new FeedbackData(clicked, comment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        progressBar.setVisibility(View.GONE);
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(mContext, "Feedback Posted Successfully !", Toast.LENGTH_SHORT)
                                .show();
                        getParentFragmentManager().popBackStack();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
    }

    private void secondStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.INVISIBLE);
        imageViewStarFill4.setVisibility(View.INVISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 2;
        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
    }

    private void thirdStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.INVISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 3;
        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
    }

    private void fourthStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.VISIBLE);
        imageViewStarFill5.setVisibility(View.INVISIBLE);
        clicked = 4;
        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
    }

    private void fifthStarSelected() {
        imageViewStarFill1.setVisibility(View.VISIBLE);
        imageViewStarFill2.setVisibility(View.VISIBLE);
        imageViewStarFill3.setVisibility(View.VISIBLE);
        imageViewStarFill4.setVisibility(View.VISIBLE);
        imageViewStarFill5.setVisibility(View.VISIBLE);
        clicked = 5;
        textViewPost.setTextColor(Color.argb(255, 253, 0, 84));
    }
}