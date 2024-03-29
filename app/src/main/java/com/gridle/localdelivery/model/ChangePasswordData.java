package com.gridle.localdelivery.model;

public class ChangePasswordData {

    private String phoneNumber;
    private String newPassword;

    public ChangePasswordData(String phoneNumber, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
