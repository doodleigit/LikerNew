package com.liker.android.Tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;


/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {

    public SharedPreferences pref, introPref;
    SharedPreferences.Editor editor, introEditor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Liker";
    private static final String INTRO_PREF_NAME = "IntroLiker";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_SECOND_TIME_LAUNCH = "IsSecondTimeLaunch";
    private static final String IS_VALIDATE_FIELD = "IsValidateField";

    private static final String DEVICE_WIDTH = "device_width";
    private static final String DEVICE_ID = "device_id";
    private static final String OAUTH_TOKEN = "oauth_token";
    private static final String FB_FIRST_NAME = "first_name";
    private static final String USER_NAME = "user_name";
    private static final String PROFILE_NAME = "profile_name";
    private static final String PROFILE_IMAGE = "profile_image";
    private static final String TWITTER_FIRST_NAME = "first_name";
    private static final String FB_LAST_NAME = "last_name";
    private static final String TWITTER_LAST_NAME = "last_name";
    private static final String FB_NAME = "name";
    private static final String TWITTER_NAME = "name";
    private static final String FB_EMAIL = "fb_email";
    private static final String TWITTER_EMAIL = "fb_email";
    private static final String FB_BIRTH_DAY = "birthday";
    private static final String TWITTER_BIRTH_DAY = "birthday";
    private static final String OAUTH_ID = "oauth_id";
    private static final String PROFILE_ID = "profile_id";
    private static final String TWITTER_OAUTH_ID = "oauth_id";
    private static final String FB_IMAGE_URL = "image_url";
    private static final String TWITTER__IMAGE_URL = "image_url";
    private static final String POST_PERMISSION = "post_permission";
    private static final String POST_AUDIENCE = "post_audience";
    private static final String NEW_NOTIFICATION = "new_notification";
    private static final String NEW_POST = "new_post";
    private static final String NEW_MESSAGE_NOTIFICATION = "new_message_notification";
    private static final String NEW_FRIEND_NOTIFICATION = "new_friend_notification";
    private static final String USER_INFO = "user_info";

    private static final String POST_CATEGORY_INTRO = "post_category-intro";
    private static final String POST_LIKE_INTRO = "user_info";
    private static final String COMMENT_LIKE_INTRO = "user_info";
    private static final String NEW_POST_INTRO = "new_post_intro";
    private static final String NEW_POST_AUDIENCE_INTRO = "new_post_audience_intro";
    private static final String FIRST_TIME_RATING_SHOW = "first_time_rating_show";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        introPref = _context.getSharedPreferences(INTRO_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDeviceWidth(int deviceWidth) {
        editor.putInt(DEVICE_WIDTH, deviceWidth);
        editor.apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public void setFirstTimeRatingShow(boolean isFirstTime) {
        editor.putBoolean(FIRST_TIME_RATING_SHOW, isFirstTime);
        editor.apply();
    }

    public void setValidateField(boolean isFirstTime) {
        editor.putBoolean(IS_VALIDATE_FIELD, isFirstTime);
        editor.apply();
    }

    public void setSecondTimeLaunch(boolean isSecondtTime) {
        editor.putBoolean(IS_SECOND_TIME_LAUNCH, isSecondtTime);
        editor.apply();
    }

    public void setNotificationCount() {
        editor.putInt(NEW_NOTIFICATION, getNotificationCount() + 1);
        editor.apply();
    }

    public void setPostCount() {
        editor.putInt(NEW_POST, getPostCount() + 1);
        editor.apply();
    }

    public void setNotificationCountClear() {
        editor.putInt(NEW_NOTIFICATION, 0);

        editor.apply();
    }

    public void setPostCountClear() {
        editor.putInt(NEW_POST, 0);

        editor.apply();
    }

    public void setMessageNotificationCount() {
        editor.putInt(NEW_MESSAGE_NOTIFICATION, getMessageNotificationCount() + 1);
        editor.apply();
    }

    public void setFriendNotificationCount() {
        editor.putInt(NEW_FRIEND_NOTIFICATION, getFriendNotificationCount() + 1);
        editor.apply();
    }

    public void setMessageNotificationCountClear() {
        editor.putInt(NEW_MESSAGE_NOTIFICATION, 0);
        editor.apply();
    }

    public void setFriendNotificationCountClear() {
        editor.putInt(NEW_FRIEND_NOTIFICATION, 0);
        editor.apply();
    }

    public int getNotificationCount() {
        return pref.getInt(NEW_NOTIFICATION, 0);
    }

    public int getPostCount() {
        return pref.getInt(NEW_POST, 0);
    }

    public int getMessageNotificationCount() {
        return pref.getInt(NEW_MESSAGE_NOTIFICATION, 0);
    }

    public int getFriendNotificationCount() {
        return pref.getInt(NEW_FRIEND_NOTIFICATION, 0);
    }

//    public boolean isFirstTimeLaunch() {
//        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
//    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }

    public boolean isFirstTimeRatingShow() {
        return pref.getBoolean(FIRST_TIME_RATING_SHOW, false);
    }


    public boolean isValidateField() {
        return pref.getBoolean(IS_VALIDATE_FIELD, false);
    }

    public boolean isSecondTimeLaunch() {
        return pref.getBoolean(IS_SECOND_TIME_LAUNCH, false);
    }

    public int getDeviceWidth() {
        return pref.getInt(DEVICE_WIDTH, 450);
    }

    public boolean getSwitchStatus(String keyValue, boolean defaultValue) {
        return pref.getBoolean(keyValue, defaultValue);
    }

    public String getDeviceId() {

        return pref.getString(DEVICE_ID, "");
    }

    public String getPostPermission() {

        return pref.getString(POST_PERMISSION, "Public");
    }

    public String getToken() {
        return pref.getString(OAUTH_TOKEN, "");
    }

    public String getPostAudience() {
        return pref.getString(POST_AUDIENCE, "Audience");
    }

    public String getOauthId() {
        return pref.getString(OAUTH_ID, "");
    }

    public String getTwitterOauthId() {
        return pref.getString(TWITTER_OAUTH_ID, "");
    }

    public void setStatus(String key, boolean defaultValue) {
        editor.putBoolean(key, defaultValue);
    }

    public void setDeviceId(String deviceId) {
        editor.putString(DEVICE_ID, deviceId);
        editor.apply();
    }

    public void setToken(String token) {
        editor.putString(OAUTH_TOKEN, token);
        editor.apply();
    }

    public void setPostPermission(String postPermission) {
        editor.putString(POST_PERMISSION, postPermission);
        editor.apply();
    }

    public void setPostAudience(String postAudience) {
        editor.putString(POST_AUDIENCE, postAudience);
        editor.apply();
    }

    //oauth_id
    public void setOauthId(String oauthId) {
        editor.putString(OAUTH_ID, oauthId);
        editor.apply();
    }

    public void setProfileId(String profileId) {
        editor.putString(PROFILE_ID, profileId);
        editor.apply();
    }

    public void setTwitterOauthId(String oauthId) {
        editor.putString(TWITTER_OAUTH_ID, oauthId);
        editor.apply();
    }

    public void setFbFirstName(String firstName) {
        editor.putString(FB_FIRST_NAME, firstName);
        editor.apply();
    }

    public void setProfileName(String profileName) {
        editor.putString(PROFILE_NAME, profileName);
        editor.apply();
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public void setProfileImage(String profileImage) {
        editor.putString(PROFILE_IMAGE, profileImage);
        editor.apply();
    }

    public void setTwitterFirstName(String firstName) {
        editor.putString(TWITTER_FIRST_NAME, firstName);
        editor.apply();
    }

    public void setFbLastName(String lastNmae) {
        editor.putString(FB_LAST_NAME, lastNmae);
        editor.apply();
    }

    public void setTwitterLastName(String lastNmae) {
        editor.putString(TWITTER_LAST_NAME, lastNmae);
        editor.apply();
    }

    public void setFbEmail(String email) {
        editor.putString(FB_EMAIL, email);
        editor.apply();
    }

    public void setTwitterEmail(String email) {
        editor.putString(TWITTER_EMAIL, email);
        editor.apply();
    }

    public void setFbName(String name) {
        editor.putString(FB_NAME, name);
        editor.apply();
    }

    public void setTwitterName(String name) {
        editor.putString(TWITTER_NAME, name);
        editor.apply();
    }

    public void setFbImageUrl(String imageUrl) {
        editor.putString(FB_IMAGE_URL, imageUrl);
        editor.apply();
    }

    public void setTwitter_imageUrl(String imageUrl) {
        editor.putString(TWITTER__IMAGE_URL, imageUrl);
        editor.apply();
    }

    public void setFbBirthDay(String birthDay) {
        editor.putString(FB_BIRTH_DAY, birthDay);
        editor.apply();
    }

    public void setTwitterBirthDay(String birthDay) {
        editor.putString(TWITTER_BIRTH_DAY, birthDay);
        editor.apply();
    }

    public void setUserInfo(String userInfo) {
        editor.putString(USER_INFO, userInfo);
        editor.apply();
    }

    public void setPostCategoryIntro(String postCategoryIntro) {
        introEditor = introPref.edit();
        introEditor.putString(POST_CATEGORY_INTRO, postCategoryIntro);
        introEditor.apply();
    }

    public void setPostLikeIntro(String postLikeIntro) {
        introEditor = introPref.edit();
        introEditor.putString(POST_LIKE_INTRO, postLikeIntro);
        introEditor.apply();
    }

    public void setCommentLikeIntro(String commentLikeIntro) {
        introEditor = introPref.edit();
        introEditor.putString(COMMENT_LIKE_INTRO, commentLikeIntro);
        introEditor.apply();
    }

    public void setNewPostIntro(String newPostIntro) {
        introEditor = introPref.edit();
        introEditor.putString(NEW_POST_INTRO, newPostIntro);
        introEditor.apply();
    }

    public void setNewPostAudienceIntro(String newPostAudienceIntro) {
        introEditor = introPref.edit();
        introEditor.putString(NEW_POST_AUDIENCE_INTRO, newPostAudienceIntro);
        introEditor.apply();
    }

    public String getUserInfo() {

        return pref.getString(USER_INFO, "");
    }

    public String getFbFirstName() {

        return pref.getString(FB_FIRST_NAME, "");
    }

    public String getProfileName() {

        return pref.getString(PROFILE_NAME, "");
    }

    public String getUserName() {

        return pref.getString(USER_NAME, "");
    }

    public String getProfileId() {

        return pref.getString(PROFILE_ID, "");
    }

    public String getProfileImage() {

        return pref.getString(PROFILE_IMAGE, "");
    }

    public String getTwitterFirstName() {

        return pref.getString(TWITTER_FIRST_NAME, "");
    }

    public String getFbLastName() {

        return pref.getString(FB_LAST_NAME, "");
    }

    public String getTwitterLastName() {

        return pref.getString(TWITTER_LAST_NAME, "");
    }

    public String getFbName() {

        return pref.getString(FB_NAME, "");
    }

    public String getTwitterName() {

        return pref.getString(TWITTER_NAME, "");
    }

    public String getFbEmail() {

        return pref.getString(FB_EMAIL, "");
    }

    public String getTwitterEmail() {

        return pref.getString(TWITTER_EMAIL, "");
    }

    public String getFbImageUrl() {

        return pref.getString(FB_IMAGE_URL, "");
    }

    public String getTwitterImageUrl() {

        return pref.getString(TWITTER__IMAGE_URL, "");
    }

    public String getFbBirthDay() {

        return pref.getString(FB_BIRTH_DAY, "");
    }

    public String getTwitterBirthDay() {

        return pref.getString(TWITTER_BIRTH_DAY, "");
    }

    public String getPostCategoryIntro() {
        return introPref.getString(POST_CATEGORY_INTRO, "0");
    }

    public String getPostLikeIntro() {
        return introPref.getString(POST_LIKE_INTRO, "0");
    }

    public String getCommentLikeIntro() {
        return introPref.getString(COMMENT_LIKE_INTRO, "0");
    }

    public String getNewPostIntro() {
        return introPref.getString(NEW_POST_INTRO, "0");
    }

    public String getNewPostAudienceIntro() {
        return introPref.getString(NEW_POST_AUDIENCE_INTRO, "0");
    }
}
//    public void setUserId(String userId){
//        editor.putString(USER_ID_KEY, userId);
//        editor.apply();
//    }
//    public String getUserId(){
//        return pref.getString(USER_ID_KEY,"");
//    }
//}
