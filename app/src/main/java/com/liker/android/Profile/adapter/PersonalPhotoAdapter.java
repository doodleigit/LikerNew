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
import com.liker.android.Profile.view.PhotoFullViewFragment;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;

public class PersonalPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PersonalPhoto> arrayList;
    private AddPhotoClickListener addPhotoClickListener;

    private int ITEM_HOLDER = 0;
    private int ADD_ITEM_HOLDER = 1;

    public PersonalPhotoAdapter(Context context, ArrayList<PersonalPhoto> arrayList, AddPhotoClickListener addPhotoClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.addPhotoClickListener = addPhotoClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ITEM_HOLDER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_photo_item, viewGroup, false);
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

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> photos = new ArrayList<>();
                    for (PersonalPhoto personalPhoto : arrayList) {
                        photos.add(personalPhoto.getImageName());
                    }
                    fullPhotoView(photos, i);
                }
            });

        } else if (viewHolder instanceof AddViewHolder) {
            AddViewHolder holder = (AddViewHolder) viewHolder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addPhotoClickListener != null)
                        addPhotoClickListener.onClickListener();
                    else
                        editPersonalPhoto(arrayList);
                }
            });
        }
    }

    private void editPersonalPhoto(ArrayList<PersonalPhoto> photos) {
        Intent intent = new Intent(context, EditPersonalPhotoActivity.class);
        intent.putParcelableArrayListExtra("media_files", photos);
        context.startActivity(intent);
    }

    private void fullPhotoView(ArrayList<String> photos, int position) {
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new PhotoFullViewFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("media_files", photos);
        bundle.putInt("position", position);
        dialogFragment.setArguments(bundle);

        dialogFragment.show(ft, "dialog");
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

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
