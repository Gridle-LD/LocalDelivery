package com.example.localdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.localdelivery.R;
import com.example.localdelivery.fragment.SignUpFragment;
import com.example.localdelivery.utils.PrefUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class SignUpLoginActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private int mRequestCode = 1;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String address = "";
    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_sign_up_login, new SignUpFragment())
                .commit();

        //for location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                SignUpLoginActivity.this);
        prefUtils = new PrefUtils(SignUpLoginActivity.this);

        getLocation();
    }

    private void getLocation() {
        //Checking permission
        if (ContextCompat.checkSelfPermission(SignUpLoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(SignUpLoginActivity.this, "Location Permission Granted",
                    Toast.LENGTH_SHORT).show();
            getLatLong();
        } else {
            //Asking for permission
            ActivityCompat.requestPermissions(SignUpLoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, mRequestCode);
        }
    }

    //finding latitude, longitude and address of current location
    @SuppressLint("MissingPermission")
    private void getLatLong() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(SignUpLoginActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            address = getCompleteAddressString(latitude, longitude);

                            prefUtils.setLatitude(String.valueOf(latitude));
                            prefUtils.setLongitude(String.valueOf(longitude));
                            prefUtils.setAddress(address);
                        }
                    }
                });
    }

    //converting lat long to address string
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(SignUpLoginActivity.this, Locale.getDefault());
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == mRequestCode){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(SignUpLoginActivity.this , "Permission Granted" ,
                        Toast.LENGTH_SHORT).show();
                getLatLong();
            }
            else{
                //describing importance of the permission
                Toast.makeText(SignUpLoginActivity.this , "Permission Not Granted !" ,
                        Toast.LENGTH_SHORT).show();
                buildDialog();
            }
        }
    }

    private void buildDialog() {
        new AlertDialog.Builder(SignUpLoginActivity.this)
                .setTitle("Permission needed")
                .setMessage("Access to your location is needed")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SignUpLoginActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                mRequestCode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //closing the app
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
