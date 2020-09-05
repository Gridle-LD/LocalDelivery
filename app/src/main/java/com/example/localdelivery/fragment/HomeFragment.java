package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.adapter.ShopsAdapter;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private EditText editTextSearchView;
    private RecyclerView recyclerView;
    private ShopsAdapter shopsAdapter;
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private List<ShopsEntity> nearbyShops;
    private List<ShopsEntity> nearbyShopsCopy;
    private List<ShopsEntity> filteredList = new ArrayList<>();
    private PrefUtils prefUtils;
    private NearbyShopsViewModel nearbyShopsViewModel;
    private ConstraintLayout constraintLayoutSort;
    private ConstraintLayout constraintLayoutFilter;
    private boolean isOpened = false;
    private boolean isGrocerySelected = false;
    private boolean isOthersSelected = false;
    private boolean isDeliveryAvailableSelected = false;
    private boolean isRatingSelected = false;
    private boolean isPopularitySelected = false;
    private boolean isDistanceSelected = false;
    private ImageView imageViewNoStores;

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
        getNearbyShops();
        setTextListeners();
        return view;
    }

    private void setView(View view) {
        editTextSearchView = view.findViewById(R.id.searchView);
        constraintLayoutSort = view.findViewById(R.id.constraint_layout_sort_button);
        constraintLayoutFilter = view.findViewById(R.id.constraint_layout_filter_button);
        recyclerView = view.findViewById(R.id.recycler_view_nearby_shops);
        imageViewNoStores = view.findViewById(R.id.imageViewNoStores);
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

    private void filter(String text) {
        filteredList = new ArrayList<>();

        for(ShopsEntity shopsEntity : nearbyShops) {
            if(shopsEntity.getShopName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(shopsEntity);
            }
        }
        setAdapter(filteredList);
    }

    private int getPos(int pos, List<ShopsEntity> shopsEntityList) {
        if(shopsEntityList.size()!=0) {
            String id = shopsEntityList.get(pos).get_id();
            int position = 0;
            for(ShopsEntity shopsEntity : nearbyShops) {
                if(shopsEntity.get_id().equals(id)) {
                    break;
                }
                ++position;
            }
            return position;
        }
        return pos;
    }

    private void getNearbyShops() {

        nearbyShopsViewModel = ViewModelProviders.of(HomeFragment.this)
                .get(NearbyShopsViewModel.class);
        nearbyShopsViewModel.getShopsList().observe(getViewLifecycleOwner(),
                new Observer<List<ShopsEntity>>() {
            @Override
            public void onChanged(List<ShopsEntity> shopsEntities) {
                nearbyShops = shopsEntities;
                setAdapter(nearbyShops);
            }
        });
    }

    private void setTextListeners() {

        editTextSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        constraintLayoutSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                            .replace(R.id.frame_layout_sort, new SortFragment(isRatingSelected,
                                    isPopularitySelected,
                                    isDistanceSelected), "sort").addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        constraintLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpened) {
                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                            R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left)
                            .replace(R.id.frame_layout_filter, new FilterFragment(isGrocerySelected,
                                    isOthersSelected
                                    , isDeliveryAvailableSelected), "filter").addToBackStack(null).commit();
                    isOpened = true;
                }
                else {
                    isOpened = false;
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    //called from MainActivity when list is filtered
    public void setFilterClick(boolean isGrocerySelected, boolean isOthersSelected,
                               boolean isDeliveryAvailableSelected) {

        //TODO : Complete Filter and Sort Section

        isOpened = false;
        this.isGrocerySelected = isGrocerySelected;
        this.isOthersSelected = isOthersSelected;
        this.isDeliveryAvailableSelected = isDeliveryAvailableSelected;

        nearbyShopsCopy = new ArrayList<>();
        if(!isGrocerySelected && !isOthersSelected && !isDeliveryAvailableSelected) {
            nearbyShopsCopy.addAll(nearbyShops);
        }
        else {
            if(isGrocerySelected) {
                for(ShopsEntity shopsEntity : nearbyShops) {
                    if(shopsEntity.getShopType()!=null){
                        if(shopsEntity.getShopType().equals("Grocery")) {
                            if(!nearbyShopsCopy.contains(shopsEntity)) {
                                nearbyShopsCopy.add(shopsEntity);
                            }
                        }
                    }
                }
            }
            if(isOthersSelected) {
                for(ShopsEntity shopsEntity : nearbyShops) {
                    if(shopsEntity.getShopType()!=null){
                        if(shopsEntity.getShopType().equals("Others")) {
                            if(!nearbyShopsCopy.contains(shopsEntity)) {
                                nearbyShopsCopy.add(shopsEntity);
                            }
                        }
                    }
                }
            }
        }

        setAdapter(nearbyShopsCopy);
    }

    public void setSortClick(boolean isRatingSelected, boolean isPopularitySelected, boolean isDistanceSelected) {
        isOpened = false;
        this.isRatingSelected = isRatingSelected;
        this.isPopularitySelected = isPopularitySelected;
        this.isDistanceSelected = isDistanceSelected;

        nearbyShopsCopy = new ArrayList<>(nearbyShops);
        if(!isRatingSelected && !isPopularitySelected && !isDistanceSelected) {
            nearbyShopsCopy = nearbyShops;
        }
        else {
            if(isRatingSelected) {
                Collections.sort(nearbyShopsCopy, ShopsEntity.RatingComparator);
            }
            if(isPopularitySelected) {
                Collections.sort(nearbyShopsCopy, ShopsEntity.PopularityComparator);
            }
            if(isDistanceSelected) {
                Collections.sort(nearbyShopsCopy, ShopsEntity.DistanceComparator);
            }
        }

        setAdapter(nearbyShopsCopy);
    }

    private void setAdapter(final List<ShopsEntity> nearbyShopsCopy) {
        shopsAdapter = new ShopsAdapter(mContext, nearbyShopsCopy);
        recyclerView.setAdapter(shopsAdapter);

        if(nearbyShopsCopy.size()==0) {
            imageViewNoStores.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNoStores.setVisibility(View.GONE);
        }

        shopsAdapter.setOnItemClickListener(
                new ShopsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Fragment fragmentFilter = getParentFragmentManager().findFragmentByTag("filter");
                        Fragment fragmentSort = getParentFragmentManager().findFragmentByTag("sort");

                        //to remove filter or sort fragment when clicked
                        if((fragmentFilter!=null && fragmentFilter.isVisible()) ||
                                (fragmentSort!=null && fragmentSort.isVisible())) {
                            getParentFragmentManager().popBackStack();
                            isOpened = false;
                        }
                        else {
                            Intent intent = new Intent(mContext, ShopDetailActivity.class);

                            //knowing the position of the clicked shop
                            intent.putExtra(String.valueOf(ShopDetailActivity.position), getPos(position,
                                    nearbyShopsCopy));
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nearbyShopsViewModel.getDisposable().dispose();
    }
}
