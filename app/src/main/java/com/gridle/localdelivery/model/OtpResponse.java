package com.gridle.localdelivery.model;

public class OtpResponse {

    private String message;
    private String token;
    private String userId;

    public OtpResponse(String message, String token, String userId) {
        this.message = message;
        this.token = token;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
