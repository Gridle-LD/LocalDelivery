package com.example.localdelivery.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.ShopsAdapter;
import com.example.localdelivery.fragment.FilterFragment;
import com.example.localdelivery.fragment.MapsFragment;
import com.example.localdelivery.fragment.SortFragment;
import com.example.localdelivery.local.ShopsEntity;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ShopsAdapter shopsAdapter;
    private JsonApiHolder jsonApiHolder;
    private List<ShopsEntity> nearbyShops = new ArrayList<>();
    private PrefUtils prefUtils;
    private NearbyShopsViewModel nearbyShopsViewModel;
    private EditText editTextLocation;
    private CardView cardViewProfileImage;
    private TextView textViewProfileAlphabet;
    private ConstraintLayout constraintLayoutSort;
    private ConstraintLayout constraintLayoutFilter;
    private boolean isOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(this).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(this);

        checkNetwork();
        setView();
        setSearchView();
        getNearbyShops();
        setTextListeners();
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
        searchView = findViewById(R.id.searchView);
        editTextLocation = findViewById(R.id.editTextLocation);
        cardViewProfileImage = findViewById(R.id.card_view_profile_image);
        textViewProfileAlphabet = findViewById(R.id.text_view_profile_alphabet);
        recyclerView = findViewById(R.id.recycler_view_nearby_shops);
        constraintLayoutSort = findViewById(R.id.constraint_layout_sort_button);
        constraintLayoutFilter = findViewById(R.id.constraint_layout_filter_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        shopsAdapter = new ShopsAdapter(this, nearbyShops);
        recyclerView.setAdapter(shopsAdapter);

        //set location in address box
        editTextLocation.setText(prefUtils.getAddress());

        //set bg colour of profile
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));
        cardViewProfileImage.setCardBackgroundColor(color);
    }

    private void setSearchView() {
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.montserrat_light);
        searchText.setTypeface(typeface);
        searchText.setTextSize(12);

        if(!searchView.isFocused()) {
            searchView.clearFocus();
        }
    }

    private void getNearbyShops() {

        nearbyShopsViewModel = ViewModelProviders.of(this)
                .get(NearbyShopsViewModel.class);
        nearbyShopsViewModel.getShopsList().observe(this,
                new Observer<List<ShopsEntity>>() {
                    @Override
                    public void onChanged(List<ShopsEntity> shopsEntities) {
                        shopsAdapter = new ShopsAdapter(MainActivity.this, shopsEntities);
                        recyclerView.setAdapter(shopsAdapter);

                        shopsAdapter.setOnItemClickListener(
                                new ShopsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(MainActivity.this, ShopDetailActivity.class);

                                        //knowing the position of the clicked shop
                                        intent.putExtra(String.valueOf(ShopDetailActivity.position), position);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
    }

    private void setTextListeners() {

        editTextLocation.addTextChangedListener( new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops, new MapsFragment())
                        .addToBackStack(null).commit();
            }
        });

        constraintLayoutSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                            .replace(R.id.frame_layout_sort, new SortFragment()).addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        constraintLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                            R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left)
                            .replace(R.id.frame_layout_filter, new FilterFragment()).addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nearbyShopsViewModel.getDisposable().dispose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isOpened = false;
    }
}
