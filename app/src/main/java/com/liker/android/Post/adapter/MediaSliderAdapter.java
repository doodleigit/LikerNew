package com.liker.android.Post.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
//import com.doodle.App;
//import com.doodle.Home.model.PostFile;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.liker.android.App;
import com.liker.android.Home.model.PostFile;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.view.fragment.DownLoadPermissionSheet;
import com.liker.android.Home.view.fragment.PostPermissionSheet;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MediaSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PostFile> arrayList;
    private ArrayList<PlayerView> videoViews;
    private RelativeLayout mediaLayer;
    private boolean isVideoUrl;

    public MediaSliderAdapter(Context context, List<PostFile> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        videoViews = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            videoViews.add(null);
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    PostItem postItem=new PostItem();
    @Override
    public int getCount() {
        return arrayList.size();
    }
    private AppCompatActivity activity;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.media_slider_item, view, false);
        mediaLayer=imageLayout.findViewById(R.id.mediaLayer);

        assert imageLayout != null;
        final PhotoView imageView = imageLayout.findViewById(R.id.media_image);
        final PlayerView videoView = imageLayout.findViewById(R.id.video_view);
        videoViews.set(position, videoView);

        String imageUrl=AppConstants.POST_IMAGES + arrayList.get(position).getImageName();
        String videoUrl=AppConstants.POST_VIDEOS + arrayList.get(position).getVideoName();
        if (arrayList.get(position).getPostType().equals("2")) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            // Setup Exoplayer instance
            SimpleExoPlayer exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(context);

// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "Liker"));
            //Getting media from raw resource
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(AppConstants.POST_VIDEOS + arrayList.get(position).getVideoName()));
//Prepare the exoPlayerInstance with the source
            exoPlayerInstance.prepare(videoSource);
            videoView.setPlayer(exoPlayerInstance);
        } else {
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(App.getAppContext())
                    .load(AppConstants.POST_IMAGES + arrayList.get(position).getImageName())
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .dontAnimate()
                    .into(imageView);
        }

      //  videoView.setControllerAutoShow(false);
//        videoView.setUseController(false);
        videoView.getVideoSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("GET", "clicked!");
                activity = (AppCompatActivity) v.getContext();
                isVideoUrl=true;
                DownLoadPermissionSheet reportReasonSheet = DownLoadPermissionSheet.newInstance(videoUrl,isVideoUrl);
                reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");
                return false;
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                isVideoUrl=false;
                DownLoadPermissionSheet reportReasonSheet = DownLoadPermissionSheet.newInstance(imageUrl, isVideoUrl);
                reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");
                return false;
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void pauseVideo(int position) {
        for (int i = 0; i < videoViews.size(); i++) {
            if (videoViews.get(i) != null) {
                if (videoViews.get(i).getPlayer() != null) {
                    if (position != i) {
                        videoViews.get(i).getPlayer().setPlayWhenReady(false);
                    }
                }
            }
        }
    }

}
