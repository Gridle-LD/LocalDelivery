package com.gridle.localdelivery.local.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table", indices = {@Index(value = {"_id"}, unique = true)})
public class OrderEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String _id;
    private String status;
    private boolean pickUp;
    private String shopName;
    private String totalPrice;
    private String createdAt;

    @Ignore
    public OrderEntity() {}

    public OrderEntity(String _id, String status, boolean pickUp, String shopName, String totalPrice, String createdAt) {
        this._id = _id;
        this.status = status;
        this.pickUp = pickUp;
        this.shopName = shopName;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
