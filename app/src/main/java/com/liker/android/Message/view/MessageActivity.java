package com.liker.android.Message.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Message.model.FriendInfo;
import com.liker.android.Message.service.OnlineStatusChangeListener;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.Tools;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.liker.android.Tool.AppConstants.IN_CHAT_MODE;

public class MessageActivity extends AppCompatActivity {

    private Socket mSocket;
    public OnlineStatusChangeListener listUserOnlineListener, messagingUserOnlineListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        IN_CHAT_MODE = true;
        mSocket = SocketIOManager.mSocket;

        boolean isMessageFromProfile = getIntent().getBooleanExtra("messageFromProfile", false);
        initialFragment(isMessageFromProfile);
        getUsersOnlineStatus();
    }

    private void initialFragment(boolean isMessageFromProfile) {
        if (isMessageFromProfile) {
            FriendInfo friendInfo = (FriendInfo) getIntent().getSerializableExtra("friendInfo");
            Bundle bundle = new Bundle();
            bundle.putSerializable("friend_info", friendInfo);
            MessagingFragment messagingFragment = new MessagingFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            messagingFragment.setArguments(bundle);
            transaction.replace(R.id.container, messagingFragment).commit();
        } else {
            MessageListFragment messageListFragment = new MessageListFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, messageListFragment).commit();
        }
    }

    private void getUsersOnlineStatus() {
        mSocket.on("get_online_user", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    String userId, online;
                    userId = jsonObject.getString("follower_id");
                    online = jsonObject.getString("online");
                    if (listUserOnlineListener != null) {
                        listUserOnlineListener.onOnlineListener(userId, online, "1");
                    }
                    if (messagingUserOnlineListener != null) {
                        messagingUserOnlineListener.onOnlineListener(userId, online, "1");
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("get_offline_user", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    String userId, online;
                    userId = jsonObject.getString("follower_id");
                    online = jsonObject.getString("online");
                    if (listUserOnlineListener != null) {
                        listUserOnlineListener.onOnlineListener(userId, online, "1");
                    }
                    if (messagingUserOnlineListener != null) {
                        messagingUserOnlineListener.onOnlineListener(userId, online, "1");
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.setPageTraffic(this, AppConstants.MESSAGE_PAGE_NUMBER); //For page traffic analytics
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("get_online_user");
        mSocket.off("get_offline_user");
        IN_CHAT_MODE = false;
    }

}
