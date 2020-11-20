package com.gridle.localdelivery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.gridle.localdelivery.fragment.ItemListFragment;
import com.gridle.localdelivery.model.StocksData;

import java.util.List;

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
