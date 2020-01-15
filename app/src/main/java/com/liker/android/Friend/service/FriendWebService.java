package com.liker.android.Friend.service;

import com.liker.android.Friend.model.NewFriendNotificationItem;
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
/*http://192.168.0.6:8040/sites/likerapp/newFriendNotification*/
    @POST(AppConstants.NEW_FRIEND_NOTIFICATION)
    @FormUrlEncoded
    Call<NewFriendNotificationItem> newFriendNotification(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );


}
