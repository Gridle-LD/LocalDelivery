package com.example.localdelivery.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.local.Dao.OrderDao;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.local.ShopsDatabase;
import com.example.localdelivery.model.OrdersResponse;
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

public class OrderRepository {

    private OrderDao orderDao;
    private LiveData<List<OrderEntity>> allOrders;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private OrdersResponse ordersList;

    public OrderRepository(Context context) {
        ShopsDatabase shopDatabase = ShopsDatabase.getInstance(context);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(context).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(context);
        this.context = context;
        getOrders();
        orderDao = shopDatabase.orderDao();
        allOrders = orderDao.getAllOrders();
    }

    private void getOrders() {
        disposable.add(
                jsonApiHolder.getOrders()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<OrdersResponse>() {
                            @Override
                            public void onSuccess(OrdersResponse ordersResponse) {
                                List<OrderEntity> orderEntityList = new ArrayList<>();
                                ordersList = ordersResponse;
                                Log.e("TAG", "onSuccess: " + ordersList.getResult().getOrders()
                                        .get(0).getOrder().get(0).getTotalPrice());
                                for(OrdersResponse.Result.Orders response : ordersList.getResult().getOrders()) {
                                    OrderEntity orderEntity = new OrderEntity(
                                            response.getOrder().get(0).get_id(),
                                            response.getOrder().get(0).getStatus(),
                                            response.getOrder().get(0).isPickUp(),
                                            response.getOrder().get(0).getShopId().getShopDetails().getShopName(),
                                            response.getOrder().get(0).getTotalPrice()
                                    );
                                    orderEntityList.add(orderEntity);
                                }
                                add(orderEntityList);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "An Error Occurred !", Toast.LENGTH_SHORT)
                                .show();
                            }
                        }));
    }

    @SuppressLint("CheckResult")
    public void add(final List<OrderEntity> orderEntities) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                orderDao.deleteAll();
            }
        }).andThen(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                orderDao.insert(orderEntities);
            }
        }))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return allOrders;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public OrdersResponse getCompleteOrderList() { return ordersList; }
}
