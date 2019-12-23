package com.liker.android.Tool.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

//import com.doodle.Home.model.PostItem;
//import com.doodle.Home.service.SocketIOManager;
//import com.doodle.Home.view.activity.StarContributorActivity;
//import com.doodle.Message.model.NewMessage;
//import com.doodle.Message.model.SenderData;
//import com.doodle.Notification.model.BadgeInfo;
//import com.doodle.Notification.model.Data;
//import com.doodle.Notification.model.NotificationItem;
//import com.doodle.Notification.service.NotificationService;
//import com.doodle.Notification.view.NotificationActivity;
//import com.doodle.Post.view.activity.PostPopup;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.ScreenOnOffBroadcast;
//import com.doodle.Tool.Tools;

import com.google.gson.Gson;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Home.view.activity.StarContributorActivity;
import com.liker.android.Message.model.NewMessage;
import com.liker.android.Message.model.OnlineNotify;
import com.liker.android.Message.model.SenderData;
import com.liker.android.Notification.model.NotificationItem;
import com.liker.android.Notification.service.NotificationService;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.ScreenOnOffBroadcast;
import com.liker.android.Tool.Tools;
import com.liker.android.Tool.model.SessionOnline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Tool.AppConstants.IN_CHAT_MODE;

public class DataFetchingService extends Service {

    NotificationManager notificationManager = null;
    private Socket socket, mSocket, nSocket;
    private BroadcastReceiver mReceiver;
    String KEY_NOTIFICATION_GROUP = "LIKER_PUSH_NOTIFICATION";

    private NotificationService socketWebService;
    private String deviceId, token, userIds;
    private PrefManager manager;
    private Handler handler;
    private Runnable runnable;
    private int DELAY_TIME = 20 * 1000;
    private boolean stopRunnable = false;

    @Override
    public void onCreate() {
        super.onCreate();

        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        socketWebService = NotificationService.wRetrofit.create(NotificationService.class);
        handler = new Handler();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ADD_TRAFFIC_BROADCAST);
        registerReceiver(broadcastReceiver, filter);

        if (socket != null) {
            if (!socket.connected()) {
                socket = new SocketIOManager().getWSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            socket = new SocketIOManager().getWSocketInstance();
        }
        if (nSocket != null) {
            if (!nSocket.connected()) {
                nSocket = new SocketIOManager().getNewPostSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            nSocket = new SocketIOManager().getNewPostSocketInstance();
        }
        if (mSocket != null) {
            if (!mSocket.connected()) {
                mSocket = new SocketIOManager().getMSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            mSocket = new SocketIOManager().getMSocketInstance();
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                //call function
                if (stopRunnable) {
                    return;
                }
                if (!isAppIsInBackground(getApplicationContext())) {
                    addTrafficRequest(false, "");
                }
                handler.postDelayed(runnable, DELAY_TIME);
            }
        };

        setStatus();
        setBroadcast();
        getNotificationData();
        getMessagesData();
        getNewPostData();
    }

    private void setStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUserStatus(true);
                setSessionUser(true, "");
            }
        }, 5 * 1000);
    }

    private void getNewPostData() {
        nSocket.on("new_post_result", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject newPostResultJson = new JSONObject(args[0].toString());
                    boolean status = newPostResultJson.getBoolean("status");
                    String message = newPostResultJson.getString("message");
                    JSONObject dataJson = newPostResultJson.getJSONObject("data");
                    int total = dataJson.getInt("total");
                    JSONArray followers = dataJson.getJSONArray("followers");
                    int permission = dataJson.getInt("permission");

                    sendBroadcast((
                            new Intent().putExtra("status", status)
                                    .putExtra("message", message))
                            .putExtra("total", total)
                            .putExtra("followerArray", followers.toString())
                            .putExtra("permission", permission)
                            .setAction(AppConstants.NEW_POST_BROADCAST_FROM_HOME));

//                    newMessage.setUserId(messageJson.getString("user_id"));
//                    newMessage.setToUserId(messageJson.getString("to_user_id"));
//                    newMessage.setMessage(messageJson.getString("message"));
//                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
//                    newMessage.setTimePosted(messageJson.getString("time_posted"));
//                    newMessage.setInsertId(messageJson.getString("insert_id"));
//                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));
//
//                    SenderData senderData = new SenderData();
//                    senderData.setId(messageJson.getJSONObject("user_data").getString("id"));
//                    senderData.setUserId(messageJson.getJSONObject("user_data").getString("user_id"));
//
//                    newMessage.setSenderData(senderData);
//                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
//                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("is_own", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST));


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }

            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!socket.connected()) {
            socket = new SocketIOManager().getWSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        if (!nSocket.connected()) {
            nSocket = new SocketIOManager().getNewPostSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        if (!mSocket.connected()) {
            mSocket = new SocketIOManager().getMSocketInstance();
//                Toast.makeText(context, "Message Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    private void getNotificationData() {
        socket.on("web_notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                sendBroadcast((new Intent().putExtra("type", "0")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
//                try {
//                    JSONObject object = new JSONObject(args[0].toString());
//                    NotificationItem notificationItem = new NotificationItem();
//                    Data data = new Data();
//
//                    notificationItem.setText(object.getString("text"));
//                    notificationItem.setData(data);
//                    JSONObject jsonObject = object.getJSONObject("data");
//                    notificationItem.getData().setId(jsonObject.getString("id"));
//                    notificationItem.getData().setUserId(jsonObject.getString("user_id"));
//                    notificationItem.getData().setFromUserId(jsonObject.getString("from_user_id"));
//                    notificationItem.getData().setNotifType(jsonObject.getString("notif_type"));
//                    notificationItem.getData().setTypeId(jsonObject.getString("type_id"));
//                    notificationItem.getData().setTimeSent(jsonObject.getString("time_sent"));
//                    notificationItem.getData().setHasSeen(jsonObject.getString("has_seen"));
//                    notificationItem.getData().setHasSeenDetails(jsonObject.getString("has_seen_details"));
//                    notificationItem.getData().setCategoryId(jsonObject.getString("category_id"));
//                    notificationItem.getData().setSubCatgId(jsonObject.getString("sub_catg_id"));
//                    notificationItem.getData().setUsername(jsonObject.getString("username"));
//                    notificationItem.getData().setFirstName(jsonObject.getString("first_name"));
//                    notificationItem.getData().setLastName(jsonObject.getString("last_name"));
//                    notificationItem.getData().setPhoto(jsonObject.getString("photo"));
//                    notificationItem.getData().setGoldStars(jsonObject.getString("gold_stars"));
//                    notificationItem.getData().setSliverStars(jsonObject.getString("sliver_stars"));
//                    notificationItem.getData().setCategoryName(jsonObject.getString("category_name"));
//                    notificationItem.getData().setBadgeInfo(new BadgeInfo());
//                    notificationItem.getData().setIsFollowed(jsonObject.getBoolean("is_followed"));
//                    notificationItem.getData().setPostUserUsername(jsonObject.getString("post_user_username"));
//
//                    sendPushNotification(DataFetchingService.this, notificationItem);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        handler.postDelayed(runnable, DELAY_TIME);

    }

//    private void addTraffic() {
//        AddTraffic addTraffic = new AddTraffic();
//        Headers headers = new Headers();
//        headers.setDeviceId(deviceId);
//        headers.setIsApps(true);
//        headers.setSecurityToken(token);
//        addTraffic.setHeaders(headers);
//        addTraffic.setUserId(userIds);
//        addTraffic.setPathName("");
//        addTraffic.setPathNameNew("");
//        addTraffic.setDeviceType("App");
//        addTraffic.setStayTime(DELAY_TIME);
//        addTraffic.setNotify(true);
//        Gson gson = new Gson();
//        String json = gson.toJson(addTraffic);
//        socket.emit("add_traffic_new", json);
//    }

    private void setUserStatus(boolean status) {
        OnlineNotify onlineNotify = new OnlineNotify();
        Headers headers = new Headers();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        onlineNotify.setUserId(userIds);
        onlineNotify.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(onlineNotify);
        if (status) {
            mSocket.emit("online_users", json);
        } else {
            mSocket.emit("current_offline_user", json);
        }
    }

    private void setSessionUser(boolean status, String pathName) {
        if (socket != null && socket.connected() && userIds != null && !userIds.isEmpty()) {
            SessionOnline sessionOnline = new SessionOnline();
            Headers headers = new Headers();
            headers.setDeviceId(deviceId);
            headers.setIsApps(true);
            headers.setSecurityToken(token);
            sessionOnline.setUserId(userIds);
            sessionOnline.setUrl(pathName);
            sessionOnline.setDeviceType("Android");
            sessionOnline.setHeaders(headers);
            Gson gson = new Gson();
            String json = gson.toJson(sessionOnline);
            if (status) {
                socket.emit("session_online_user", json);
            } else {
                socket.emit("remove_online_user", json);
            }
        }
    }

    private void getMessagesData() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("is_own", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST));

//                    Intent intent = new Intent();
//                    intent.setAction(AppConstants.NEW_MESSAGE_BROADCAST);
//                    intent.putExtra("new_message", (Parcelable) newMessage);
//                    intent.putExtra("is_own", type);
//                    getActivity().sendBroadcast(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }
                if (!IN_CHAT_MODE)
                    sendBroadcast((new Intent().putExtra("type", "1")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
            }
        });

        mSocket.on("message_own", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("to_user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("to_user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("to_user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("to_user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("to_user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("to_user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("to_user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("to_user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("to_user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("to_user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("to_user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("to_user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("to_user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("to_user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("to_user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("to_user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 1)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("is_own", 1)).setAction(AppConstants.NEW_MESSAGE_BROADCAST));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }
            }
        });

        mSocket.on("seen_by_user", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    String userId = jsonObject.getString("user_id");
                    sendBroadcast((new Intent().putExtra("to_user_id", userId)).setAction(AppConstants.NEW_MESSAGE_SEEN_BROADCAST));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }
            }
        });
    }

    private void setBroadcast() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOffBroadcast();
        registerReceiver(mReceiver, filter);
    }

    public void sendPushNotification(Context context, NotificationItem notificationItem) {
        Log.d("sendNotification", "true");
        new LoadNotificationMedia(context, notificationItem).execute(AppConstants.PROFILE_IMAGE + notificationItem.getData().getPhoto());
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadNotificationMedia extends AsyncTask<String, Void, Bitmap> {
        Context context;
        NotificationItem notificationItem;

        public LoadNotificationMedia(Context context, NotificationItem notificationItem) {
            this.context = context;
            this.notificationItem = notificationItem;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            Intent intent = new Intent(context, NotificationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            intent.putExtra("notification_item", (Parcelable) notificationItem);
//            intent.putExtra("is_pushed", true);

            int actionType = Tools.getNotificationTypeActionType(notificationItem.getData().getNotifType());

            if (actionType == 1) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("user_id", notificationItem.getData().getFromUserId());
                intent.putExtra("user_name", notificationItem.getData().getUsername());
                pushNotification(notificationItem, bitmap, intent, context);
            } else if (actionType == 2) {
                sendPostItemRequest(notificationItem.getData().getTypeId(), false, notificationItem, bitmap, context);
            } else if (actionType == 3) {
                sendPostItemRequest(notificationItem.getData().getTypeId(), true, notificationItem, bitmap, context);
            } else if (actionType == 4) {
                Intent intent = new Intent(context, StarContributorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("category_id", "");
                intent.putExtra("category_name", "");
                pushNotification(notificationItem, bitmap, intent, context);
            } else if (actionType == 5) {
                Intent intent = new Intent(context, StarContributorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("category_id", notificationItem.getData().getCategoryId());
                intent.putExtra("category_name", notificationItem.getData().getCategoryName());
                pushNotification(notificationItem, bitmap, intent, context);
            } else {
                Intent intent = new Intent();
                pushNotification(notificationItem, bitmap, intent, context);
            }

        }
    }

    private void pushNotification(NotificationItem notificationItem, Bitmap bitmap, Intent intent, Context context) {
        int requestCode = new Random().nextInt();
        int notificationCode = Integer.parseInt(notificationItem.getData().getId().isEmpty() ? String.valueOf(requestCode) : notificationItem.getData().getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        long time = Long.valueOf(notificationItem.getData().getTimeSent());

        RemoteViews contentViews = new RemoteViews(context.getPackageName(), R.layout.push_notification_item);
        contentViews.setTextViewText(R.id.notificationDetails, Tools.colorBackground(notificationItem.getText()));
        contentViews.setTextViewText(R.id.notificationTime, Tools.chatDateCompare(context, time));
        if (bitmap != null) {
            contentViews.setImageViewBitmap(R.id.imageNotification, bitmap);
        } else {
            contentViews.setImageViewResource(R.id.imageNotification, R.drawable.profile);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_setting_black_24dp)
                .setCustomContentView(contentViews)
                .setAutoCancel(true)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setPriority(android.app.Notification.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationCode, notificationBuilder.build());
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy hh:mm a", cal).toString();
        return date;
    }

    private void addTrafficRequest(boolean isPageTraffic, String pathName) {
//        setSessionUser(true, pathName);
        Call<String> call;
        if (isPageTraffic) {
            call = socketWebService.addPageTrafficNew(deviceId, userIds, token, userIds, "Android", pathName);
        } else {
            call = socketWebService.addTrafficNew(deviceId, userIds, token, userIds, "Android", pathName, DELAY_TIME/1000, "");
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendPostItemRequest(String postId, boolean isCommentAction, NotificationItem notificationItem, Bitmap bitmap, Context context) {
//        Call<PostItem> call = webService.getPostDetails(deviceId, profileId, token, profileId, postId);
//        call.enqueue(new Callback<PostItem>() {
//            @Override
//            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
//                PostItem postItem = response.body();
//                if (postItem != null) {
//                    Intent intent = new Intent(context, PostPopup.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    intent.putExtra(AppConstants.ITEM_KEY, (Parcelable) postItem);
//                    intent.putExtra("has_footer", false);
//                    intent.putExtra("is_comment_action", isCommentAction);
//                    pushNotification(notificationItem, bitmap, intent, context);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostItem> call, Throwable t) {
//                Log.d("MESSAGE: ", t.getMessage());
//            }
//        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pageNumber = intent.getIntExtra("page_number", -1);
            switch (pageNumber) {
                case AppConstants.HOME_PAGE_NUMBER:
                    addTrafficRequest(true, "Home");
                    break;
                case AppConstants.MESSAGE_PAGE_NUMBER:
                    addTrafficRequest(true, "Message");
                    break;
                case AppConstants.NOTIFICATION_PAGE_NUMBER:
                    addTrafficRequest(true, "notification");
                    break;
                case AppConstants.POST_PAGE_NUMBER:
                    addTrafficRequest(true, "post");
                    break;
                case AppConstants.PROFILE_PAGE_NUMBER:
                    addTrafficRequest(true, "profile");
                    break;
                case AppConstants.SEARCH_PAGE_NUMBER:
                    addTrafficRequest(true, "search");
                    break;
                case AppConstants.ADVANCE_SEARCH_PAGE_NUMBER:
                    addTrafficRequest(true, "advance_search");
                    break;
                case AppConstants.SETTING_PAGE_NUMBER:
                    addTrafficRequest(true, "setting");
                    break;
            }
        }
    };

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        try {
            Object service = context.getSystemService(Context.ACTIVITY_SERVICE);

            if (service != null) {
                ActivityManager am = (ActivityManager) service;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            for (String activeProcess : processInfo.pkgList) {
                                if (activeProcess.equals(context.getPackageName())) {
                                    isInBackground = false;
                                }
                            }
                        }
                    }
                } else {
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (componentInfo.getPackageName().equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Epic", "Notification > isAppIsInBackground: Exception: " + e.getMessage());
        }

        return isInBackground;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            setUserStatus(false);
            setSessionUser(false, "");
            Log.d("dafafafaffa", "Notification > isAppIsInBackground: Exception: ");
            socket.off("web_notification");
            mSocket.off("message");
            mSocket.off("message_own");
            nSocket.off("new_post_result");
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(mReceiver);
            stopRunnable = true;
            handler.removeCallbacksAndMessages(null);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            setUserStatus(false);
            setSessionUser(false, "");
            socket.off("web_notification");
            mSocket.off("message");
            mSocket.off("message_own");
            nSocket.off("new_post_result");
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(mReceiver);
            stopRunnable = true;
            handler.removeCallbacksAndMessages(null);
        } catch (IllegalArgumentException ignored) {
        }
    }

}
