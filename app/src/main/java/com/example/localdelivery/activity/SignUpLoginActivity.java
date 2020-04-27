package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.localdelivery.R;
import com.example.localdelivery.fragment.SignUpFragment;

public class SignUpLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login ,
                new SignUpFragment())
                .commit();
    }
}
