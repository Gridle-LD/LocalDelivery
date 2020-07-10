package com.example.localdelivery.Interface;

import com.example.localdelivery.model.LoginData;
import com.example.localdelivery.model.LoginResponse;
import com.example.localdelivery.model.NearbyShopsData;
import com.example.localdelivery.model.NearbyShopsResponse;
import com.example.localdelivery.model.OtpData;
import com.example.localdelivery.model.OtpResponse;
import com.example.localdelivery.model.PlaceOrderData;
import com.example.localdelivery.model.ResendOtpResponse;
import com.example.localdelivery.model.ReviewData;
import com.example.localdelivery.model.SignUpData;
import com.example.localdelivery.model.SignUpResponse;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonApiHolder {

    @POST("user/signUp")
    Single<SignUpResponse> signUp(@Body SignUpData signUpData);

    @POST("user/otpVerify/{userId}")
    Single<OtpResponse> verifyOtp(@Path("userId") String id, @Body OtpData otpData);

    @POST("user/login")
    Single<LoginResponse> login(@Body LoginData loginData);

    @POST("user/otpResend/{userId}")
    Single<ResendOtpResponse> resendOtp(@Path("userId") String id);

    @PUT("shopaction/getNearbyShops")
    Single<NearbyShopsResponse> getNearbyShops(@Body NearbyShopsData nearbyShopsData);

    @POST("customerAction/placeOrder")
    Single<ResponseBody> placeOrder(@Body PlaceOrderData placeOrderData);

    @POST("customerAction/postReview")
    Single<ResponseBody> postReview(@Body ReviewData reviewData);
}
