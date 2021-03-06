package com.liker.android.Profile.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

//import com.doodle.Profile.model.Story;
//import com.doodle.Profile.service.StoryModificationListener;
//import com.doodle.R;

import com.liker.android.Profile.model.Story;
import com.liker.android.Profile.service.StoryModificationListener;
import com.liker.android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddStoryAdapter extends RecyclerView.Adapter<AddStoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Story> arrayList;
    private List<String> storyList;
    private StoryModificationListener storyModificationListener;
    private Story story = null;
    private int editPosition = -1;

    public AddStoryAdapter(Context context, ArrayList<Story> arrayList, StoryModificationListener storyModificationListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.storyList = Arrays.asList(context.getResources().getStringArray(R.array.story_type_list));
        this.storyModificationListener = storyModificationListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_story, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = storyList.get(Integer.valueOf(arrayList.get(i).getType()) - 1);

        viewHolder.tvStoryTitle.setText(title);
        viewHolder.tvStory.setText(arrayList.get(i).getDescription());

        viewHolder.ivChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storyEdit(i);
//                notifyItemRemoved(i);
//                storyModificationListener.onStoryEdit(arrayList.get(i));
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storyDelete(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStoryTitle, tvStory;
        ImageView ivChange, ivDelete;
        Spinner storyPrivacySpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStoryTitle = itemView.findViewById(R.id.story_title);
            ivChange = itemView.findViewById(R.id.change);
            ivDelete = itemView.findViewById(R.id.delete);
            tvStory = itemView.findViewById(R.id.story);
            storyPrivacySpinner = itemView.findViewById(R.id.story_privacy_spinner);
        }
    }

    private void storyEdit(int position) {
        story = arrayList.get(position);
        editPosition = position;
        storyModificationListener.onStoryEdit(story, position);
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    private void storyDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.are_u_sure_you_want_to_delete_the_story));
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                storyModificationListener.onStoryDelete(arrayList.get(position), position);
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.show();
    }

    public void storyEditCancel() {
        if (story != null && editPosition != -1) {
            if (arrayList.size() >= editPosition) {
                arrayList.add(editPosition, story);
            } else {
                arrayList.add(story);
            }
            notifyDataSetChanged();
            story = null;
            editPosition = -1;
        }
    }

}
