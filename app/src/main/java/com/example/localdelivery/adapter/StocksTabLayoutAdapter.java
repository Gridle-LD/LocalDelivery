package com.example.localdelivery.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.localdelivery.fragment.ItemListFragment;
import com.example.localdelivery.model.StocksData;
import java.util.List;

public class StocksTabLayoutAdapter extends FragmentStatePagerAdapter {

    private int NoOfTabs;
    private List<StocksData> shop;
    private Context context;

    public StocksTabLayoutAdapter(@NonNull FragmentManager fm, int NoOfTabs, Context context, List<StocksData> shop) {
        super(fm);
        this.NoOfTabs = NoOfTabs;
        this.shop = shop;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ItemListFragment(0, shop, "Grocery", context);
            case 1:
                return new ItemListFragment(1, shop, "Skincare", context);
            case 2:
                return new ItemListFragment(2, shop, "Beverages", context);
            case 3:
                return new ItemListFragment(3, shop, "Bakery", context);
            case 4:
                return new ItemListFragment(4, shop, "Organic Food", context);
            case 5:
                return new ItemListFragment(5, shop, "Packed Food", context);
            case 6:
                return new ItemListFragment(6, shop, "Home & Dining", context);
            case 7:
                return new ItemListFragment(7, shop, "Bathroom & Cleaning", context);
            case 8:
                return new ItemListFragment(8, shop, "Electricals and Electronics", context);
            default:
                return null;
        }
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
