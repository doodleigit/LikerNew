package com.liker.android.Group.service;

import com.liker.android.Authentication.model.City;
import com.liker.android.Authentication.model.Country;
import com.liker.android.Authentication.model.ForgotPassword;
import com.liker.android.Authentication.model.LoginUser;
import com.liker.android.Authentication.model.ResendStatus;
import com.liker.android.Tool.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface GroupWebservice {


    Retrofit retrofitBase = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /*CREATE GROUP*/
/*Group create API - http://192.168.0.77:8040/sites/likerapp/createGroup
Form Data: user_id:
category_id:
group_name:
permission:
invite_users[0]:
invite_users[1]:*/

    @POST(AppConstants.CREATE_GROUP)
    @FormUrlEncoded
    Call<String> createGroup(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("category_id") String categoryId,
            @Field("group_name") String groupName,
            @Field("permission") String permission
    );


}

