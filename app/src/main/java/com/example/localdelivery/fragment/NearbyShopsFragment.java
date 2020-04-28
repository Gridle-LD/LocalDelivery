package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.ShopDetailsActivity;
import com.example.localdelivery.adapter.NearbyShopsAdapter;
import com.example.localdelivery.local.ShopsEntity;
import com.example.localdelivery.model.NearbyShopsData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.viewModel.NearbyShopsViewModel;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NearbyShopsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NearbyShopsAdapter nearbyShopsAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private List<ShopsEntity> nearbyShops;
    private PrefUtils prefUtils;
    private NearbyShopsViewModel nearbyShopsViewModel;

    public NearbyShopsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_nearby_shops, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
        recyclerView = view.findViewById(R.id.recycler_view_nearby_shops);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        nearbyShopsAdapter = new NearbyShopsAdapter(mContext, nearbyShops);
        recyclerView.setAdapter(nearbyShopsAdapter);

        getNearbyShops();
        return view;
    }

    private void getNearbyShops() {

        nearbyShopsViewModel = ViewModelProviders.of(NearbyShopsFragment.this)
                .get(NearbyShopsViewModel.class);
        nearbyShopsViewModel.getShopsList().observe(getViewLifecycleOwner(), new Observer<List<ShopsEntity>>() {
            @Override
            public void onChanged(List<ShopsEntity> shopsEntities) {
                nearbyShopsAdapter = new NearbyShopsAdapter(mContext, shopsEntities);
                recyclerView.setAdapter(nearbyShopsAdapter);

                nearbyShopsAdapter.setOnItemClickListener(
                        new NearbyShopsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(mContext, ShopDetailsActivity.class);
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
