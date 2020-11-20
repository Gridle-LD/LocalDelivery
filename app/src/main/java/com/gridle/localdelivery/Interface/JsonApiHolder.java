package com.gridle.localdelivery.Interface;

import com.gridle.localdelivery.model.CancelOrderData;
import com.gridle.localdelivery.model.FavData;
import com.gridle.localdelivery.model.FavResponse;
import com.gridle.localdelivery.model.FeedbackData;
import com.gridle.localdelivery.model.LoginData;
import com.gridle.localdelivery.model.LoginResponse;
import com.gridle.localdelivery.model.NearbyShopsData;
import com.gridle.localdelivery.model.NearbyShopsResponse;
import com.gridle.localdelivery.model.OrdersResponse;
import com.gridle.localdelivery.model.OtpData;
import com.gridle.localdelivery.model.OtpResponse;
import com.gridle.localdelivery.model.PayTmCheckSumData;
import com.gridle.localdelivery.model.PayTmCheckSumResponse;
import com.gridle.localdelivery.model.PlaceOrderData;
import com.gridle.localdelivery.model.ProfileData;
import com.gridle.localdelivery.model.ResendOtpResponse;
import com.gridle.localdelivery.model.ReviewData;
import com.gridle.localdelivery.model.SignUpData;
import com.gridle.localdelivery.model.SignUpResponse;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @PUT("shopAction/getNearbyShops")
    Single<NearbyShopsResponse> getNearbyShops(@Body NearbyShopsData nearbyShopsData);

    @POST("customerAction/placeOrder")
    Single<ResponseBody> placeOrder(@Body PlaceOrderData placeOrderData);

    @POST("customerAction/postReview")
    Single<ResponseBody> postReview(@Body ReviewData reviewData);

    @POST("customerAction/postFeedback")
    Single<ResponseBody> postFeedback(@Body FeedbackData feedbackData);

    @GET("customerAction/getOrders")
    Single<OrdersResponse> getOrders();

    @POST("customerAction/paisa")
    Single<PayTmCheckSumResponse> getPayTmCheckSum(@Body PayTmCheckSumData payTmCheckSumData);

    @PUT("customerAction/fav")
    Single<FavResponse> favourite(@Body FavData favData);

    @PUT("customerAction/changeStatus")
    Single<ResponseBody> cancelOrder(@Body CancelOrderData cancelOrderData);

    @PUT("user/editPersonalData")
    Single<ResponseBody> editProfile(@Body ProfileData profileData);
}
