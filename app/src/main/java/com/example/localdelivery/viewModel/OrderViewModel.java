package com.example.localdelivery.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.model.CancelOrderData;
import com.example.localdelivery.model.OrdersResponse;
import com.example.localdelivery.repository.OrderRepository;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository orderRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public CompositeDisposable getDisposable() {
        return orderRepository.getDisposable();
    }

    public OrdersResponse getCompleteOrderList() {
        return orderRepository.getCompleteOrderList();
    }

    public boolean cancelOrder(CancelOrderData cancelOrderData) {
        return orderRepository.cancelOrder(cancelOrderData);
    }
}
