package com.liker.android.Friend.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.liker.android.App;
import com.liker.android.Friend.model.Friend;
import com.liker.android.Friend.service.FriendRequestListener;
import com.liker.android.Group.model.InviteMember;
import com.liker.android.Group.service.InviteClickListener;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;

import java.util.ArrayList;

//import com.doodle.App;
//import com.doodle.Profile.model.FollowersResult;
//import com.doodle.Profile.service.FollowUnfollowClickListener;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;

public class NewFriendNotificationAdapter extends RecyclerView.Adapter<NewFriendNotificationAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<Friend> arrayList;
    private FriendRequestListener friendRequestListener;



    public NewFriendNotificationAdapter(Context context, ArrayList<Friend> arrayList, FriendRequestListener friendRequestListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.friendRequestListener = friendRequestListener;
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
        viewHolder.likes.setText(likes + " " + context.getString(R.string.likes));
        viewHolder.stars.setText(stars + " " + context.getString(R.string.stars));

    /*    if (manager.getProfileId().equalsIgnoreCase(arrayList.get(i).getUserId())) {
            viewHolder.btnConfirmFriendRequest.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.btnConfirmFriendRequest.setVisibility(View.VISIBLE);
            if (arrayList.get(i).isIsMember()) {
                viewHolder.btnConfirmFriendRequest.setText("MEMBER");
                viewHolder.btnConfirmFriendRequest.setEnabled(false);
                viewHolder.btnConfirmFriendRequest.setBackgroundResource(R.drawable.drawable_comment);
                viewHolder.btnConfirmFriendRequest.setTextColor(Color.BLACK);
            } else {
                if(arrayList.get(i).isInvite()){
                    viewHolder.btnConfirmFriendRequest.setText(context.getString(R.string.group_invited));
                    viewHolder.btnConfirmFriendRequest.setEnabled(false);
                    viewHolder.btnConfirmFriendRequest.setBackgroundResource(R.drawable.drawable_comment);
                    viewHolder.btnConfirmFriendRequest.setTextColor(Color.BLACK);
                }else {
                    viewHolder.btnConfirmFriendRequest.setText(context.getString(R.string.group_invite));
                    viewHolder.btnConfirmFriendRequest.setEnabled(true);
                    viewHolder.btnConfirmFriendRequest.setBackgroundResource(R.drawable.rectangle_corner_round_thirteen);
                    viewHolder.btnConfirmFriendRequest.setTextColor(Color.WHITE);
                }


            }
        }*/


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
                viewHolder.progressBarInvite.setVisibility(View.VISIBLE);
                viewHolder.btnConfirmFriendRequest.setText("");
                viewHolder.btnConfirmFriendRequest.setEnabled(false);
                viewHolder.btnConfirmFriendRequest.setBackgroundResource(R.drawable.drawable_comment);
                friendRequestListener.onRequestConfirmListener(arrayList.get(i).getFromId(),arrayList.get(i).getToId(), i,viewHolder.progressBarInvite);

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", arrayList.get(i).getFromId()).putExtra("user_name", arrayList.get(i).getUserName()));
            }
        });

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
        TextView userName, likes, stars;
        Button btnConfirmFriendRequest;
        ProgressBar progressBarInvite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            likes = itemView.findViewById(R.id.likes);
            stars = itemView.findViewById(R.id.stars);
            btnConfirmFriendRequest = itemView.findViewById(R.id.btnConfirmFriendRequest);
            progressBarInvite = itemView.findViewById(R.id.progress_bar_invite);
        }
    }



}
