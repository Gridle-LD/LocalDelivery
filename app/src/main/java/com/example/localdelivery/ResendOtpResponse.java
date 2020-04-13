package com.example.localdelivery;

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
