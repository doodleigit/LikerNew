package com.liker.android.Notification.service;

//import com.doodle.Home.model.PostItem;
//import com.doodle.Notification.model.NotificationItem;
//import com.doodle.Tool.AppConstants;

import com.liker.android.Home.model.PostItem;
import com.liker.android.Notification.model.NotificationItem;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NotificationService {


    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit wRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.SOCKET_WEB)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @POST(AppConstants.NOTIFICATION)
    @FormUrlEncoded
    Call<ArrayList<NotificationItem>> getNotificationList(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Header("is_apps") boolean isApp,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.GET_POST_DETAILS)
    @FormUrlEncoded
    Call<PostItem> getPostDetails(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("post_id") String postId
    );

    @POST(AppConstants.ADD_TRAFFIC_NEW)
    @FormUrlEncoded
    Call<String> addTrafficNew(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("device_type") String deviceType,
            @Field("pathname") String pathname,
            @Field("stay_time") int stay_time,
            @Field("pathname_new") String pathname_new
    );

    @POST(AppConstants.ADD_PAGE_TRAFFIC_NEW)
    @FormUrlEncoded
    Call<String> addPageTrafficNew(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("device_type") String deviceType,
            @Field("pathname") String pathname
    );

}
