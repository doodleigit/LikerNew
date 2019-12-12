package com.liker.android.Search.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.doodle.App;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Search.model.User;
import com.liker.android.App;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Search.model.User;
import com.liker.android.Search.service.FollowUnfollowClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.liker.android.Tool.AppConstants.POST_IMAGES;

//import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class AdvanceSearchAdapter extends RecyclerView.Adapter<AdvanceSearchAdapter.UserViewHolder> {

    private Context context;
    private List<User> mUser;
    private FollowUnfollowClickListener followUnfollowClickListener;

    public AdvanceSearchAdapter(Context context, List<User> mUser, FollowUnfollowClickListener followUnfollowClickListener) {
        this.context = context;
        this.mUser = mUser;
        this.followUnfollowClickListener = followUnfollowClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder viewHolder, int position) {
        viewHolder.populate(mUser.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLike, tvStar;
        ImageView imgUser;
        Button btnFollow;

        public UserViewHolder(View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvStar = itemView.findViewById(R.id.tvStar);
            imgUser = itemView.findViewById(R.id.imgUser);
            btnFollow = itemView.findViewById(R.id.follow);
        }

        public void populate(User user, int i) {
            int likes, totalStar;
            try {
                likes = Integer.parseInt(user.getTotalLikes());
                totalStar = Integer.parseInt(user.getGoldStars()) + Integer.parseInt(user.getSliverStars());
            } catch (NumberFormatException e) {
                likes = 0;
                totalStar = 0;
            }

            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            tvLike.setText(likes > 1 ? likes + " " + context.getString(R.string.likes) : likes + " " + context.getString(R.string.like));
            tvStar.setText(totalStar > 1 ? totalStar + " " + context.getString(R.string.stars) : totalStar + " " + context.getString(R.string.star));
            String imagePhoto = POST_IMAGES + user.getPhoto();

            if (user.isFollowed()) {
                btnFollow.setText(context.getString(R.string.unfollow));
            } else {
                btnFollow.setText(context.getString(R.string.follow));
            }

            if (imagePhoto.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(imagePhoto)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(imgUser);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", user.getUserId()).putExtra("user_name", user.getUserName()));
                }
            });

            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.isFollowed()) {
                        followUnfollowClickListener.onUnFollowClick(user.getUserId(), i);
                    } else {
                        followUnfollowClickListener.onFollowClick(user.getUserId(), i);
                    }
                }
            });

        }
    }

}
