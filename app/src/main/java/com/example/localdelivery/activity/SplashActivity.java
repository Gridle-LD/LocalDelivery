package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.localdelivery.R;
import com.example.localdelivery.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefUtils = new PrefUtils(SplashActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                if(prefUtils.isLoggedIn()){
                    Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this , SignUpLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },1500);
    }
}
