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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Group.service.InviteClickListener;
import com.liker.android.Profile.model.FollowersResult;
import com.liker.android.Profile.service.FollowUnfollowClickListener;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;

//import com.doodle.App;
//import com.doodle.Profile.model.FollowersResult;
//import com.doodle.Profile.service.FollowUnfollowClickListener;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;

public class GroupInviteAdapter extends RecyclerView.Adapter<GroupInviteAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FollowersResult> arrayList;
    private InviteClickListener inviteClickListener;

    public GroupInviteAdapter(Context context, ArrayList<FollowersResult> arrayList, InviteClickListener inviteClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.inviteClickListener = inviteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_group_invite, viewGroup, false);
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


        if (arrayList.get(i).getGroupMemberInvite()) {
            viewHolder.btnInvite.setText(context.getString(R.string.group_invited));
            viewHolder.btnInvite.setEnabled(false);
        } else {
            viewHolder.btnInvite.setText(context.getString(R.string.group_invite));

        }

        Glide.with(App.getAppContext())
                .load(photo)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.userImage);

        viewHolder.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteClickListener.onInviteClick(arrayList.get(i).getUserId(), i);
             //   Toast.makeText(context, "invite user", Toast.LENGTH_SHORT).show();
                /*if (arrayList.get(i).getIsFollowed()) {
                    followUnfollowClickListener.onUnFollowClick(arrayList.get(i).getUserId(), i);
                } else {
                    followUnfollowClickListener.onFollowClick(arrayList.get(i).getUserId(), i);
                }*/
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", arrayList.get(i).getUserId()).putExtra("user_name", arrayList.get(i).getUserName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, likes, stars;
        Button btnInvite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            likes = itemView.findViewById(R.id.likes);
            stars = itemView.findViewById(R.id.stars);
            btnInvite = itemView.findViewById(R.id.btnInvite);
        }
    }

}