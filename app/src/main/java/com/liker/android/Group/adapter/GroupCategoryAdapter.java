package com.liker.android.Group.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.model.SuggestedCategory;
import com.liker.android.Group.view.GroupListCategoryWiseActivity;
import com.liker.android.Group.view.GroupPageActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.squareup.picasso.Picasso;


import java.util.List;

public class GroupCategoryAdapter extends RecyclerView.Adapter<GroupCategoryAdapter.MyViewHolder> {

    Context context;
    List<SuggestedCategory> suggestedCategoryList;
    private int SHOW_INTERSTITIAL_AD = 5080;
    private String writingsCatageory;
    private int width, height, itemWidth;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewIcon;

        public MyViewHolder(View view) {
            super(view);
            imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
            textViewName = (TextView) view.findViewById(R.id.textViewName);

            //Setting Item width to 80% of device width
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(itemWidth,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
        }
    }

    public GroupCategoryAdapter(Context context, List<SuggestedCategory> suggestedCategoryList) {
        this.context = context;
        this.suggestedCategoryList = suggestedCategoryList;

        //Calculate the width and height of device
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.width = size.x;
        this.height = size.y;

        //80% of screen width
        itemWidth = (width * 80 / 100);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SuggestedCategory category = suggestedCategoryList.get(position);
        holder.textViewName.setText(category.name);
        holder.imageViewIcon.setImageBitmap(null);
        String coverImage= AppConstants.USER_UPLOADED_IMAGES+category.getImageName();
        Picasso.with(holder.imageViewIcon.getContext()).load(coverImage).into(holder.imageViewIcon);
        holder.itemView.setTag(category);
//        Glide.with(context)
//                .load(app.imageName)
//                .into(holder.imageViewIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryWiseGroupIntent=new Intent(context, GroupListCategoryWiseActivity.class);
                categoryWiseGroupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                categoryWiseGroupIntent.putExtra("category_id",category.catId);
                categoryWiseGroupIntent.putExtra("category_name",category.name);
                categoryWiseGroupIntent.putExtra("category_image",category.imageName);
                context.startActivity(categoryWiseGroupIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestedCategoryList.size();
    }

}

