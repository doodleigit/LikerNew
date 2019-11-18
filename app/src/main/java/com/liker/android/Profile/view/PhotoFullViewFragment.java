package com.liker.android.Profile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liker.android.Profile.adapter.PhotoSliderAdapter;
import com.liker.android.R;

import java.util.ArrayList;
import java.util.Objects;

public class PhotoFullViewFragment extends DialogFragment {

    View view;
    private ImageView close, previous, next;
    private ViewPager viewPager;
    private PhotoSliderAdapter photoSliderAdapter;

    private ArrayList<String> photos;
    private int playPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_media_full_view_fragment_layout, container, false);

        initialComponent();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initialComponent() {
        photos = new ArrayList<>();
        photos = Objects.requireNonNull(getArguments()).getStringArrayList("media_files");
        playPosition = getArguments().getInt("position", -1);

        close = view.findViewById(R.id.close);
        previous = view.findViewById(R.id.previous);
        next = view.findViewById(R.id.next);
        viewPager = view.findViewById(R.id.viewpager);

        photoSliderAdapter = new PhotoSliderAdapter(getActivity(), photos);
        viewPager.setAdapter(photoSliderAdapter);

        if (photos.size() > 1) {
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(playPosition == -1 ? 0 : playPosition);
            }
        }, 500);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 0) {
                    previous.setVisibility(View.GONE);
                } else {
                    previous.setVisibility(View.VISIBLE);
                }
                if (viewPager.getCurrentItem() >= photos.size() - 1) {
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

}
