package com.liker.android.Message.view;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.doodle.Home.model.Headers;
//import com.doodle.Home.service.SocketIOManager;
//import com.doodle.Message.adapter.MessagingAdapter;
//import com.doodle.Message.model.Chats;
//import com.doodle.Message.model.FriendInfo;
//import com.doodle.Message.model.MessageSeenParam;
//import com.doodle.Message.model.MessageSendParam;
//import com.doodle.Message.model.Messages;
//import com.doodle.Message.model.NewMessage;
//import com.doodle.Message.model.User;
//import com.doodle.Message.service.MessageService;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.NetworkHelper;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.Tools;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Message.adapter.MessagingAdapter;
import com.liker.android.Message.model.Chats;
import com.liker.android.Message.model.FriendInfo;
import com.liker.android.Message.model.MessageSeenParam;
import com.liker.android.Message.model.MessageSendParam;
import com.liker.android.Message.model.Messages;
import com.liker.android.Message.model.NewMessage;
import com.liker.android.Message.model.User;
import com.liker.android.Message.service.MessageService;
import com.liker.android.Message.service.OnlineStatusChangeListener;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RelativeLayout bottomBar;
    private LinearLayoutManager layoutManager;
    private TextView tvUserName, tvLikes, tvStars, tvUnblock;
    private EditText etReply;
    private ImageView tvSend, ivSetting, ivOnline, ivSeen;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private Socket socket;
    private PrefManager manager;
    private boolean networkOk;
    private MessageService messageService;
    private MessagingAdapter messagingAdapter;
    private FriendInfo friendInfo;
    private ArrayList<Messages> messages;
    private ArrayList<User> users;
    private String deviceId, profileId, token, userName, likes, stars, userIds, toUserId, chatUserName, photo, online, chatboxTurnOnOff;
    private boolean isBlock;
    int limit = 20;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.messaging_fragment_layout, container, false);

        initialComponent();
        return view;
    }

    private void initialComponent() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NEW_MESSAGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, filter);
        IntentFilter seenFilter = new IntentFilter();
        seenFilter.addAction(AppConstants.NEW_MESSAGE_SEEN_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(messageSeenBroadcast, seenFilter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));

        socket = SocketIOManager.mSocket;
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        friendInfo = (FriendInfo) getArguments().getSerializable("friend_info");
        chatUserName = friendInfo.getUserName();
        toUserId = friendInfo.getToUserId();
        userName = friendInfo.getFullName();
        photo = friendInfo.getPhoto();
        likes = friendInfo.getLikes();
        stars = friendInfo.getStars();
        online = friendInfo.getOnline();
        chatboxTurnOnOff = friendInfo.getChatboxTurnOnOff();
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        messageService = MessageService.mRetrofit.create(MessageService.class);
        messages = new ArrayList<>();
        users = new ArrayList<>();

        tvUserName = view.findViewById(R.id.user_name);
        tvLikes = view.findViewById(R.id.likes);
        tvStars = view.findViewById(R.id.stars);
        tvUnblock = view.findViewById(R.id.unblock_text);
        ivSetting = view.findViewById(R.id.setting);
        ivOnline = view.findViewById(R.id.online);
        ivSeen = view.findViewById(R.id.seen);
        tvSend = view.findViewById(R.id.send);
        etReply = view.findViewById(R.id.reply);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        toolbar = view.findViewById(R.id.toolbar);
        progressBar = view.findViewById(R.id.progress_bar);
        bottomBar = view.findViewById(R.id.bottom_bar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        messagingAdapter = new MessagingAdapter(getActivity(), messages, users, userIds);
        recyclerView.setAdapter(messagingAdapter);

        getData();
        setData();
        messageSeen();

        ((MessageActivity) getActivity()).messagingUserOnlineListener = new OnlineStatusChangeListener() {
            @Override
            public void onOnlineListener(String userId, String online, String chatboxTurnOnOff) {
                if (userId.equals(toUserId)) {
                    ivOnline.post(new Runnable() {
                        @Override
                        public void run() {
                            ivOnline.setImageResource(online.equals("1") && chatboxTurnOnOff.equals("1") ? R.drawable.user_online : R.drawable.user_offline);
                        }
                    });
                }
            }
        };

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                getActivity().onBackPressed();
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
                    getPagination();
                }
            }
        });

        tvUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> call = messageService.setUnBlockChatUser(deviceId, profileId, token, userIds, toUserId);
                sendBlockRequest(call, false);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting(ivSetting);
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = etReply.getText().toString();
                if (!reply.isEmpty()) {
                    sendReply(reply);
                }
            }
        });

    }

    private void getData() {
        if (networkOk) {
            Call<Chats> call = messageService.getAllMessage(deviceId, profileId, token, userIds, chatUserName, limit, offset);
            sendAllMessageRequest(call);
            progressDialog.show();
        } else {
            Tools.showNetworkDialog(getChildFragmentManager());
        }
    }

    private void openSetting(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        if (isBlock) {
            popupMenu.getMenuInflater().inflate(R.menu.messaging_unblock_setting, popupMenu.getMenu());
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.messaging_block_setting, popupMenu.getMenu());
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_block) {
                    Call<String> call = messageService.setBlockChatUser(deviceId, profileId, token, userIds, toUserId);
                    sendBlockRequest(call, true);
                } else if (id == R.id.action_unblock) {
                    Call<String> call = messageService.setUnBlockChatUser(deviceId, profileId, token, userIds, toUserId);
                    sendBlockRequest(call, false);
                }
                return true;
            }
        });

        popupMenu.show();
    }

    private void sendReply(String reply) {
        Headers headers = new Headers();
        MessageSendParam messageSendParam = new MessageSendParam();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        messageSendParam.setToUserId(toUserId);
        messageSendParam.setMessage(reply);
        messageSendParam.setUserId(userIds);
        messageSendParam.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(messageSendParam);
        socket.emit("message", json, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
        etReply.setText("");
    }

    private void messageSeen() {
        Headers headers = new Headers();
        MessageSeenParam messageSeenParam = new MessageSeenParam();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        messageSeenParam.setChatUserId(toUserId);
        messageSeenParam.setUserId(userIds);
        messageSeenParam.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(messageSeenParam);
        socket.emit("seen_message", json, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    private void setData() {
        tvUserName.setText(userName);
        tvLikes.setText(likes + " " + getString(R.string.likes));
        tvStars.setText(stars + " " + getString(R.string.stars));
        ivOnline.setImageResource(online.equals("1") && chatboxTurnOnOff.equals("1") ? R.drawable.user_online : R.drawable.user_offline);
    }

    private void getPagination() {
        if (networkOk) {
            progressBar.setVisibility(View.VISIBLE);
            Call<Chats> call = messageService.getAllMessage(deviceId, profileId, token, userIds, chatUserName, limit, offset);
            sendAllMessagePaginationRequest(call);
        }
    }

    private void sendAllMessageRequest(Call<Chats> call) {

        call.enqueue(new Callback<Chats>() {

            @Override
            public void onResponse(Call<Chats> call, Response<Chats> response) {

                Chats chats = response.body();
                if (chats != null) {
                    Collections.reverse(chats.getMessages());
                    messages.addAll(chats.getMessages());
                    users.addAll(chats.getUsers());
                    messagingAdapter.notifyDataSetChanged();
                    offset += limit;
                    isBlock = chats.getPrivacy().getChatUserBlock();
                    changeView(isBlock);
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Chats> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendAllMessagePaginationRequest(Call<Chats> call) {

        call.enqueue(new Callback<Chats>() {

            @Override
            public void onResponse(Call<Chats> call, Response<Chats> response) {

                Chats chats = response.body();
                if (chats != null) {
                    Collections.reverse(chats.getMessages());
                    messages.addAll(chats.getMessages());
                    messagingAdapter.notifyDataSetChanged();
                    offset += limit;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Chats> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void sendBlockRequest(Call<String> call, boolean block) {

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            changeView(block);
                            isBlock = block;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });

    }

    private void changeView(boolean block) {
        for (User user : users) {
            if (toUserId.equals(user.getUserId())) {
                if (user.getOnline() != null) {
                    ivOnline.setImageResource(user.getOnline().equals("1") && user.getChatboxTurnOnOff().equals("1") ? R.drawable.user_online : R.drawable.user_offline);
                    break;
                }
            }
        }
        tvUnblock.setText(getString(R.string.unblock) + " " + friendInfo.getFullName());
        if (block) {
            bottomBar.setVisibility(View.GONE);
            tvUnblock.setVisibility(View.VISIBLE);
        } else {
            bottomBar.setVisibility(View.VISIBLE);
            tvUnblock.setVisibility(View.GONE);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NewMessage newMessage = intent.getParcelableExtra("new_message");
            int isOwnMessage = intent.getIntExtra("is_own", 0);
            for (User user : users) {
                if (user.getUserId().equals(newMessage.getUserId())) {
                    Messages msg = new Messages();
                    msg.setId(newMessage.getInsertId());
                    msg.setFromUserId(newMessage.getUserId());
                    msg.setToUserId(newMessage.getToUserId());
                    msg.setContent(newMessage.getMessage());
                    msg.setTimePosted(newMessage.getTimePosted());
                    msg.setSeen("0"); //Change
                    msg.setReportId("0");
                    msg.setReportStatus("0");
                    msg.setDeletedBy("0");
                    if (messages.size() > 0) {
                        messages.add(0, msg);
                    } else {
                        messages.add(msg);
                    }
                    offset++;
                    break;
                }
            }
            if (isOwnMessage == 0) {
                messageSeen();
            }
            messagingAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }
    };

    BroadcastReceiver messageSeenBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String toId = intent.getStringExtra("to_user_id");
            if (toUserId.equals(toId)) {
                for (int i = 0; i < messages.size(); i++) {
                    messages.get(i).setSeen("1");
                }
//                Glide.with(App.getAppContext())
//                        .load(photo)
//                        .placeholder(R.drawable.ic_sent_24dp)
//                        .error(R.drawable.profile)
//                        .centerCrop()
//                        .dontAnimate()
//                        .into(ivSeen);
                messagingAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(messageSeenBroadcast);
    }
}
