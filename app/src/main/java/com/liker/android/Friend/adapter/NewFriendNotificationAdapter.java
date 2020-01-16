package com.liker.android.Friend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Friend.model.Friend;
import com.liker.android.Friend.model.FriendRequestSend;
import com.liker.android.Friend.model.FriendRequestStatus;
import com.liker.android.Friend.view.NewFriendNotificationActivity;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NewFriendNotificationAdapter extends RecyclerView.Adapter<NewFriendNotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Friend> arrayList;
    private PrefManager manager;


    public NewFriendNotificationAdapter(Context context, ArrayList<Friend> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        manager = new PrefManager(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_new_friend_notification, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fullName, photo, likes, stars;
        fullName = arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName();
        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getPhoto();
        likes = arrayList.get(i).getTotalLikes();
        stars = arrayList.get(i).getGoldStars();

        viewHolder.userName.setText(fullName);
        viewHolder.likes.setText(likes + " " + context.getString(R.string.likes)+" :");
        viewHolder.stars.setText(stars + " " + context.getString(R.string.stars));
        getFriendRequestAcceptStatus(viewHolder, i);
        getFriendRequestAcceptByUserStatus(viewHolder, i);
        getRejectFriendRequestStatus(viewHolder, i);
        getRejectFriendRequestByReceiverStatus(viewHolder, i);


        Glide.with(App.getAppContext())
                .load(photo)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.userImage);

        viewHolder.btnConfirmFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                acceptFriendRequest(arrayList.get(i));
            }
        });
        viewHolder.btnDeleteFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectFriendRequest(arrayList.get(i));
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", arrayList.get(i).getFromId()).putExtra("user_name", arrayList.get(i).getUserName()));
            }
        });

    }

    private void acceptFriendRequest(Friend friend) {
        Socket wSocket = new SocketIOManager().getWSocketInstance();
        if (wSocket != null && wSocket.connected() && manager.getProfileId() != null && !manager.getProfileId().isEmpty()) {

            FriendRequestSend friendRequestSend = new FriendRequestSend();
            Headers headers = new Headers();
            headers.setDeviceId(manager.getDeviceId());
            headers.setIsApps(true);
            headers.setSecurityToken(manager.getToken());
            headers.setUserId(manager.getProfileId());
            friendRequestSend.setUserId(friend.getToId());
            friendRequestSend.setToUserId(friend.getFromId());
            friendRequestSend.setHeaders(headers);
            Gson gson = new Gson();
            String json = gson.toJson(friendRequestSend);
            wSocket.emit("friend_request_accept", json);
            //You are now friends
            //Request removed


        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, likes, stars, tvFriendRequestStatus;
        Button btnConfirmFriendRequest, btnDeleteFriendRequest;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            likes = itemView.findViewById(R.id.likes);
            stars = itemView.findViewById(R.id.stars);
            tvFriendRequestStatus = itemView.findViewById(R.id.tvFriendRequestStatus);
            btnConfirmFriendRequest = itemView.findViewById(R.id.btnConfirmFriendRequest);
            btnDeleteFriendRequest = itemView.findViewById(R.id.btnDeleteFriendRequest);

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

    private void rejectFriendRequest(Friend friend) {
        Socket mSocket = new SocketIOManager().getMSocketInstance();
        if (mSocket != null && mSocket.connected() && manager.getProfileId() != null && !manager.getProfileId().isEmpty()) {
            FriendRequestStatus friendRequestStatus = new FriendRequestStatus();
            Headers headers = new Headers();
            headers.setDeviceId(manager.getDeviceId());
            headers.setIsApps(true);
            headers.setSecurityToken(manager.getToken());
            headers.setUserId(manager.getProfileId());
            friendRequestStatus.setUserId(manager.getProfileId());
            friendRequestStatus.setFriendUserId(friend.getFromId());
            friendRequestStatus.setHeaders(headers);
            Gson gson = new Gson();
            String json = gson.toJson(friendRequestStatus);
            mSocket.emit("cancel_friend_request", json);

        }

    }


    private void getFriendRequestAcceptStatus(ViewHolder viewHolder, int i) {
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
                    String fromId = String.valueOf(toUserId);
                    int userId = newPostResultJson.getInt("user_id");


                    if (status == 1 && arrayList.get(i).getFromId().equalsIgnoreCase(fromId)) {
                        ((NewFriendNotificationActivity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                viewHolder.btnConfirmFriendRequest.setVisibility(View.GONE);
                                viewHolder.btnDeleteFriendRequest.setVisibility(View.GONE);
                                viewHolder.tvFriendRequestStatus.setVisibility(View.VISIBLE);
                                viewHolder.tvFriendRequestStatus.setText("You are now friends");
                              //  notifyDataSetChanged();
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

    private void getFriendRequestAcceptByUserStatus(ViewHolder viewHolder, int i) {

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
                    String fromId = String.valueOf(toUserId);
                    int userId = newPostResultJson.getInt("user_id");


                    if (status == 1 && arrayList.get(i).getFromId().equalsIgnoreCase(fromId)) {
                        ((NewFriendNotificationActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.btnConfirmFriendRequest.setVisibility(View.GONE);
                                viewHolder.btnDeleteFriendRequest.setVisibility(View.GONE);
                                viewHolder.tvFriendRequestStatus.setVisibility(View.VISIBLE);
                                viewHolder.tvFriendRequestStatus.setText("You are now friends");
                              //  notifyDataSetChanged();
//                                notifyItemChanged(position);
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

    private void getRejectFriendRequestStatus(ViewHolder viewHolder, int i) {//by whom reject get own status
        Socket mSocket = new SocketIOManager().getMSocketInstance();
        mSocket.on("cancel_friend_request_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject newPostResultJson = new JSONObject(args[0].toString());
                    boolean status = newPostResultJson.getBoolean("status");
                    String friendUserId = newPostResultJson.getString("friend_user_id");
                    if (status && arrayList.get(i).getFromId().equalsIgnoreCase(friendUserId)) {
                        ((NewFriendNotificationActivity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                viewHolder.btnConfirmFriendRequest.setVisibility(View.GONE);
                                viewHolder.btnDeleteFriendRequest.setVisibility(View.GONE);
                                viewHolder.tvFriendRequestStatus.setVisibility(View.VISIBLE);
                                viewHolder.tvFriendRequestStatus.setText("Request removed");
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

    private void getRejectFriendRequestByReceiverStatus(ViewHolder viewHolder, int i) {//event from reciever
        Socket mSocket = new SocketIOManager().getMSocketInstance();
        mSocket.on("cancel_friend_request_by_receiver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject newPostResultJson = new JSONObject(args[0].toString());
                    boolean status = newPostResultJson.getBoolean("status");
                    String friendUserId = newPostResultJson.getString("friend_user_id");
                    if (status && arrayList.get(i).getFromId().equalsIgnoreCase(friendUserId)) {

                        ((NewFriendNotificationActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.btnConfirmFriendRequest.setVisibility(View.GONE);
                                viewHolder.btnDeleteFriendRequest.setVisibility(View.GONE);
                                viewHolder.tvFriendRequestStatus.setVisibility(View.VISIBLE);
                                viewHolder.tvFriendRequestStatus.setText("Request removed");
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
