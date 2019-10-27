package com.liker.android.Message.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Message.model.Friend;
import com.liker.android.Message.service.SuggestedListClickResponseService;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
//import com.doodle.App;
//import com.doodle.Message.model.Friend;
//import com.doodle.Message.service.SuggestedListClickResponseService;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;

import java.util.ArrayList;

public class SuggestedListAdapter extends RecyclerView.Adapter<SuggestedListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Friend> arrayList;
    private SuggestedListClickResponseService suggestedListClickResponseService;

    public SuggestedListAdapter(Context context, ArrayList<Friend> arrayList, SuggestedListClickResponseService suggestedListClickResponseService) {
        this.context = context;
        this.arrayList = arrayList;
        this.suggestedListClickResponseService = suggestedListClickResponseService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_suggested, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvUserName.setText(arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName());
        viewHolder.tvLikes.setText(arrayList.get(i).getTotalLikes() + " " + context.getString(R.string.likes));
        viewHolder.tvStars.setText(arrayList.get(i).getGoldStars() + " " + context.getString(R.string.stars));

        Glide.with(App.getAppContext())
                .load(AppConstants.PROFILE_IMAGE + arrayList.get(i).getPhoto())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.ivUserImage);

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestedListClickResponseService.onListItemClick(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        ImageView ivUserImage;
        TextView tvUserName, tvStars, tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            ivUserImage = itemView.findViewById(R.id.userImage);
            tvUserName = itemView.findViewById(R.id.userName);
            tvStars = itemView.findViewById(R.id.stars);
            tvLikes = itemView.findViewById(R.id.likes);
        }
    }

    public void addPagingData(ArrayList<Friend> friends) {
        arrayList.addAll(friends);
        notifyDataSetChanged();
    }

}
