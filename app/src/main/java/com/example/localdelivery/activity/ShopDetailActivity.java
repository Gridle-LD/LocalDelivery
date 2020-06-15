package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localdelivery.R;
import com.example.localdelivery.fragment.StocksFragment;
import com.example.localdelivery.local.ShopsDao;
import com.example.localdelivery.local.ShopsDatabase;
import com.example.localdelivery.local.ShopsEntity;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;

import java.util.ArrayList;
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
    private int flag = 0;
    public static final int position = 0;
    private NearbyShopsViewModel viewModel;
    private ShopsEntity shop;
    private TextView textViewShopName;
    private TextView textViewShopType;
    private TextView textViewShopAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        prefUtils = new PrefUtils(ShopDetailActivity.this);

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

        getShopDetails();

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
                Toast.makeText(ShopDetailActivity.this, "On a Call !", Toast.LENGTH_LONG)
                        .show();
            }
        });

        constraintLayoutReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShopDetailActivity.this, "On a Review !", Toast.LENGTH_LONG)
                        .show();
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
                                new StocksFragment(shop)).commit();
                    }
                });

                textViewDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ShopDetailActivity.this, "Delivery",
                                Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                                new StocksFragment(shop)).commit();
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


    }

    private void getShopDetails() {
        viewModel = ViewModelProviders.of(ShopDetailActivity.this).get(NearbyShopsViewModel.class);
        viewModel.getShopsList().observe(ShopDetailActivity.this, new Observer<List<ShopsEntity>>() {
            @Override
            public void onChanged(List<ShopsEntity> shopsEntities) {
                shop = shopsEntities.get(getIntent().getIntExtra(String.valueOf(position), 0));
                textViewShopName.setText(shop.getShopName());
                textViewShopType.setText("Shop Type : " + shop.getShopType());
                textViewShopAddress.setText(shop.getAddress());
                textViewLocation.setText("Delivering to : " + prefUtils.getAddress());
            }
        });
    }
}
