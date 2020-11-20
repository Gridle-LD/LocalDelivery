package com.gridle.localdelivery.model;

public class LoginResponse {

    private String token;
    private String userId;
    private String name;

    public LoginResponse(String token, String userId, String name) {
        this.token = token;
        this.userId = userId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
