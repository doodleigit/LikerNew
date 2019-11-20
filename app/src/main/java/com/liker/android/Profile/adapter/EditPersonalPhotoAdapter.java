package com.liker.android.Profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.service.AddPhotoClickListener;
import com.liker.android.Profile.view.EditPersonalPhotoActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;

public class EditPersonalPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PersonalPhoto> arrayList;
    private AddPhotoClickListener addPhotoClickListener;

    private int ITEM_HOLDER = 0;
    private int ADD_ITEM_HOLDER = 1;

    public EditPersonalPhotoAdapter(Context context, ArrayList<PersonalPhoto> arrayList, AddPhotoClickListener addPhotoClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.addPhotoClickListener = addPhotoClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ITEM_HOLDER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_personal_photo_item, viewGroup, false);
            return new ViewHolder(view);
        } else if (i == ADD_ITEM_HOLDER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_photo_item, viewGroup, false);
            return new AddViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            String url = AppConstants.USER_UPLOADED_IMAGES + arrayList.get(i).getImageName();
            Glide.with(App.getAppContext())
                    .load(url)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.imageView);

            if (arrayList.get(i).isUploading()) {
                holder.imageView.setVisibility(View.GONE);
                holder.close.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.close.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPhotoClickListener.onDeleteClick(arrayList.get(i).getId());
                    arrayList.remove(i);
                    notifyDataSetChanged();
                }
            });

        } else if (viewHolder instanceof AddViewHolder) {
            AddViewHolder holder = (AddViewHolder) viewHolder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPhotoClickListener.onClickListener();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return ADD_ITEM_HOLDER;
        }
        return ITEM_HOLDER;
    }

    private boolean isPositionFooter(int position) {
        return position == arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, close;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            close = itemView.findViewById(R.id.close);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public void addUploadingAnimation(PersonalPhoto personalPhoto) {
        arrayList.add(personalPhoto);
        notifyDataSetChanged();
    }

    public void stopUploadingAnimation(String id, PersonalPhoto personalPhoto) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (id.equals(arrayList.get(i).getId())) {
                arrayList.set(i, personalPhoto);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeUpload(String id) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (id.equals(arrayList.get(i).getId())) {
                arrayList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }
}