package com.example.localdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.localdelivery.Interface.FilterSortClickListener;
import com.example.localdelivery.R;
import com.example.localdelivery.fragment.HomeFragment;
import com.example.localdelivery.fragment.MyOrdersFragment;
import com.example.localdelivery.fragment.ProfileFragment;
import com.example.localdelivery.utils.GpsUtils;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.ScrollHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements FilterSortClickListener {

    private BottomNavigationView bottomNavigationView;
    private EditText editTextLocation;
    private CardView cardViewProfileImage;
    private TextView textViewProfileAlphabet;
    private PrefUtils prefUtils;
    private boolean isEditTextChanged = true;

    private FusedLocationProviderClient fusedLocationClient;
    private int mRequestCode = 1;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String address = "";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isGPS = false;
    public static final int GPS_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefUtils = new PrefUtils(this);

        //for location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                MainActivity.this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                new HomeFragment()).commit();

        editTextLocation = findViewById(R.id.editTextLocation);

        checkNetwork();
        getLocation();
        requestLocationUpdates();
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
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
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

    //converting lat long to address string
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        address = getCompleteAddressString(latitude, longitude);
                        prefUtils.setLatitude(String.valueOf(latitude));
                        prefUtils.setLongitude(String.valueOf(longitude));
                        prefUtils.setAddress(address);
                        if (fusedLocationClient != null) {
                            fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    mRequestCode);
            return;
        }
        editTextLocation.setText(prefUtils.getAddress());

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == mRequestCode) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
            else {
                stopApp();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                isGPS = true; // flag maintain before get location
            }
            else {
                stopApp();
            }
        }

    }

    private void stopApp() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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