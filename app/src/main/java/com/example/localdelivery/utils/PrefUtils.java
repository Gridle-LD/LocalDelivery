package com.example.localdelivery.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.localdelivery.activity.SignUpLoginActivity;

public class PrefUtils {

    private static SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context ctx;

    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_TOKEN = "token";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ADDRESS = "address";

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

        Intent i = new Intent(ctx, SignUpLoginActivity.class);
        ctx.startActivity(i);
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
}
