package com.example.localdelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.MainPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        setSearchView();

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Nearby Shops"));
        tabLayout.addTab(tabLayout.newTab().setText("Shops which deliver"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int number = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int number = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setSearchView() {
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.montserrat_light);
        searchText.setTypeface(typeface);
        searchText.setTextSize(12);

        if(!searchView.isFocused()) {
            searchView.clearFocus();
        }
    }
}
