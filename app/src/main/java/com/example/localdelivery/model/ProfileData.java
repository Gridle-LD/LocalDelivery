package com.example.localdelivery.model;

public class ProfileData {

    private String address;
    private String latitude;
    private String longitude;
    private String name;

    public ProfileData(String address, String latitude, String longitude, String name) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
