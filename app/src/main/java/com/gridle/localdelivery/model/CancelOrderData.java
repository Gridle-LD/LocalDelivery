package com.gridle.localdelivery.model;

import java.util.List;

public class CancelOrderData {

    private String orderId;
    private String status;
    private List<OrdersResponse.Result.Orders.Order.Items> items;
    private String type;

    public CancelOrderData(String orderId, String status, List<OrdersResponse.Result.Orders.Order.Items> items,
                           String type) {
        this.orderId = orderId;
        this.status = status;
        this.items = items;
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrdersResponse.Result.Orders.Order.Items> getItems() {
        return items;
    }

    public void setItems(List<OrdersResponse.Result.Orders.Order.Items> items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
