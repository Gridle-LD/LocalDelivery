package com.gridle.localdelivery.model;

public class PayTmCheckSumData {

    private String mid;
    private String orderId;
    private String value;

    public PayTmCheckSumData(String mid, String orderId, String value) {
        this.mid = mid;
        this.orderId = orderId;
        this.value = value;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
