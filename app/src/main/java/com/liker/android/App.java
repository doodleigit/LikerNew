package com.liker.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.danikula.videocache.HttpProxyCacheServer;
//import com.doodle.Comment.model.CommentPersistData;
//import com.doodle.Comment.model.Comment_;
//import com.doodle.Comment.model.Reply;
//import com.doodle.Home.model.PostItem;
//import com.doodle.Post.model.Category;
//import com.doodle.Reply.model.ReplyPersistData;
//import com.doodle.Post.model.Subcatg;
//import com.doodle.Tool.Operation;
//import com.doodle.Tool.PrefManager;
import com.liker.android.Comment.model.CommentPersistData;
import com.liker.android.Comment.model.Comment_;
import com.liker.android.Comment.model.Reply;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Post.model.Category;
import com.liker.android.Post.model.Subcatg;
import com.liker.android.Reply.model.ReplyPersistData;
import com.liker.android.Tool.Operation;
import com.liker.android.Tool.PrefManager;
import com.onesignal.OneSignal;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

import io.fabric.sdk.android.Fabric;
import java.util.List;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO;

public class App extends Application {


    private static boolean isValidate;
    private static String fbProvider;
    private static String queryResult;
    private static String socketId;
    private static String newPostSocketId;
    private static String categoryId;
    private static boolean isFBSignup;
    private static boolean isTwitterSignup;
    private static boolean isFBLogin;
    private static boolean isTwitterLogin;
    private static final String TAG = App.class.getSimpleName();
    private static App myInstance;
    private static Context mContext;
    PrefManager manager;
    private static Category mCategory;
    private static Subcatg mSubcatg;
    private static String mSocketId;
    private static int commentCount;
    private static String profilePhoto;
    private static boolean isFirstTimeShowReply;
    private static Comment_ commentItem;
    private static Reply replyItem;
    private static PostItem item;
    private static boolean isFollow;
    private static boolean isBockComment;
    private static boolean isBockReply;
    private static boolean notificationStatus;
    private static boolean notificationOff;
    private static boolean deleteStatus;
    private static int position;
    private static boolean isMimPopup;
    private static boolean isImagePopup;
    private static List<String> deleteMediaIds;
    private static boolean isPostShare;
    private static boolean checkEmail;
    private static boolean rvCommentHeader;
    private static boolean sharePostfooter;
    private static CommentPersistData commentPersistData;
    private static ReplyPersistData replyPersistData;


    public static ReplyPersistData getReplyPersistData() {
        return replyPersistData;
    }

    public static void setReplyPersistData(ReplyPersistData replyPersistData) {
        App.replyPersistData = replyPersistData;
    }

    public static CommentPersistData getCommentPersistData() {
        return commentPersistData;
    }

    public static void setCommentPersistData(CommentPersistData commentPersistData) {
        App.commentPersistData = commentPersistData;
    }


    public static boolean isSharePostfooter() {
        return sharePostfooter;
    }

    public static void setSharePostfooter(boolean sharePostfooter) {
        App.sharePostfooter = sharePostfooter;
    }

    public static boolean isRvCommentHeader() {
        return rvCommentHeader;
    }

    public static void setRvCommentHeader(boolean rvCommentHeader) {
        App.rvCommentHeader = rvCommentHeader;
    }

    public static boolean isIsPostShare() {
        return isPostShare;
    }

    public static void setIsPostShare(boolean isPostShare) {
        App.isPostShare = isPostShare;
    }

    public static List<String> getDeleteMediaIds() {
        return deleteMediaIds;
    }

    public static void setDeleteMediaIds(List<String> deleteMediaIds) {
        App.deleteMediaIds = deleteMediaIds;
    }

    public static boolean isCheckEmail() {
        return checkEmail;
    }

    public static void setCheckEmail(boolean checkEmail) {
        App.checkEmail = checkEmail;
    }

    public static boolean isNotificationOff() {
        return notificationOff;
    }

    public static void setNotificationOff(boolean notificationOff) {
        App.notificationOff = notificationOff;
    }

    public static String getNewPostSocketId() {
        return newPostSocketId;
    }

    public static void setNewPostSocketId(String newPostSocketId) {
        App.newPostSocketId = newPostSocketId;
    }

    public static boolean isIsImagePopup() {
        return isImagePopup;
    }

    public static void setIsImagePopup(boolean isImagePopup) {
        App.isImagePopup = isImagePopup;
    }

    public static boolean isIsMimPopup() {
        return isMimPopup;
    }

    public static void setIsMimPopup(boolean isMimPopup) {
        App.isMimPopup = isMimPopup;
    }

    public static boolean isDeleteStatus() {
        return deleteStatus;
    }

    public static void setDeleteStatus(boolean deleteStatus) {
        App.deleteStatus = deleteStatus;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        App.position = position;
    }

    public static Reply getReplyItem() {
        return replyItem;
    }

    public static void setReplyItem(Reply replyItem) {
        App.replyItem = replyItem;
    }

    public static boolean isNotificationStatus() {
        return notificationStatus;
    }

    public static void setNotificationStatus(boolean notificationStatus) {
        App.notificationStatus = notificationStatus;
    }

    public static boolean isIsBockReply() {
        return isBockReply;
    }

    public static void setIsBockReply(boolean isBockReply) {
        App.isBockReply = isBockReply;
    }

    public static boolean isIsBockComment() {
        return isBockComment;
    }

    public static void setIsBockComment(boolean isBockComment) {
        App.isBockComment = isBockComment;
    }

    public static boolean isIsFollow() {
        return isFollow;
    }

    public static void setIsFollow(boolean isFollow) {
        App.isFollow = isFollow;
    }

    public static PostItem getItem() {
        return item;
    }

    public static void setItem(PostItem item) {
        App.item = item;
    }

    public static Comment_ getCommentItem() {
        return commentItem;
    }

    public static void setCommentItem(Comment_ commentItem) {
        App.commentItem = commentItem;
    }

    public static boolean isIsFirstTimeShowReply() {
        return isFirstTimeShowReply;
    }

    public static void setIsFirstTimeShowReply(boolean isFirstTimeShowReply) {
        App.isFirstTimeShowReply = isFirstTimeShowReply;
    }

    public static String getProfilePhoto() {
        return profilePhoto;
    }

    public static void setProfilePhoto(String profilePhoto) {
        App.profilePhoto = profilePhoto;
    }

    public static int getCommentCount() {
        return commentCount;
    }

    public static void setCommentCount(int commentCount) {
        App.commentCount = commentCount;
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    public static String getCategoryId() {
        return categoryId;
    }

    public static void setCategoryId(String categoryId) {
        App.categoryId = categoryId;
    }

    public static Category getmCategory() {
        return mCategory;
    }

    public static void setmCategory(Category mCategory) {
        App.mCategory = mCategory;
    }

    public static Subcatg getmSubcatg() {
        return mSubcatg;
    }

    public static void setmSubcatg(Subcatg mSubcatg) {
        App.mSubcatg = mSubcatg;
    }

    public static String getSocketId() {
        return socketId;
    }

    public static void setSocketId(String socketId) {
        App.socketId = socketId;
    }

    public static String getmSocketId() {
        return mSocketId;
    }

    public static void setmSocketId(String mSocketId) {
        App.mSocketId = mSocketId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO);

        EmojiManager.install(new TwitterEmojiProvider());

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }

        myInstance = this;
        mContext = getApplicationContext();
        manager = new PrefManager(mContext);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                manager.setDeviceId(userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);

            }
        });

        TwitterConfig config = new TwitterConfig.Builder(mContext)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();


        //finally initialize twitter with created configs
        Twitter.initialize(config);
        MultiDex.install(this);
    }

    public static String getQueryResult() {
        return queryResult;
    }

    public static void setQueryResult(String queryResult) {
        App.queryResult = queryResult;
    }

    public static String getFbProvider() {
        return fbProvider;
    }

    public static void setFbProvider(String fbProvider) {
        App.fbProvider = fbProvider;
    }

    public static boolean isIsFBSignup() {
        return isFBSignup;
    }

    public static void setIsFBSignup(boolean isFBSignup) {
        App.isFBSignup = isFBSignup;
    }

    public static boolean isIsTwitterSignup() {
        return isTwitterSignup;
    }

    public static void setIsTwitterSignup(boolean isTwitterSignup) {
        App.isTwitterSignup = isTwitterSignup;
    }


    public static boolean isIsTwitterLogin() {
        return isTwitterLogin;
    }

    public static void setIsTwitterLogin(boolean isTwitterLogin) {
        App.isTwitterLogin = isTwitterLogin;
    }

    public static boolean isIsFBLogin() {
        return isFBLogin;
    }

    public static void setIsFBLogin(boolean isFBLogin) {
        App.isFBLogin = isFBLogin;
    }

    public static synchronized App getInstance() {
        return myInstance;
    }

    public static Context getAppContext() {
        return App.mContext;
    }

    public static boolean isIsValidate() {
        return isValidate;
    }

    public static void setIsValidate(boolean isValidate) {
        App.isValidate = isValidate;
    }


    public static void toggleCustomise(Context context, ActionBarDrawerToggle toggle, String userId) {
        if (!TextUtils.isEmpty(userId)) {

            Bitmap bitmap = Operation.getFacebookProfilePicture(userId);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 90, 90, false);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmapResized);
            roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
            roundedBitmapDrawable.setCircular(true);
            toggle.setHomeAsUpIndicator(roundedBitmapDrawable);
        } else {
            toggle.setHomeAsUpIndicator(R.drawable.ic_sentiment_satisfied_black_24dp);

        }
    }

}
