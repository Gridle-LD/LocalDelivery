package com.example.localdelivery.model;

import java.util.List;

public class NearbyShopsResponse {
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
        private List<StocksData> stock;
        private String _id;
        private String phoneNumber;
        private String name;
        private String address;
        private String latitude;
        private String longitude;
        private String shopName;
        private String shopType;

        public NearbyShopsObject(List<StocksData> stock, String _id, String phoneNumber,
                                 String name, String address, String latitude, String longitude,
                                 String shopName, String shopType) {
            this.stock = stock;
            this._id = _id;
            this.phoneNumber = phoneNumber;
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.shopName = shopName;
            this.shopType = shopType;
        }

        public List<StocksData> getStock() {
            return stock;
        }

        public void setStock(List<StocksData> stock) {
            this.stock = stock;
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
    }
}
