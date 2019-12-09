package com.liker.android.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.doodle.Profile.model.Story;
//import com.doodle.R;
//import com.doodle.Tool.Tools;

import com.liker.android.Profile.model.Story;
import com.liker.android.R;
import com.liker.android.Tool.Tools;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Story> arrayList;

    public StoryAdapter(Context context, ArrayList<Story> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_story, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title, summary, permissionType;
        title = arrayList.get(i).getType();
        summary = arrayList.get(i).getDescription();
        permissionType = arrayList.get(i).getPermissionType();

        viewHolder.tvTitle.setText(Tools.getStoryType(title));
        viewHolder.tvSummary.setText(summary);
        if (permissionType.equals("1")) {
            viewHolder.ivPermissionIcon.setImageResource(R.drawable.ic_only_me_12dp);
        } else if (permissionType.equals("2")) {
            viewHolder.ivPermissionIcon.setImageResource(R.drawable.ic_friends_12dp);
        } else {
            viewHolder.ivPermissionIcon.setImageResource(R.drawable.ic_public_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvSummary;
        ImageView ivPermissionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title);
            tvSummary = itemView.findViewById(R.id.summary);
            ivPermissionIcon = itemView.findViewById(R.id.permission_icon);
        }
    }

}
