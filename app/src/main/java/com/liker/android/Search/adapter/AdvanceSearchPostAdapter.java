package com.liker.android.Search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.doodle.App;
//import com.doodle.R;
//import com.doodle.Search.model.Post;
//import com.doodle.Search.service.PostClickListener;
//import com.doodle.Tool.Operation;
import com.liker.android.App;
import com.liker.android.R;
import com.liker.android.Search.model.Post;
import com.liker.android.Search.service.PostClickListener;
import com.liker.android.Tool.Operation;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.liker.android.Tool.AppConstants.Link_IMAGE_PATH;
import static com.liker.android.Tool.AppConstants.POST_IMAGES;
import static com.liker.android.Tool.AppConstants.PROFILE_IMAGE;
import static com.liker.android.Tool.AppConstants.YOUTUBE_IMAGE_PATH;
import static com.liker.android.Tool.AppConstants.YOUTUBE_IMAGE_PATH_NEW;

//import static com.doodle.Tool.AppConstants.POST_IMAGES;
//import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;

public class AdvanceSearchPostAdapter extends RecyclerView.Adapter<AdvanceSearchPostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> mPost;
    private PostClickListener postClickListener;

    public AdvanceSearchPostAdapter(Context context, List<Post> mPost, PostClickListener postClickListener) {
        this.context = context;
        this.mPost = mPost;
        this.postClickListener = postClickListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder viewHolder, int position) {
        viewHolder.populate(mPost.get(position));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostUserName, tvPostDuration, tvPostLike, tvPostStar, postCategory, tvPostText;
        ImageView imgPostUser, imgPostPermission, imgPostImage;

        public PostViewHolder(View itemView) {
            super(itemView);

            tvPostUserName = itemView.findViewById(R.id.tvPostUserName);
            tvPostDuration = itemView.findViewById(R.id.tvPostDuration);
            tvPostLike = itemView.findViewById(R.id.tvPostLike);
            tvPostStar = itemView.findViewById(R.id.tvPostStar);
            postCategory = itemView.findViewById(R.id.postCategory);
            tvPostText = itemView.findViewById(R.id.tvPostText);

            imgPostUser = itemView.findViewById(R.id.imgPostUser);
            imgPostPermission = itemView.findViewById(R.id.imgPostPermission);
            imgPostImage = itemView.findViewById(R.id.imgPostImage);
        }

        public void populate(Post post) {

            //   String postDate = Operation.getDurationBreakdown(Long.parseLong(post.getPostDate()));
            //  String postDate = Operation.getDate(Long.parseLong(post.getPostDate()), "dd/MM/yyyy hh:mm:ss.SSS");
            long myMillis = Long.parseLong(post.getPostDate()) * 1000;
            String postDate = Operation.postDateCompare(context, myMillis);
            int likes, totalStar;
            try {
                likes = Integer.parseInt(post.getTotalLikes());
                totalStar = Integer.parseInt(post.getGoldStars()) + Integer.parseInt(post.getSliverStars());
            } catch (NumberFormatException e) {
                likes = 0;
                totalStar = 0;
            }
            tvPostUserName.setText(post.getFirstName() + " " + post.getLastName());
            tvPostDuration.setText(postDate);
            tvPostLike.setText(likes > 1 ? likes + " " + context.getString(R.string.likes) : likes + " " + context.getString(R.string.like));
            tvPostStar.setText(totalStar > 1 ? totalStar + " " + context.getString(R.string.stars) : totalStar + " " + context.getString(R.string.star));
            postCategory.setText(post.getCategoryName());
            tvPostText.setText(post.getPostText());
            String imagePhoto = PROFILE_IMAGE + post.getPhoto();
            String imagePost;
            if (post.getPostType().equals("3")) {
                imagePost = Link_IMAGE_PATH + post.getPostImage();
            } else if (post.getPostType().equals("4")) {
                imagePost = YOUTUBE_IMAGE_PATH_NEW + post.getPostImage();
            } else {
                imagePost = POST_IMAGES + post.getPostImage();
            }
            Glide.with(App.getAppContext())
                    .load(imagePhoto)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgPostUser);

            Glide.with(App.getAppContext())
                    .load(imagePost)
                    .placeholder(R.drawable.stack)
                    .error(R.drawable.stack)
                    .centerCrop()
                    .into(imgPostImage);

            if (post.getPostType().equals("1") || post.getPostType().equals("6") || post.getPostImage() == null) {
                imgPostImage.setVisibility(View.GONE);
            } else {
                imgPostImage.setVisibility(View.VISIBLE);
            }

            int postPermission = Integer.parseInt(post.getPostPermission());
            switch (postPermission) {
                case 0:
                    imgPostPermission.setImageResource(R.drawable.ic_public_black_24dp);
                    break;
                case 1:
                    imgPostPermission.setImageResource(R.drawable.ic_only_me_12dp);
                    break;
                case 2:
                    imgPostPermission.setImageResource(R.drawable.ic_friends_12dp);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post.getPostType().equals("2") || post.getPostType().equals("3") || post.getPostType().equals("4")) {
                        postClickListener.onPostClickListener(post);
                    }
                }
            });
        }
    }

}
