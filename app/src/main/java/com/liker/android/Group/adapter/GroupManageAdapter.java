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
import com.liker.android.Group.model.GroupManage;
import com.liker.android.Group.service.JoinClickListener;
import com.liker.android.Group.view.GroupPageActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.Tools;

import java.util.ArrayList;

public class GroupManageAdapter extends RecyclerView.Adapter<GroupManageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroupManage> arrayList;
    private JoinClickListener joinClickListener;

    public GroupManageAdapter(Context context, ArrayList<GroupManage> arrayList, JoinClickListener joinClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.joinClickListener = joinClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_group_manage, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String groupName, photo;
        groupName = arrayList.get(i).getName() ;
        photo = AppConstants.USER_UPLOADED_IMAGES + arrayList.get(i).getImageName();

        viewHolder.userName.setText(groupName);

        String groupMember = arrayList.get(i).getTotalMember().equals("0") ? "Members: 0" :"Members: "+ Tools.getFormattedLikerCount(arrayList.get(i).getTotalMember());
        String groupPosts = arrayList.get(i).getTotalPost().equals("0") ? " | Posts: 0" :  " | Posts: " +Tools.getFormattedLikerCount(arrayList.get(i).getTotalPost());
        String allCountInfo =  groupMember + groupPosts;
        viewHolder.tvGroupItemInfoCount.setText(allCountInfo);

/*
        if (arrayList.get(i).getGroupMemberInvite()) {
            viewHolder.btnInvite.setText(context.getString(R.string.group_invited));
            viewHolder.btnInvite.setEnabled(false);
            viewHolder.btnInvite.setBackgroundResource(R.drawable.drawable_comment);
            viewHolder.btnInvite.setTextColor(Color.BLACK);
        } else {
            viewHolder.btnInvite.setText(context.getString(R.string.group_invite));
            viewHolder.btnInvite.setEnabled(true);
            viewHolder.btnInvite.setBackgroundResource(R.drawable.rectangle_corner_round_thirteen);
            viewHolder.btnInvite.setTextColor(Color.WHITE);

        }*/

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
              //  inviteClickListener.onInviteClick(arrayList.get(i).getUserId(), i);
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
               // AppSingleton.getInstance().setGroupId(arrayList.get(i).groupId);
                context.startActivity(new Intent(context, GroupPageActivity.class).putExtra("group_id",arrayList.get(i).groupId));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, tvGroupItemInfoCount;
        Button btnInvite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            tvGroupItemInfoCount = itemView.findViewById(R.id.tvGroupItemInfoCount);
            btnInvite = itemView.findViewById(R.id.btnInvite);
        }
    }

    public void filterList(ArrayList<GroupManage> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

}
