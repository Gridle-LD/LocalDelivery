package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import com.example.localdelivery.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        nextScreen();
    }

    private void nextScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                    Intent intent = new Intent(SplashActivity.this , SignUpLoginActivity.class);
                    startActivity(intent);
                    finish();
            }
        },1000);
    }
}
