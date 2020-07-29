package com.example.localdelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context ctx;

    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_TOKEN = "token";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ADDRESS = "address";
    private static final String NAME = "name";
    private static final String CONTACT_NUMBER = "contactNumber";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";

    public PrefUtils(Context context){
        ctx = context;
        sp = ctx.getSharedPreferences(IS_LOGIN , Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void createLogin(String token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sp.getBoolean(IS_LOGIN, false);
    }

    public String getToken() {
        return sp.getString(KEY_TOKEN, null);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public void setLatitude(String latitude) {
        editor.putString(LATITUDE, latitude).commit();
    }

    public void setLongitude(String longitude) {
        editor.putString(LONGITUDE, longitude).commit();
    }

    public void setAddress(String address) {
        editor.putString(ADDRESS, address).commit();
    }

    public String getLatitude() {
        return sp.getString(LATITUDE, null);
    }

    public String getLongitude() {
        return sp.getString(LONGITUDE, null);
    }

    public String getAddress() {
        return sp.getString(ADDRESS, null);
    }

    public void setName(String name) {
        editor.putString(NAME, name).commit();
    }

    public String getNAME() {
        return sp.getString(NAME, null);
    }

    public void setContactNumber(String contactNumber) {
        editor.putString(CONTACT_NUMBER, contactNumber).commit();
    }

    public String getContactNumber() {
        return sp.getString(CONTACT_NUMBER, null);
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
    }

    public String getUserId() {
        return sp.getString(USER_ID, null);
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
    }

    public String getPassword() {
        return sp.getString(PASSWORD, null);
    }
}
