package com.example.localdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import com.example.localdelivery.Interface.FilterSortClickListener;
import com.example.localdelivery.R;
import com.example.localdelivery.fragment.HomeFragment;
import com.example.localdelivery.fragment.MyOrdersFragment;
import com.example.localdelivery.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FilterSortClickListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                new HomeFragment()).commit();

        checkNetwork();
        setView();
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
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setClickListeners() {
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
                                new MyOrdersFragment()).commit();
                        break;
                    case R.id.nav_bottom_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                                new ProfileFragment()).commit();
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
        homeFragment.setFilterClick(isGrocerySelected, isDairySelected, isDeliveryAvailableSelected);
    }
}
