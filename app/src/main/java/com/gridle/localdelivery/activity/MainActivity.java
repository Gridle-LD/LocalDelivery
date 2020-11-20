package com.gridle.localdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gridle.localdelivery.Interface.FilterSortClickListener;
import com.gridle.localdelivery.R;
import com.gridle.localdelivery.fragment.HomeFragment;
import com.gridle.localdelivery.fragment.MyOrdersFragment;
import com.gridle.localdelivery.fragment.ProfileFragment;
import com.gridle.localdelivery.utils.PrefUtils;
import com.gridle.localdelivery.utils.ScrollHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements FilterSortClickListener {

    private BottomNavigationView bottomNavigationView;
    private EditText editTextLocation;
    private CardView cardViewProfileImage;
    private TextView textViewProfileAlphabet;
    private PrefUtils prefUtils;
    private View viewBlurr;
    private ProgressBar progressBar;
    private boolean isEditTextChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefUtils = new PrefUtils(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editTextLocation = findViewById(R.id.editTextLocation);
        viewBlurr = findViewById(R.id.blurr_screen_main);
        progressBar = findViewById(R.id.progressBarMain);

        checkNetwork();
        setView();
        setClickListeners();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                new HomeFragment()).commit();
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

        cardViewProfileImage = findViewById(R.id.card_view_profile_image);
        textViewProfileAlphabet = findViewById(R.id.text_view_profile_alphabet);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView
                .getLayoutParams();
        layoutParams.setBehavior(new ScrollHandler());

        //set location in address box
        editTextLocation.setText(prefUtils.getAddress());

        //set bg colour of profile
        Random rnd = new Random();
        int color = Color.argb(255,  (rnd.nextInt(225)+25), (rnd.nextInt(225)+25),
                (rnd.nextInt(225)+25));
        cardViewProfileImage.setCardBackgroundColor(color);

        if(prefUtils.getNAME()!=null) {
            //set first alphabet of username
            String firstAlphabet = String.valueOf(prefUtils.getNAME().charAt(0));
            textViewProfileAlphabet.setText(firstAlphabet.toUpperCase());
        }
    }

    private void setClickListeners() {

        cardViewProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_bottom_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                        new ProfileFragment()).addToBackStack(null).commit();
            }
        });

        editTextLocation.addTextChangedListener( new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                    if(isEditTextChanged) {

                        int originalLength = prefUtils.getAddress().length();
                        int newLength = s.toString().length();
                        //to not open maps more than once
                        if(originalLength == (newLength-1) || (originalLength == (newLength+1))) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    }
                    isEditTextChanged = true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                                new HomeFragment()).commit();
                        break;
                    case R.id.nav_bottom_my_orders:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                                new MyOrdersFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_bottom_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                                new ProfileFragment()).addToBackStack(null).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void setFilterClick(boolean isGrocerySelected, boolean isDairySelected,
                               boolean isDeliveryAvailableSelected) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().
                findFragmentById(R.id.frame_layout_shops);
        assert homeFragment != null;
        homeFragment.setFilterClick(isGrocerySelected, isDairySelected, isDeliveryAvailableSelected);
    }

    @Override
    public void setSortClick(boolean isRatingSelected, boolean isPopularitySelected, boolean isDistanceSelected) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().
                findFragmentById(R.id.frame_layout_shops);
        assert homeFragment != null;
        homeFragment.setSortClick(isRatingSelected, isPopularitySelected, isDistanceSelected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //for not calling maps activity in edit text changed listener
        isEditTextChanged = false;

        editTextLocation.setText(prefUtils.getAddress());
    }

    @Override
    public void onBackPressed() {
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.nav_bottom_home != selectedItemId) {
            bottomNavigationView.setSelectedItemId(R.id.nav_bottom_home);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                    new HomeFragment()).commit();
        } else {
            super.onBackPressed();

            this.finishAffinity();
        }
    }
}