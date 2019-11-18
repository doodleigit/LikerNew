package com.liker.android.Profile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.liker.android.App;
import com.liker.android.Home.view.fragment.DownLoadPermissionSheet;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import java.util.List;

public class PhotoSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> arrayList;
    private boolean isVideoUrl;

    public PhotoSliderAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    private AppCompatActivity activity;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.media_slider_item, view, false);

        final PhotoView imageView = imageLayout.findViewById(R.id.media_image);

        String imageUrl = AppConstants.POST_IMAGES + arrayList.get(position);

        Glide.with(App.getAppContext())
                .load(imageUrl)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .dontAnimate()
                .into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                isVideoUrl = false;
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

}
