package com.example.localdelivery.local.Entity;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.localdelivery.local.Converter.FavouriteConverter;
import com.example.localdelivery.local.Converter.ReviewConverter;
import com.example.localdelivery.local.Converter.StocksConverter;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.model.StocksData;

import java.util.Comparator;
import java.util.List;

@Entity(tableName = "shops_table", indices = {@Index(value = {"_id"}, unique = true)})
public class ShopsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters({StocksConverter.class})
    private List<StocksData> stock;

    @TypeConverters({ReviewConverter.class})
    private List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList;

    private boolean favourite;

    private String _id;
    private String phoneNumber;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String shopName;
    private String shopType;
    private int numberOfOrders;

    @Ignore
    public ShopsEntity() {}

    public ShopsEntity(List<StocksData> stock, List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList,
                       boolean favourite, String _id, String phoneNumber, String name, String address,
                       String latitude, String longitude, String shopName, String shopType, int numberOfOrders) {
        this.stock = stock;
        this.reviewList = reviewList;
        this.favourite = favourite;
        this._id = _id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shopName = shopName;
        this.shopType = shopType;
        this.numberOfOrders = numberOfOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<StocksData> getStock() {
        return stock;
    }

    public void setStock(List<StocksData> stock) {
        this.stock = stock;
    }

    public List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList) {
        this.reviewList = reviewList;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public static Comparator<ShopsEntity> RatingComparator = new Comparator<ShopsEntity>() {

        @Override
        public int compare(ShopsEntity o1, ShopsEntity o2) {

            String rating1 = calculateRating(o1.getReviewList());
            String rating2 = calculateRating(o2.getReviewList());

            return rating2.compareTo(rating1);
        }
    };

    public static Comparator<ShopsEntity> PopularityComparator = new Comparator<ShopsEntity>() {
        @Override
        public int compare(ShopsEntity o1, ShopsEntity o2) {
            String reviewList1 = String.valueOf(o1.getNumberOfOrders());
            String reviewList2 = String.valueOf(o2.getNumberOfOrders());

            return reviewList2.compareTo(reviewList1);
        }
    };

    // TODO : Distance Comparator

    @SuppressLint("DefaultLocale")
    private static String calculateRating(List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList) {
        int ratingSum = 0;
        double ratingAverage;
        for(NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject reviewObject : reviewList) {
            ratingSum += reviewObject.getRating();
        }
        ratingAverage = ((double) ratingSum)/reviewList.size();
        return String.format("%.1f", ratingAverage);
    }
}
