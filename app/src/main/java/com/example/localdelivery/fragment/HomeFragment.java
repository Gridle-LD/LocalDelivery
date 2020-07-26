package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.adapter.ShopsAdapter;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ShopsAdapter shopsAdapter;
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private List<ShopsEntity> nearbyShops;
    private List<ShopsEntity> nearbyShopsCopy;
    private PrefUtils prefUtils;
    private NearbyShopsViewModel nearbyShopsViewModel;
    private ConstraintLayout constraintLayoutSort;
    private ConstraintLayout constraintLayoutFilter;
    private boolean isOpened = false;
    private boolean isGrocerySelected = false;
    private boolean isDairySelected = false;
    private boolean isDeliveryAvailableSelected = false;


    public HomeFragment() {
        // Required empty public constructor
        nearbyShops = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setView(view);
        setSearchView();
        getNearbyShops();
        setTextListeners();

        progressBar.setVisibility(View.GONE);
        return view;
    }

    private void setView(View view) {
        searchView = view.findViewById(R.id.searchView);
        constraintLayoutSort = view.findViewById(R.id.constraint_layout_sort_button);
        constraintLayoutFilter = view.findViewById(R.id.constraint_layout_filter_button);
        recyclerView = view.findViewById(R.id.recycler_view_nearby_shops);
        progressBar = view.findViewById(R.id.progressBar3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        shopsAdapter = new ShopsAdapter(mContext, nearbyShops);
        recyclerView.setAdapter(shopsAdapter);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    mActivity.onBackPressed();
                    isOpened = false;
                    return true;
                }
                return false;
            }
        });
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

    private void getNearbyShops() {

        nearbyShopsViewModel = ViewModelProviders.of(HomeFragment.this)
                .get(NearbyShopsViewModel.class);
        nearbyShopsViewModel.getShopsList().observe(getViewLifecycleOwner(),
                new Observer<List<ShopsEntity>>() {
            @Override
            public void onChanged(List<ShopsEntity> shopsEntities) {
                nearbyShops = shopsEntities;
                shopsAdapter = new ShopsAdapter(mContext, shopsEntities);
                recyclerView.setAdapter(shopsAdapter);

                shopsAdapter.setOnItemClickListener(
                        new ShopsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(mContext, ShopDetailActivity.class);

                                //knowing the position of the clicked shop
                                intent.putExtra(String.valueOf(ShopDetailActivity.position), position);
                                startActivity(intent);
                            }
                        });
            }
        });
    }

    private void setTextListeners() {

        constraintLayoutSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                            .replace(R.id.frame_layout_sort, new SortFragment()).addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getFragmentManager().popBackStack();
                }
            }
        });

        constraintLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                            R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left)
                            .replace(R.id.frame_layout_filter, new FilterFragment(isGrocerySelected, isDairySelected
                                    ,isDeliveryAvailableSelected)).addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    //called from MainActivity when list is filtered
    public void setFilterClick(boolean isGrocerySelected, boolean isDairySelected,
                               boolean isDeliveryAvailableSelected) {

        //TODO : Complete Filter and Sort Section

        isOpened = false;
        this.isGrocerySelected = isGrocerySelected;
        this.isDairySelected = isDairySelected;
        this.isDeliveryAvailableSelected = isDeliveryAvailableSelected;

        nearbyShopsCopy = new ArrayList<>();
        if(!isGrocerySelected && !isDairySelected && !isDeliveryAvailableSelected) {
            nearbyShopsCopy.addAll(nearbyShops);
        }
        else {
            if(isGrocerySelected) {
                for(ShopsEntity shopsEntity : nearbyShops) {
                    if(shopsEntity.getShopType()!=null){
                        if(shopsEntity.getShopType().equals("Grocery")) {
                            nearbyShopsCopy.add(shopsEntity);
                        }
                    }
                }
            }
        }

        shopsAdapter = new ShopsAdapter(mContext, nearbyShopsCopy);
        recyclerView.setAdapter(shopsAdapter);

        shopsAdapter.setOnItemClickListener(
                new ShopsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);

                        //knowing the position of the clicked shop
                        intent.putExtra(String.valueOf(ShopDetailActivity.position), position);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nearbyShopsViewModel.getDisposable().dispose();
    }
}
