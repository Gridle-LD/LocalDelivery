package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.localdelivery.R;
import com.example.localdelivery.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                new HomeFragment()).commit();
    }
}
