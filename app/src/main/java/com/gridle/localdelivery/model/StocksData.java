package com.gridle.localdelivery.model;

public class StocksData {

    private String _id;
    private String name;
    private String price;
    private String type;
    private String image;
    private boolean available;
    private String shop;
    private String createdAt;
    private int quantity;

    public StocksData(String _id, String name, String price, String type,
                      String image, boolean available, String shop, String createdAt) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
        this.available = available;
        this.shop = shop;
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}