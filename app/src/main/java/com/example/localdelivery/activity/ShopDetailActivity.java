package com.example.localdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.ReviewAdapter;
import com.example.localdelivery.fragment.ReviewFragment;
import com.example.localdelivery.fragment.StocksFragment;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity {

    private ImageView imageViewFavLike;
    private ImageView imageViewFavUnlike;
    private ConstraintLayout constraintLayoutCall;
    private ConstraintLayout constraintLayoutReview;
    private TextView textViewLocation;
    private PrefUtils prefUtils;
    private ConstraintLayout blurr_screen;
    private ImageView order_type_screen;
    private ImageView imageViewVisitStore;
    private ConstraintLayout constraint_layout_order_type;
    private TextView textViewPickup;
    private TextView textViewDelivery;
    private ImageView imageViewBackButton;
    private RecyclerView recyclerView;
    private int flag = 0;
    public static final int position = 0;
    private NearbyShopsViewModel viewModel;
    private ShopsEntity shop;
    private TextView textViewShopName;
    private TextView textViewShopType;
    private TextView textViewShopAddress;
    private TextView textViewRating;
    private CardView cardViewRating;
    private String phoneNumber;
    private int mRequestCode = 1;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefUtils = new PrefUtils(ShopDetailActivity.this);

        checkNetwork();
        setView();
        getShopDetails();
        setClickListeners();
    }

    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null & datac != null)
                && (wifi.isConnected() || datac.isConnected())) {
            //connection is available
        }else{
            //no connection
            setAlertBox();
        }
    }

    private void setAlertBox() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setView() {
        imageViewFavLike = findViewById(R.id.imageViewFavLike);
        imageViewFavUnlike = findViewById(R.id.imageViewFavUnlike);
        constraintLayoutCall = findViewById(R.id.constraint_layout_call);
        constraintLayoutReview = findViewById(R.id.constraint_layout_review);
        textViewLocation = findViewById(R.id.textViewLocation);
        blurr_screen = findViewById(R.id.blurr_screen);
        order_type_screen = findViewById(R.id.order_type_screen);
        imageViewVisitStore = findViewById(R.id.imageViewVisitStore);
        constraint_layout_order_type = findViewById(R.id.constraint_layout_order_type);
        textViewPickup = findViewById(R.id.textViewPickup);
        textViewDelivery = findViewById(R.id.textViewDelivery);
        textViewShopName = findViewById(R.id.textViewShopNameDetail);
        textViewShopType = findViewById(R.id.textViewShopTypeDetail);
        textViewShopAddress = findViewById(R.id.textViewShopAddressDetail);
        textViewRating = findViewById(R.id.textViewShopRatingDetail);
        cardViewRating = findViewById(R.id.card_view_rating_box_shop_detail);
        imageViewBackButton = findViewById(R.id.imageViewBackButton);
        recyclerView = findViewById(R.id.recycler_view_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);
        if(shop!=null) {
            reviewAdapter = new ReviewAdapter(shop.getReviewList());
            recyclerView.setAdapter(reviewAdapter);
        }
    }

    private void getShopDetails() {
        viewModel = ViewModelProviders.of(ShopDetailActivity.this).get(NearbyShopsViewModel.class);
        viewModel.getShopsList().observe(ShopDetailActivity.this, new Observer<List<ShopsEntity>>() {
            @Override
            public void onChanged(List<ShopsEntity> shopsEntities) {
                if(shopsEntities.size()!=0) {
                    shop = shopsEntities.get(getIntent().getIntExtra(String.valueOf(position), 0));
                    textViewShopName.setText(shop.getShopName());
                    textViewShopType.setText("Shop Type : " + shop.getShopType());
                    textViewShopAddress.setText(shop.getAddress());
                    textViewLocation.setText("Delivering to : " + prefUtils.getAddress());
                    phoneNumber = shop.getPhoneNumber();
                    if(shop!=null) {
                        reviewAdapter = new ReviewAdapter(shop.getReviewList());
                        recyclerView.setAdapter(reviewAdapter);
                        reviewAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                int rating = shop.getReviewList().get(position).getRating();
                                String comment = shop.getReviewList().get(position).getComment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                                        new ReviewFragment(rating, comment)).addToBackStack(null).commit();
                            }
                        });
                        textViewRating.setText(calculateRating(shop.getReviewList()));
                        if(Double.parseDouble(calculateRating(shop.getReviewList())) >= 4.0) {
                            cardViewRating.setCardBackgroundColor(Color.argb(255, 17, 168, 0));
                        }
                        if(Double.parseDouble(calculateRating(shop.getReviewList())) >= 3.0) {
                            cardViewRating.setCardBackgroundColor(Color.argb(255, 255, 230, 0));
                        }
                        else {
                            cardViewRating.setCardBackgroundColor(Color.argb(255, 255, 0, 0));
                        }
                    }
                }
            }
        });
    }

    private void setClickListeners() {
        imageViewFavUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewFavLike.setVisibility(View.VISIBLE);
                imageViewFavUnlike.setVisibility(View.GONE);
            }
        });

        imageViewFavLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewFavLike.setVisibility(View.GONE);
                imageViewFavUnlike.setVisibility(View.VISIBLE);
            }
        });

        constraintLayoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        constraintLayoutReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                        new ReviewFragment(shop.get_id())).addToBackStack(null).commit();
            }
        });

        imageViewVisitStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraint_layout_order_type.setVisibility(View.VISIBLE);
                textViewPickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ShopDetailActivity.this, "Pickup",
                                Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                                new StocksFragment(shop.getStock(), shop.get_id())).commit();
                    }
                });

                textViewDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ShopDetailActivity.this, "Delivery",
                                Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                                new StocksFragment(shop.getStock(), shop.get_id())).commit();
                    }
                });
            }
        });

        blurr_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                order_type_screen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = 1;
                    }
                });
                if(flag==0) {
                    constraint_layout_order_type.setVisibility(View.GONE);
                }
            }
        });

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void call() {
        String uri = "tel:" + phoneNumber.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    mRequestCode);
            return;
        }
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    private String calculateRating(List<NearbyShopsResponse.NearbyShopsObject.ReviewObject> reviewList) {
        int ratingSum = 0;
        double ratingAverage;
        for(NearbyShopsResponse.NearbyShopsObject.ReviewObject reviewObject : reviewList) {
            ratingSum += reviewObject.getRating();
        }
        ratingAverage = ((double) ratingSum)/reviewList.size();
        return String.format("%.1f", ratingAverage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == mRequestCode) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            }
            else {
                Toast.makeText(ShopDetailActivity.this, "Call Permission Not Granted !",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
