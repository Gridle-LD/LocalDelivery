package com.example.localdelivery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.localdelivery.fragment.NearbyShopsFragment;
import com.example.localdelivery.fragment.ShopsWhichDeliverFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private int NoOfTabs;

    public MainPagerAdapter(FragmentManager fm, int NoOfTabs) {
        super(fm);
        this.NoOfTabs = NoOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NearbyShopsFragment();
            case 1:
                return new ShopsWhichDeliverFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }

}
