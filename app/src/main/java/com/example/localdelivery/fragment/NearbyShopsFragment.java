package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.adapter.NearbyShopsAdapter;
import com.example.localdelivery.model.NearbyShopsData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.utils.RetrofitInstance;

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
    private List<NearbyShopsResponse.NearbyShopsObject> nearbyShops;

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
        NearbyShopsData nearbyShopsData = new NearbyShopsData("26.4213363", "80.3622929");
        disposable.add(
                jsonApiHolder.getNearbyShops(nearbyShopsData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<NearbyShopsResponse>() {
                            @Override
                            public void onSuccess(NearbyShopsResponse nearbyShopsResponse) {
                                nearbyShops = nearbyShopsResponse.getMessage();
                                nearbyShopsAdapter = new NearbyShopsAdapter(mContext, nearbyShops);
                                recyclerView.setAdapter(nearbyShopsAdapter);

                                nearbyShopsAdapter.setOnItemClickListener(
                                        new NearbyShopsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent();
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
