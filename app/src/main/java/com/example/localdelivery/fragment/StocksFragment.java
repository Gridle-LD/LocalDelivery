package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.StocksTabLayoutAdapter;
import com.example.localdelivery.model.StocksData;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class StocksFragment extends Fragment {

    private SearchView searchView;
    private Context mContext;
    private Activity mActivity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static List<StocksData> shop;
    private ImageView imageViewViewCart;
    private List<StocksData> cartList;
    private String shopId;
    private String shopName;
    private TextView textViewShopName;
    private boolean isPickup;

    public StocksFragment() {
        // Required empty public constructor
    }

    public StocksFragment(List<StocksData> shop, String shopId, String shopName, boolean isPickup) {
        cartList = new ArrayList<>();
        StocksFragment.shop = shop;
        this.shopId = shopId;
        this.shopName = shopName;
        this.isPickup = isPickup;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setView(view);
        setSearchView();
        setTabLayout();
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        searchView = view.findViewById(R.id.searchViewStocks);
        tabLayout = view.findViewById(R.id.tabLayoutStocks);
        viewPager = view.findViewById(R.id.viewPager);
        imageViewViewCart = view.findViewById(R.id.imageViewViewCart);
        textViewShopName = view.findViewById(R.id.textViewShopNameTitle);

        textViewShopName.setText(shopName);
    }

    private void setSearchView() {
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.montserrat_light);
        searchText.setTypeface(typeface);
        searchText.setTextSize(12);

        if(!searchView.isFocused()) {
            searchView.clearFocus();
        }
    }

    public static void changeQuantity(String type, int position, int quantity, String name) {
        int i=0;
        for(StocksData stocksData : shop) {
            if(stocksData.getType().equals(type) && stocksData.getName().equals(name)) {
                shop.get(i).setQuantity(quantity);
            }
            ++i;
        }
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Grocery"));
        tabLayout.addTab(tabLayout.newTab().setText("Skincare"));
        tabLayout.addTab(tabLayout.newTab().setText("Beverages"));
        tabLayout.addTab(tabLayout.newTab().setText("Bakery"));
        tabLayout.addTab(tabLayout.newTab().setText("Organic Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Packed Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Home & Dining"));
        tabLayout.addTab(tabLayout.newTab().setText("Bathroom & Cleaning"));
        tabLayout.addTab(tabLayout.newTab().setText("Electricals and Electronics"));

        StocksTabLayoutAdapter mainPagerAdapter = new StocksTabLayoutAdapter(getFragmentManager(),
                tabLayout.getTabCount(), mContext, shop);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setClickListeners() {
        imageViewViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(StocksData stocksData : shop) {
                    if(stocksData.getQuantity()!=0) {
                        cartList.add(stocksData);
                    }
                }
                if(cartList.size()>0) {
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout_visit_store,
                            new OrderFragment(shop, cartList, shopId, shopName, isPickup)).commit();
                }
                else {
                    Toast.makeText(mContext, "You haven't selected any Item !", Toast.LENGTH_LONG).show();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
