package com.liker.android.Friend.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liker.android.Friend.adapter.NewFriendNotificationAdapter;
import com.liker.android.Friend.model.Friend;
import com.liker.android.Friend.model.FriendRequestSend;
import com.liker.android.Friend.model.NewFriendNotificationItem;
import com.liker.android.Friend.service.FriendRequestListener;
import com.liker.android.Friend.service.FriendWebService;
import com.liker.android.Group.model.GroupInviteMember;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.R;
import com.liker.android.Setting.view.SettingActivity;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFriendNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewFriendNotification";

    // TODO: 1/15/2020 new Friend notification

    private PrefManager manager;
    private FriendWebService friendWebService;
    private boolean networkOk;
    private String deviceId, token, userId;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private ArrayList<Friend> friends;
    private NewFriendNotificationAdapter adapter;


    private TextView tvFindFriend;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend_notification);
        initialComponent();
        getData();
    }

    private void getData() {
        if (networkOk) {
            progressDialog.show();
            Call<NewFriendNotificationItem> call = friendWebService.newFriendNotification(deviceId, userId, token, userId, limit, offset);
            sendNewFriendRequest(call);
        }
    }

    private void sendNewFriendRequest(Call<NewFriendNotificationItem> call) {
        call.enqueue(new Callback<NewFriendNotificationItem>() {
            @Override
            public void onResponse(Call<NewFriendNotificationItem> call, Response<NewFriendNotificationItem> response) {
                if (response.isSuccessful()) {
                    NewFriendNotificationItem friendNotificationItem = response.body();
                    friends.addAll(friendNotificationItem.getFriends());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: " + friendNotificationItem.toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<NewFriendNotificationItem> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void initialComponent() {
        friendWebService = FriendWebService.retrofit.create(FriendWebService.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        manager = new PrefManager(this);
        friends = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
        progressDialog = new ProgressDialog(this);
        tvFindFriend = findViewById(R.id.tvFindFriend);
        tvFindFriend.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Friend Invites");
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        FriendRequestListener friendRequestListener = new FriendRequestListener() {
            @Override
            public void onRequestConfirmListener(String fromId, String toId, int position, ProgressBar progressBarInvite) {
                acceptFriendRequest(fromId, toId, position, progressBarInvite);
                // TODO: 1/15/2020  You are now friends
            }

            @Override
            public void onRequestDeleteListener(String fromId, String toId, int position, ProgressBar progressBarInvite) {
// TODO: 1/15/2020  Request removed
            }
        };

        adapter = new NewFriendNotificationAdapter(this, friends, friendRequestListener);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                getData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    sendFriendRequestPaginationRequest();
                }
            }
        });
    }

    private void sendFriendRequestPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<NewFriendNotificationItem> call = friendWebService.newFriendNotification(deviceId, userId, token, userId, limit, offset);

        call.enqueue(new Callback<NewFriendNotificationItem>() {
            @Override
            public void onResponse(Call<NewFriendNotificationItem> call, Response<NewFriendNotificationItem> response) {
                NewFriendNotificationItem friendNotificationItem = response.body();
                if (friendNotificationItem != null) {
                    friends.addAll(friendNotificationItem.getFriends());
                    adapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewFriendNotificationItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                friends.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvFindFriend:
                Intent findFriendsIntent = new Intent(this, SettingActivity.class);
                findFriendsIntent.putExtra("type", "findFriends");
                findFriendsIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(findFriendsIntent);
                break;
        }
    }


    private void acceptFriendRequest(String fromId, String toId, int position, ProgressBar progressBarInvite) {


        /*accept friend request :
----------------------------
web Socket :
==========
emit : friend_request_accept
parameter :
user_id: ,
to_user_id: ,
headers:*/
        Socket wSocket = new SocketIOManager().getWSocketInstance();
        if (wSocket != null && wSocket.connected() && manager.getProfileId() != null && !manager.getProfileId().isEmpty()) {

            FriendRequestSend friendRequestSend = new FriendRequestSend();
            Headers headers = new Headers();
            headers.setDeviceId(manager.getDeviceId());
            headers.setIsApps(true);
            headers.setSecurityToken(manager.getToken());
            headers.setUserId(manager.getProfileId());
            friendRequestSend.setUserId(toId);
            friendRequestSend.setToUserId(fromId);
            friendRequestSend.setHeaders(headers);
            Gson gson = new Gson();
            String json = gson.toJson(friendRequestSend);
            wSocket.emit("friend_request_accept", json);
            //You are now friends
            //Request removed


        }

    }

    private void getFriendRequestAcceptStatus() {
//        mSocket = new SocketIOManager().getMSocketInstance();
        Socket wSocket = new SocketIOManager().getWSocketInstance();
        wSocket.on("friend_request_accept_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject newPostResultJson = new JSONObject(args[0].toString());

                    /*{ status: 1,
  message: 'Friend has accepted.',
  user_id: 26445,
  to_user_id: 26444 }*/
                    int status = newPostResultJson.getInt("status");
                    String message = newPostResultJson.getString("message");
                    int toUserId = newPostResultJson.getInt("to_user_id");
                    int userId = newPostResultJson.getInt("user_id");


                    if (status == 1) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                friendRequestLayout.setVisibility(View.VISIBLE);
//                                acceptFriendStatusLayout.setVisibility(View.INVISIBLE);
//                                tvAddFriendRequestStatus.setText("Unfriend");
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }
            }
        });


    }

    private void getFriendRequestAcceptByUserStatus() {

        //mSocket = new SocketIOManager().getMSocketInstance();
        Socket wSocket = new SocketIOManager().getWSocketInstance();
        wSocket.on("friend_request_accepted_byuser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject newPostResultJson = new JSONObject(args[0].toString());
                    int status = newPostResultJson.getInt("status");
                    String message = newPostResultJson.getString("message");
                    int toUserId = newPostResultJson.getInt("to_user_id");
                    int userId = newPostResultJson.getInt("user_id");


                    if (status == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                friendRequestLayout.setVisibility(View.VISIBLE);
//                                acceptFriendStatusLayout.setVisibility(View.INVISIBLE);
//                                tvAddFriendRequestStatus.setText("Unfriend");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {
                }
            }
        });

    }
}
