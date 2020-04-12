package com.example.localdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PrefUtils {

    private static SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context ctx;

    private static final String IS_LOGIN = "isLoggedIn";

    private static final String KEY_TOKEN = "token";

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

}
