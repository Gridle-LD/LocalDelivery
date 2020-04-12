package com.example.localdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
