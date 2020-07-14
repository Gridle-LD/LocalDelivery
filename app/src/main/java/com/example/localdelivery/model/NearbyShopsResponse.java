package com.example.localdelivery.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NearbyShopsResponse {

    @SerializedName("result")
    private List<NearbyShopsObject> message;

    public NearbyShopsResponse(List<NearbyShopsObject> message) {
        this.message = message;
    }

    public List<NearbyShopsObject> getMessage() {
        return message;
    }

    public void setMessage(List<NearbyShopsObject> message) {
        this.message = message;
    }

    public static class NearbyShopsObject {
        private ShopDetails shopDetails;
        private List<StocksData> stock;
        private List<String> orders;
        private List<ReviewObject> reviews;
        private String _id;
        private String phoneNumber;
        private String name;

        public NearbyShopsObject(ShopDetails shopDetails, List<StocksData> stock, List<String> orders,
                                 List<ReviewObject> reviews, String _id, String phoneNumber, String name) {
            this.shopDetails = shopDetails;
            this.stock = stock;
            this.orders = orders;
            this.reviews = reviews;
            this._id = _id;
            this.phoneNumber = phoneNumber;
            this.name = name;
        }

        public ShopDetails getShopDetails() {
            return shopDetails;
        }

        public void setShopDetails(ShopDetails shopDetails) {
            this.shopDetails = shopDetails;
        }

        public List<StocksData> getStock() {
            return stock;
        }

        public void setStock(List<StocksData> stock) {
            this.stock = stock;
        }

        public List<String> getOrders() {
            return orders;
        }

        public void setOrders(List<String> orders) {
            this.orders = orders;
        }

        public List<ReviewObject> getReviews() {
            return reviews;
        }

        public void setReviews(List<ReviewObject> reviews) {
            this.reviews = reviews;
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

        public static class ShopDetails {
            private String address;
            private String latitude;
            private String longitude;
            private String shopName;
            private String shopType;

            public ShopDetails(String address, String latitude, String longitude, String shopName, String shopType) {
                this.address = address;
                this.latitude = latitude;
                this.longitude = longitude;
                this.shopName = shopName;
                this.shopType = shopType;
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
        }

        public static class ReviewObject {
            private String _id;
            private int rating;
            private String comment;
            private String shopId;
            private UserId userId;
            private String createdAt;

            public ReviewObject(String _id, int rating, String comment, String shopId, UserId userId, String createdAt) {
                this._id = _id;
                this.rating = rating;
                this.comment = comment;
                this.shopId = shopId;
                this.userId = userId;
                this.createdAt = createdAt;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
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

            public UserId getUserId() {
                return userId;
            }

            public void setUserId(UserId userId) {
                this.userId = userId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public static class UserId {
                private String username;

                public UserId(String username) {
                    this.username = username;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }
            }
        }
    }
}
