package com.gridle.localdelivery.model;

public class ForgotPasswordData {

    private String phoneNumber;

    public ForgotPasswordData(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
