package com.liker.android.Profile.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.service.AddPhotoClickListener;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.SwipeAndDragHelper;

import java.util.ArrayList;
import java.util.Collections;

public class EditPersonalPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragHelper.ActionCompletionContract {

    private Context context;
    private ArrayList<PersonalPhoto> arrayList;
    private AddPhotoClickListener addPhotoClickListener;
    private ItemTouchHelper touchHelper;
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
            String url = AppConstants.PROFILE_IMAGE + arrayList.get(i).getImageName();
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
                holder.featured.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.featured.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                if (arrayList.get(i).isFeatured()) {
                    holder.close.setVisibility(View.GONE);
                    holder.featured.setImageResource(R.drawable.ic_featured_24dp);
                    holder.mainLayout.setBackgroundResource(R.drawable.rectangle_one);
                } else {
                    holder.close.setVisibility(View.VISIBLE);
                    holder.featured.setImageResource(R.drawable.ic_unfeatured_24dp);
                    holder.mainLayout.setBackgroundResource(R.color.colorWhite);
                }
            }

            holder.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (arrayList.get(i).isFeatured())
                            touchHelper.startDrag(holder);
                    }
                    return false;
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.featured.getVisibility() == View.VISIBLE) {
                        if (arrayList.get(i).isFeatured()) {
                            arrayList.get(i).setFeatured(!arrayList.get(i).isFeatured());
                            notifyItemChanged(i);
                        } else {
                            if (getFeaturedCount() < 9) {
                                arrayList.get(i).setFeatured(!arrayList.get(i).isFeatured());
                                notifyItemChanged(i);
                            }
//                            else {
//                                Toast.makeText(context, "You have already reached maximum number of featured image", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
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

    private int getFeaturedCount() {
        int count = 0;
        for (PersonalPhoto personalPhoto : arrayList) {
            if (personalPhoto.isFeatured()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public int getItemCount() {
        if (arrayList.size() == 0)
            return 0;
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

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        if (oldPosition < arrayList.size() && newPosition < arrayList.size()) {
            Collections.swap(arrayList, oldPosition, newPosition);
            notifyItemMoved(oldPosition, newPosition);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 500);
        }


//        LectureModel targetUser = allLecture.get(oldPosition);
//        allLecture.remove(oldPosition);
//        allLecture.add(newPosition, targetUser);
//        notifyItemMoved(oldPosition, newPosition);
//        String lectureOrder = "";
//        for (int i = 0; i < allLecture.size(); i++) {
//            lectureOrder = lectureOrder.concat(allLecture.get(i).getId()).concat(",");
//        }
    }

    @Override
    public void onViewSwiped(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        ImageView imageView, featured, close;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.main_layout);
            imageView = itemView.findViewById(R.id.image);
            featured = itemView.findViewById(R.id.featured);
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

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

}