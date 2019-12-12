package com.liker.android.Group.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Group.model.GroupMember;
import com.liker.android.Profile.model.FollowersResult;
import com.liker.android.Profile.service.FollowUnfollowClickListener;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;
import java.util.List;

//import com.doodle.App;
//import com.doodle.Profile.model.FollowersResult;
//import com.doodle.Profile.service.FollowUnfollowClickListener;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {

    private Context context;
    private List<GroupMember> groupMembers;
    private FollowUnfollowClickListener followUnfollowClickListener;

    public GroupMemberAdapter(Context context, List<GroupMember> groupMembers, FollowUnfollowClickListener followUnfollowClickListener) {
        this.context = context;
        this.groupMembers = groupMembers;
        this.followUnfollowClickListener = followUnfollowClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_group_member, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fullName, photo, likes, stars;
        fullName = groupMembers.get(i).getFirstName() + " " + groupMembers.get(i).getLastName();
        photo = AppConstants.PROFILE_IMAGE + groupMembers.get(i).getPhoto();
        likes = groupMembers.get(i).getTotalLikes();
        stars = groupMembers.get(i).getGoldStars();

        viewHolder.userName.setText(fullName);
        viewHolder.likes.setText(likes + " " + context.getString(R.string.likes));
        viewHolder.stars.setText(stars + " " + context.getString(R.string.stars));
/*
        if (groupMembers.get(i).getIsFollowed()) {
            viewHolder.follow.setText(context.getString(R.string.unfollow));
        } else {
            viewHolder.follow.setText(context.getString(R.string.follow));
        }*/

        Glide.with(App.getAppContext())
                .load(photo)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.userImage);

    /*    viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupMembers.get(i).getIsFollowed()) {
                    followUnfollowClickListener.onUnFollowClick(arrayList.get(i).getUserId(), i);
                } else {
                    followUnfollowClickListener.onFollowClick(arrayList.get(i).getUserId(), i);
                }
            }
        });*/

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", groupMembers.get(i).getUserId()).putExtra("user_name", groupMembers.get(i).getUserName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, likes, stars;
        Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            likes = itemView.findViewById(R.id.likes);
            stars = itemView.findViewById(R.id.stars);
            follow = itemView.findViewById(R.id.follow);
        }
    }

}
