package com.example.localdelivery.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.localdelivery.fragment.ItemListFragment;
import com.example.localdelivery.model.StocksData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StocksTabLayoutAdapter extends FragmentStatePagerAdapter {

    private int NoOfTabs;
    private List<StocksData> shop;
    private List<String> stocksCategories;

    public StocksTabLayoutAdapter(@NonNull FragmentManager fm, int NoOfTabs, List<StocksData> shop,
                                  List<String> stocksCategories) {
        super(fm);
        this.NoOfTabs = NoOfTabs;
        this.shop = shop;
        this.stocksCategories = stocksCategories;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new ItemListFragment(shop, stocksCategories.get(position));
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }

    public void filterList(List<StocksData> stocksDataList, int tabPos) {
        shop = stocksDataList;
        notifyDataSetChanged();
    }
}
