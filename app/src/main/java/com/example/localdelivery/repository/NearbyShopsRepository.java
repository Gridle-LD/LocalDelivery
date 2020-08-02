package com.example.localdelivery.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.activity.SignUpLoginActivity;
import com.example.localdelivery.local.Dao.ShopsDao;
import com.example.localdelivery.local.ShopsDatabase;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.model.FavData;
import com.example.localdelivery.model.FavResponse;
import com.example.localdelivery.model.LoginData;
import com.example.localdelivery.model.LoginResponse;
import com.example.localdelivery.model.NearbyShopsData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class NearbyShopsRepository {

    private ShopsDao shopsDao;
    private LiveData<List<ShopsEntity>> allShops;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<NearbyShopsResponse.Result.NearbyShopsObject> nearbyShops;

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
                                nearbyShops = nearbyShopsResponse.getMessage().getShop();
                                int pos = 0;
                                for(NearbyShopsResponse.Result.NearbyShopsObject shopsObject: nearbyShops) {
                                    ShopsEntity shopsEntity = new ShopsEntity(
                                            shopsObject.getStock(),
                                            shopsObject.getReviews(),
                                            isFav(nearbyShopsResponse, pos),
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
                                    ++pos;
                                }
                                add(shopsEntities);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof HttpException) {
                                    Converter<ResponseBody, ErrorMessage> errorConverter =
                                            RetrofitInstance.getRetrofitInstance(context)
                                                    .responseBodyConverter(ErrorMessage.class, new Annotation[0]);
                                    ErrorMessage errorBody = null;
                                    try {
                                        errorBody = errorConverter.convert(((HttpException) e).response().errorBody());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    if(errorBody.getError().equals("jwt expired")) {
                                        reLogin();
                                    }
                                    else {
                                        Toast.makeText(context, "An Error Occurred !", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                                else {
                                    Toast.makeText(context, "An Error Occurred !", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }));
    }

    private boolean isFav(NearbyShopsResponse nearbyShopsResponse, int pos) {
        List<String> favouriteShopList = nearbyShopsResponse.getMessage().getFavouriteShops();
        String shopId = nearbyShopsResponse.getMessage().getShop().get(pos).get_id();
        return favouriteShopList.contains(shopId);
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

    private void reLogin() {
        LoginData loginData = new LoginData(prefUtils.getContactNumber(), prefUtils.getPassword());

        disposable.add(
                jsonApiHolder.login(loginData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse loginResponse) {
                                prefUtils.createLogin("JWT "+loginResponse.getToken());
                                prefUtils.setUserId(loginResponse.getUserId());
                                prefUtils.setName(loginResponse.getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "Please Re-Login !", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    public LiveData<List<ShopsEntity>> getAllShops() {
        return allShops;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    private static class ErrorMessage {
        private String error;

        public ErrorMessage(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
