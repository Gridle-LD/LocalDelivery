package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.adapter.ShopsAdapter;
import com.example.localdelivery.local.ShopsEntity;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ShopsAdapter shopsAdapter;
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private List<ShopsEntity> nearbyShops;
    private PrefUtils prefUtils;
    private NearbyShopsViewModel nearbyShopsViewModel;
    private EditText editTextLocation;
    private ImageView imageViewFilter;

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
        searchView = view.findViewById(R.id.searchView);
        setSearchView();
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        editTextLocation.setText(prefUtils.getAddress());
        recyclerView = view.findViewById(R.id.recycler_view_nearby_shops);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        shopsAdapter = new ShopsAdapter(mContext, nearbyShops);
        recyclerView.setAdapter(shopsAdapter);
        imageViewFilter = view.findViewById(R.id.imageViewFilterButton);

        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                startActivity(intent);
            }
        });

        getNearbyShops();

        return view;
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
                shopsAdapter = new ShopsAdapter(mContext, shopsEntities);
                recyclerView.setAdapter(shopsAdapter);

                shopsAdapter.setOnItemClickListener(
                        new ShopsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nearbyShopsViewModel.getDisposable().dispose();
    }
}
