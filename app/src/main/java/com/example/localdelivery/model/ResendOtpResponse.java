package com.example.localdelivery.model;

public class ResendOtpResponse {

    private String message;

    public ResendOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
