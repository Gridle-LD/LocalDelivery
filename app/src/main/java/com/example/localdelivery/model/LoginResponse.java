package com.example.localdelivery.model;

public class LoginResponse {

    private String token;
    private String userId;
    private String name;
    private String message;

    public LoginResponse(String token, String userId, String name) {
        this.token = token;
        this.userId = userId;
        this.name = name;
    }

    public LoginResponse(String userId, String message) {
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
