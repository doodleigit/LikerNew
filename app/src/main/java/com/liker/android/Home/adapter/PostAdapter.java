package com.liker.android.Home.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

//import com.doodle.Home.holder.HowLikerWorksHolder;
//import com.doodle.Home.model.PostItem;
//import com.doodle.Home.holder.ImageHolder;
//import com.doodle.Home.holder.LinkScriptHolder;
//import com.doodle.Home.holder.LinkScriptYoutubeHolder;
//import com.doodle.Home.holder.TextHolder;
//import com.doodle.Home.holder.TextMimHolder;
//import com.doodle.Home.holder.VideoHolder;
//import com.doodle.Post.model.Mim;
//import com.doodle.Post.service.DataProvider;
//import com.doodle.R;

import com.liker.android.App;
import com.liker.android.Home.holder.HowLikerWorksHolder;
import com.liker.android.Home.holder.ImageHolder;
import com.liker.android.Home.holder.LinkScriptHolder;
import com.liker.android.Home.holder.LinkScriptYoutubeHolder;
import com.liker.android.Home.holder.TextHolder;
import com.liker.android.Home.holder.TextMimHolder;
import com.liker.android.Home.holder.VideoHolder;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.service.TabClickListener;
import com.liker.android.Post.model.Mim;
import com.liker.android.Post.service.DataProvider;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.List;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_LIKER_USE = 0;
    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;
//    final int VIEW_TYPE_VIDEO = 5;
    final int VIEW_TYPE_TEX_MIM = 6;

    private List<PostItem> postItems;
    List<Mim> viewColors = new DataProvider().getMimList();
    private Context mContext;
    Drawable mDrawable;
    public TextHolder.PostItemListener listener;
    public TextMimHolder.PostItemListener mimListener;
    public VideoHolder.PostItemListener videoListener;
    public LinkScriptYoutubeHolder.PostItemListener YoutubeListener;
    public LinkScriptHolder.PostItemListener LinkListener;
    public ImageHolder.PostItemListener imageListener;
    private String className;
    private String mOrientation="PORTRAIT";

    public PostAdapter(Context context, List<PostItem> postItems,
                       TextHolder.PostItemListener listener,
                       TextMimHolder.PostItemListener mimListener,
                       VideoHolder.PostItemListener videoListener,
                       LinkScriptYoutubeHolder.PostItemListener YoutubeListener,
                       LinkScriptHolder.PostItemListener LinkListener,
                       ImageHolder.PostItemListener imageListener,
                       String className
    ) {
        this.mContext = context;
        this.postItems = postItems;
        this.listener = listener;
        this.mimListener = mimListener;
        this.videoListener = videoListener;
        this.YoutubeListener = YoutubeListener;
        this.LinkListener = LinkListener;
        this.imageListener = imageListener;
        this.className = className;
       // getScreenOrientation(mContext);

//        IntentFilter permissionIntent = new IntentFilter();
//        permissionIntent.addAction(AppConstants.NEW_ORIENTATION_BROADCAST);
//        Objects.requireNonNull(mContext).registerReceiver(permissionBroadcast, permissionIntent);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (viewType == VIEW_TYPE_LIKER_USE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.how_liker_works_layout, parent, false);
            return new HowLikerWorksHolder(view, mContext);
        }
        if (viewType == VIEW_TYPE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_text, parent, false);
            return new TextHolder(view, mContext, listener, className);
        }
        if (viewType == VIEW_TYPE_TEX_MIM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_text_mim, parent, false);
            return new TextMimHolder(view, mContext, mimListener, className);
        }
        if (viewType == VIEW_TYPE_TEXT_IMAGE) {
            if(mOrientation.equalsIgnoreCase("PORTRAIT")) {
               // Toast.makeText(mContext, "PORTRAIT", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_image, parent, false);
                return new ImageHolder(view, mContext, imageListener, className,mOrientation);
            }else if(mOrientation.equalsIgnoreCase("LANDSCAPE")){
             //   Toast.makeText(mContext, "LANDSCAPE", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_image, parent, false);
                return new ImageHolder(view, mContext, imageListener, className,mOrientation);
            }
        }

        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script, parent, false);
            return new LinkScriptHolder(view, mContext, LinkListener, className);

     /*       if(mOrientation.equalsIgnoreCase("PORTRAIT")){
                //Toast.makeText(mContext, "PORTRAIT", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script, parent, false);
                return new LinkScriptHolder(view, mContext, LinkListener, className);
            }else if(mOrientation.equalsIgnoreCase("LANDSCAPE")) {
//                Toast.makeText(mContext, "LANDSCAPE", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script, parent, false);
                return new LinkScriptHolder(view, mContext, LinkListener, className);
            }*/

        }
        if (viewType == VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script_youtube, parent, false);
            return new LinkScriptYoutubeHolder(view, mContext, YoutubeListener, className);

        /*    if(mOrientation.equalsIgnoreCase("PORTRAIT")) {
//                Toast.makeText(mContext, "PORTRAIT", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script_youtube, parent, false);
                return new LinkScriptYoutubeHolder(view, mContext, YoutubeListener, className);
            }else if(mOrientation.equalsIgnoreCase("LANDSCAPE")){
//                Toast.makeText(mContext, "LANDSCAPE", Toast.LENGTH_SHORT).show();
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_link_script_youtube, parent, false);
                return new LinkScriptYoutubeHolder(view, mContext, YoutubeListener, className);
            }*/

        }
//        if (viewType == VIEW_TYPE_VIDEO) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_video, parent, false);
//            return new VideoHolder(view, mContext, videoListener);
//        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HowLikerWorksHolder) {
            HowLikerWorksHolder vh = (HowLikerWorksHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof TextHolder) {
            TextHolder vh = (TextHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof TextMimHolder) {
            TextMimHolder vh = (TextMimHolder) viewHolder;
            vh.setItem(postItems.get(position), position, viewHolder);
        }
        if (viewHolder instanceof LinkScriptHolder) {
            LinkScriptHolder vh = (LinkScriptHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof LinkScriptYoutubeHolder) {
            LinkScriptYoutubeHolder vh = (LinkScriptYoutubeHolder) viewHolder;
            vh.setItem(postItems.get(position), position);
        }
        if (viewHolder instanceof ImageHolder) {
            ImageHolder vh = (ImageHolder) viewHolder;
            vh.setItem(postItems.get(position), position, viewHolder);
        }
//        if (viewHolder instanceof VideoHolder) {
//            VideoHolder vh = (VideoHolder) viewHolder;
//            vh.setItem(postItems.get(position), position);
//        }

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        String postType = postItems.get(position).getPostType();
        String hasMim = postItems.get(position).getHasMeme();
        int mimId = Integer.parseInt(hasMim);
        int viewType = Integer.parseInt(postType);
        switch (viewType) {
            case 0:
                return VIEW_TYPE_LIKER_USE;
            case 1:
                if (mimId > 0) {

                    return VIEW_TYPE_TEX_MIM;
                } else {
                    return VIEW_TYPE_TEXT;
                }
            case 2:
                return VIEW_TYPE_TEXT_IMAGE;
            case 3:
                return VIEW_TYPE_TEXT_LINK_SCRIPT;
            case 4:
                return VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE;
//            case 5:
//                return VIEW_TYPE_VIDEO;
            default:
                return -1;
        }
    }


    public void addPagingData(List<PostItem> postItemList) {

        for (PostItem temp : postItemList
        ) {
            postItems.add(temp);
        }

        notifyDataSetChanged();
    }


    public void deleteItem(int position) {


        // comment_list.remove(position);
        // comment_list.remove(position);

        //   postItems.remove(position);
   //     notifyItemRemoved(position);
     //   notifyDataSetChanged();

        postItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, postItems.size());
      //  holder.itemView.setVisibility(View.GONE);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getScreenOrientation(Context context){
        final int screenOrientation = ((WindowManager)  context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                Toast.makeText(context, "SCREEN_ORIENTATION_PORTRAIT", Toast.LENGTH_SHORT).show();
                return "SCREEN_ORIENTATION_PORTRAIT";
            case Surface.ROTATION_90:
                Toast.makeText(context, "SCREEN_ORIENTATION_LANDSCAPE", Toast.LENGTH_SHORT).show();
                return "SCREEN_ORIENTATION_LANDSCAPE";
            case Surface.ROTATION_180:
                Toast.makeText(context, "SCREEN_ORIENTATION_REVERSE_PORTRAIT", Toast.LENGTH_SHORT).show();
                return "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
            default:
                Toast.makeText(context, "SCREEN_ORIENTATION_REVERSE_LANDSCAPE", Toast.LENGTH_SHORT).show();
                return "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
        }

    }

    public void screenChanged(String orientation){
        if (orientation.equals("LANDSCAPE")){
            //code
            mOrientation="LANDSCAPE";
        }else{
            mOrientation="PORTRAIT";
            //code
           // Toast.makeText(mContext, "LANDSCAPE", Toast.LENGTH_SHORT).show();
        }
    }

    BroadcastReceiver permissionBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //     PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            //  int position = intent.getIntExtra("position", -1);
            mOrientation = intent.getStringExtra("orientation");
            Log.d("broadCastOrientation",mOrientation);
        }
    };

}
