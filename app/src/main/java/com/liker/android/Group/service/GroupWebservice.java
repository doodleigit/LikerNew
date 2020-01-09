package com.liker.android.Group.service;

import com.liker.android.Group.model.GroupContent;
import com.liker.android.Group.model.GroupInviteMember;
import com.liker.android.Group.model.GroupWiseCateGoryInfo;
import com.liker.android.Group.model.MyGroupMember;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Tool.AppConstants;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface GroupWebservice {


    Retrofit retrofitBase = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


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

    @POST(AppConstants.GET_GROUP_ABOUT)
    @FormUrlEncoded
    Call<String> getGroupAbout(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId
    );

    @Multipart
    @POST(AppConstants.UPDATE_GROUP_MEDIA)
    Call<String> uploadCoverImage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file,
            @Part("user_id") String userIds,
            @Part("group_id") String groupId

    );


    @POST(AppConstants.GROUP_MEMBERS)
    @FormUrlEncoded
    Call<MyGroupMember> groupMembers(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId
    );


    @POST(AppConstants.LEAVE_MEMBERS)
    @FormUrlEncoded
    Call<String> leaveMembers(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId
    );

    @POST(AppConstants.JOIN_MEMBERS)
    @FormUrlEncoded
    Call<String> joinMembers(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId
    );

    @POST(AppConstants.INVITE_MEMBERS)
    @FormUrlEncoded
    Call<String> inviteMembers(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId,
            @Field("invite_users[]") String[] inviteUsers
    );


    @POST(AppConstants.ALL_GROUP_INFO)
    @FormUrlEncoded
    Call<GroupContent> allGroupInfo(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.SUGGESTED_GROUP)
    @FormUrlEncoded
    Call<GroupContent> suggestedGroup(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.GROUP_YOU_MANAGE)
    @FormUrlEncoded
    Call<GroupContent> groupYouManage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.GROUP_YOU_IN)
    @FormUrlEncoded
    Call<GroupContent> groupYouIn(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.UPDATE_GROUP_ABOUT)
    @FormUrlEncoded
    Call<String> updateGroupabout(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId,
            @Field("description") String description
    );

    @POST(AppConstants.UPDATE_GROUP)
    @FormUrlEncoded
    Call<String> updateGroup(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId,
            @Field("category_id") String categoryId,
            @Field("group_name") String groupName,
            @Field("permission") String permission
    );


    @POST(AppConstants.DELETE_GROUP)
    @FormUrlEncoded
    Call<String> deleteGroup(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId
    );

    @POST(AppConstants.CATEGORY_GROUP)
    @FormUrlEncoded
    Call<GroupWiseCateGoryInfo> categoryGroup(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("category_id") String categoryId
    );

    @POST(AppConstants.GROUP_FEED)
    @FormUrlEncoded
    Call<List<PostItem>> groupFeed(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("cat_id") String cat_id,
            @Field("is_public") boolean isPublic,
            @Field("max_post_id") String maxPostId
            );
/*Get Invite Follower List API - http://192.168.0.77:8040/sites/likerapp/inviteMemberList
Form Data: 	user_id:
			group_id:*/


    @POST(AppConstants.INVITE_MEMBER_LIST)
    @FormUrlEncoded
    Call<GroupInviteMember> inviteMemberList(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("group_id") String groupId,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("fetch_more") boolean fetchMore
    );
}

