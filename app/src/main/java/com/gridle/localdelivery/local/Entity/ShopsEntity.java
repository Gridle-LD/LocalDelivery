package com.gridle.localdelivery.local.Entity;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.gridle.localdelivery.local.Converter.ReviewConverter;
import com.gridle.localdelivery.local.Converter.StocksConverter;
import com.gridle.localdelivery.model.NearbyShopsResponse;
import com.gridle.localdelivery.model.StocksData;

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
    private String image;
    private String mid;
    private String mKey;
    private String timings;
    private boolean delivery;
    private String deliveryRadius;
    private String deliveryRate;
    private boolean onlinePayment;
    private int numberOfOrders;
    private double distance;

    @Ignore
    public ShopsEntity() {}

    public ShopsEntity(List<StocksData> stock, List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> reviewList,
                       boolean favourite, String _id, String phoneNumber, String name, String address, String latitude,
                       String longitude, String shopName, String shopType, String image, String mid, String mKey,
                       String timings, boolean delivery, String deliveryRadius, String deliveryRate,
                       boolean onlinePayment, int numberOfOrders, double distance) {
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
        this.image = image;
        this.mid = mid;
        this.mKey = mKey;
        this.timings = timings;
        this.delivery = delivery;
        this.deliveryRadius = deliveryRadius;
        this.deliveryRate = deliveryRate;
        this.onlinePayment = onlinePayment;
        this.numberOfOrders = numberOfOrders;
        this.distance = distance;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryRadius() {
        return deliveryRadius;
    }

    public void setDeliveryRadius(String deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }

    public String getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(String deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public boolean isOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(boolean onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public static Comparator<ShopsEntity> DistanceComparator = new Comparator<ShopsEntity>() {
        @Override
        public int compare(ShopsEntity o1, ShopsEntity o2) {
            String dist1 = String.valueOf(o1.getDistance());
            String dist2 = String.valueOf(o2.getDistance());

            return dist1.compareTo(dist2);
        }
    };

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
