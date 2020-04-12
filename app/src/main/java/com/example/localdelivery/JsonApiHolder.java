package com.example.localdelivery;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonApiHolder {

    @POST("signUp")
    Call<SignUpResponse> signUp(@Body SignUpData signUpData);

    @POST("otpVerify/{userId}")
    Call<OtpResponse> verifyOtp(@Path("userId") String userId, @Body OtpData otpData);

    @POST("login")
    Call<LoginResponse> login(@Body LoginData loginData);
}
