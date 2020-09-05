package com.example.localdelivery.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import com.example.localdelivery.R;
import com.example.localdelivery.fragment.GpsFragment;
import com.example.localdelivery.fragment.LoginFragment;
import com.example.localdelivery.utils.PrefUtils;

public class SignUpLoginActivity extends AppCompatActivity {

    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefUtils = new PrefUtils(this);

        //if logged in
        if(prefUtils.isLoggedIn()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                    new GpsFragment()).commit();
        }

        //If not logged in
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                    new LoginFragment()).commit();
        }

        checkNetwork();
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
}
