package com.liker.android.Profile.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.doodle.App;
//import com.doodle.Profile.model.AlbumPhoto;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
import com.github.chrisbanes.photoview.PhotoView;
import com.liker.android.App;
import com.liker.android.Profile.model.AlbumPhoto;
import com.liker.android.Profile.view.PhotoFullViewFragment;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;

public class AlbumPhotoAdapter  extends RecyclerView.Adapter<AlbumPhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AlbumPhoto> arrayList;

    public AlbumPhotoAdapter(Context context, ArrayList<AlbumPhoto> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_photo_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = AppConstants.USER_UPLOADED_IMAGES + arrayList.get(i).getImageName();
        Glide.with(App.getAppContext())
                .load(url)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> photos = new ArrayList<>();
                for (AlbumPhoto albumPhoto : arrayList) {
                    photos.add(albumPhoto.getImageName());
                }
                fullPhotoView(photos, i);
//                viewFullImage(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
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

    private void viewFullImage(String url) {
        Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.image_full_view);

        ImageView close = dialog.findViewById(R.id.close);

        PhotoView photoView = dialog.findViewById(R.id.photo_view);
        Glide.with(App.getAppContext())
                .load(url)
                .dontAnimate()
                .into(photoView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
