package com.example.localdelivery.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.local.Dao.ShopsDao;
import com.example.localdelivery.local.ShopsDatabase;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.model.FavData;
import com.example.localdelivery.model.FavResponse;
import com.example.localdelivery.model.NearbyShopsData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NearbyShopsRepository {

    private ShopsDao shopsDao;
    private LiveData<List<ShopsEntity>> allShops;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<NearbyShopsResponse.NearbyShopsObject> nearbyShops;

    public NearbyShopsRepository(Context context) {
        ShopsDatabase shopDatabase = ShopsDatabase.getInstance(context);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(context).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(context);
        this.context = context;
        getShops();
        shopsDao = shopDatabase.shopsDao();
        allShops = shopsDao.getAllShops();
    }

    public void getShops() {
        NearbyShopsData nearbyShopsData = new NearbyShopsData(prefUtils.getLatitude(),
                prefUtils.getLongitude());
        disposable.add(
                jsonApiHolder.getNearbyShops(nearbyShopsData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<NearbyShopsResponse>() {
                            @Override
                            public void onSuccess(NearbyShopsResponse nearbyShopsResponse) {
                                List<ShopsEntity> shopsEntities = new ArrayList<>();
                                nearbyShops = nearbyShopsResponse.getMessage();
                                for(NearbyShopsResponse.NearbyShopsObject shopsObject: nearbyShops) {
                                    ShopsEntity shopsEntity = new ShopsEntity(
                                            shopsObject.getStock(),
                                            shopsObject.getReviews(),
                                            shopsObject.get_id(),
                                            shopsObject.getPhoneNumber(),
                                            shopsObject.getName(),
                                            shopsObject.getShopDetails().getAddress(),
                                            shopsObject.getShopDetails().getLatitude(),
                                            shopsObject.getShopDetails().getLongitude(),
                                            shopsObject.getShopDetails().getShopName(),
                                            shopsObject.getShopDetails().getShopType()
                                    );
                                    shopsEntities.add(shopsEntity);
                                }
                                add(shopsEntities);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "An Error Occurred !", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }));
    }

    public void fav(int pos, int like) {
        if(nearbyShops!=null) {
            FavData favData = new FavData(nearbyShops.get(pos).get_id(), like);
            disposable.add(
                    jsonApiHolder.favourite(favData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<FavResponse>() {
                                @Override
                                public void onSuccess(FavResponse favResponse) {
                                    if(favResponse.getMessage().equals("liked")) {
                                        Toast.makeText(context, "Shop Added to Favourites !", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    if(favResponse.getMessage().equals("unliked")) {
                                        Toast.makeText(context, "Shop Removed from Favourites !", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(context, "An Error Occurred !", Toast.LENGTH_SHORT)
                                            .show();
                                }}));
        }
    }

    @SuppressLint("CheckResult")
    public void add(final List<ShopsEntity> shopsEntities) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                shopsDao.deleteAll();
            }
        }).andThen(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                shopsDao.insert(shopsEntities);
            }
        }))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public LiveData<List<ShopsEntity>> getAllShops() {
        return allShops;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }
}
