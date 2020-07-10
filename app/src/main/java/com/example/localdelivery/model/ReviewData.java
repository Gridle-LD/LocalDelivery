package com.example.localdelivery.model;

public class ReviewData {

    private int rating;
    private String comment;
    private String shopId;

    public ReviewData(int rating, String comment, String shopId) {
        this.rating = rating;
        this.comment = comment;
        this.shopId = shopId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
