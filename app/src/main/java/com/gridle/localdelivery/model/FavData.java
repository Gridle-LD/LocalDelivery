package com.gridle.localdelivery.model;

public class FavData {

    private String shopId;
    private int action;

    public FavData(String shopId, int action) {
        this.shopId = shopId;
        this.action = action;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
