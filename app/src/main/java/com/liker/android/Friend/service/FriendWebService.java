package com.liker.android.Friend.service;

import com.liker.android.Tool.AppConstants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FriendWebService {


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("getFeaturedImages")
    @FormUrlEncoded
    Call<String> isFriendStatus(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String userIds,
            @Field("profile_username") String profileUserName
    );


}
