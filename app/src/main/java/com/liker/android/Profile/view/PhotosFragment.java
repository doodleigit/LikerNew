package com.liker.android.Profile.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.doodle.App;
//import com.doodle.Home.model.PostItem;
//import com.doodle.Post.view.activity.PostPopup;
//import com.doodle.Profile.adapter.AlbumAdapter;
//import com.doodle.Profile.adapter.AlbumPhotoAdapter;
//import com.doodle.Profile.adapter.PhotoAdapter;
//import com.doodle.Profile.model.AlbumPhoto;
//import com.doodle.Profile.model.PhotoAlbum;
//import com.doodle.Profile.model.RecentPhoto;
//import com.doodle.Profile.service.PhotoAlbumClickListener;
//import com.doodle.Profile.service.PhotoClickListener;
//import com.doodle.Profile.service.ProfileService;
//import com.doodle.R;
//import com.doodle.Tool.PrefManager;

import com.liker.android.Home.model.PostItem;
import com.liker.android.Post.view.activity.PostPopup;
import com.liker.android.Profile.adapter.AlbumAdapter;
import com.liker.android.Profile.adapter.AlbumPhotoAdapter;
import com.liker.android.Profile.adapter.PersonalPhotoAdapter;
import com.liker.android.Profile.adapter.PhotoAdapter;
import com.liker.android.Profile.model.AlbumPhoto;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.model.PhotoAlbum;
import com.liker.android.Profile.model.RecentPhoto;
import com.liker.android.Profile.service.PhotoAlbumClickListener;
import com.liker.android.Profile.service.PhotoClickListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Tool.AppConstants.ITEM_KEY;

//import static com.doodle.Tool.AppConstants.ITEM_KEY;

public class PhotosFragment extends Fragment {

    View view;
    private RecyclerView albumRecyclerView, personalPhotoRecyclerView, photoRecyclerView;

    private ProgressDialog progressDialog;

    private PhotoAlbumClickListener photoAlbumClickListener;
    private PhotoClickListener photoClickListener;
    private ProfileService profileService;
    private PrefManager manager;
    private AlbumAdapter albumAdapter;
    private PersonalPhotoAdapter personalPhotoAdapter;
    private PhotoAdapter photoAdapter;
    private ArrayList<PhotoAlbum> photoAlbums;
    private ArrayList<PersonalPhoto> personalPhotos;
    private ArrayList<RecentPhoto> recentPhotos;
    private String deviceId, profileUserId, token, userId;
    int limit = 10;
    int offset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photos_fragment_layout, container, false);

        initialComponent();
        getData();

        return view;
    }

    private void initialComponent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.PERSONAL_PHOTO_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        photoAlbums = new ArrayList<>();
        personalPhotos = new ArrayList<>();
        recentPhotos = new ArrayList<>();
        deviceId = manager.getDeviceId();
        assert getArguments() != null;
        profileUserId = getArguments().getString("user_id");
        token = manager.getToken();
        userId = manager.getProfileId();

        photoAlbumClickListener = new PhotoAlbumClickListener() {
            @Override
            public void onAlbumClick(PhotoAlbum photoAlbum) {
                showAlbumPhotos(photoAlbum);
            }
        };

        photoClickListener = new PhotoClickListener() {
            @Override
            public void onPhotoClick(RecentPhoto photo) {
                getPostDetails(photo);
            }
        };

        albumAdapter = new AlbumAdapter(getActivity(), photoAlbums, photoAlbumClickListener);
        personalPhotoAdapter = new PersonalPhotoAdapter(getActivity(), personalPhotos, null);
        photoAdapter = new PhotoAdapter(getActivity(), recentPhotos);

        albumRecyclerView = view.findViewById(R.id.albumRecyclerView);
        personalPhotoRecyclerView = view.findViewById(R.id.personalPhotoRecyclerView);
        photoRecyclerView = view.findViewById(R.id.photoRecyclerView);
        albumRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        albumRecyclerView.setNestedScrollingEnabled(false);
        personalPhotoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        personalPhotoRecyclerView.setNestedScrollingEnabled(false);
        photoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photoRecyclerView.setNestedScrollingEnabled(false);

        albumRecyclerView.setAdapter(albumAdapter);
        personalPhotoRecyclerView.setAdapter(personalPhotoAdapter);
        photoRecyclerView.setAdapter(photoAdapter);
    }

    private void getData() {
        Call<ArrayList<PhotoAlbum>> callAlbums = profileService.getAlbums(deviceId, token, userId, profileUserId, userId);
        sendAlbumListRequest(callAlbums);
        Call<ArrayList<RecentPhoto>> callPhotos = profileService.getRecentPhotos(deviceId, token, userId, profileUserId, userId, limit, offset);
        sendRecentListRequest(callPhotos);
        Call<String> callPersonalPhotos = profileService.getFeaturedPhotos(deviceId, token, userId, profileUserId, userId, limit, offset);
        sendPersonalPhotoRequest(callPersonalPhotos);
    }

    private void showAlbumPhotos(PhotoAlbum photoAlbum) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.album_photo_list_layout);

        ArrayList<AlbumPhoto> albumPhotos = new ArrayList<>();
        AlbumPhotoAdapter albumPhotoAdapter = new AlbumPhotoAdapter(getActivity(), albumPhotos);
        int limit = 0, offset = 0;

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView tvTitle = dialog.findViewById(R.id.title);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(albumPhotoAdapter);

        tvTitle.setText(photoAlbum.getTitle());

        if (photoAlbum.getAlbumType() == -1) {
            for (RecentPhoto recentPhoto : recentPhotos) {
                albumPhotos.add(new AlbumPhoto(recentPhoto.getImageName()));
            }
            albumPhotoAdapter.notifyDataSetChanged();
        } else {
            Call<ArrayList<AlbumPhoto>> callPhotos = profileService.getAlbumPhotos(deviceId, token, userId, profileUserId, String.valueOf(photoAlbum.getAlbumType()), userId, limit, offset);
            sendAlbumPhotoListRequest(callPhotos, albumPhotos, albumPhotoAdapter);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSingleMedia(PhotoAlbum photoAlbum) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.popup_single_media_layout);

        ViewPager viewpager = dialog.findViewById(R.id.viewpager);
        ImageView close, leftSlide, rightSlide;
        close = dialog.findViewById(R.id.close);
        leftSlide = dialog.findViewById(R.id.left_slide);
        rightSlide = dialog.findViewById(R.id.right_slide);

        dialog.show();
    }

    private void getPostDetails(RecentPhoto recentPhoto) {
        progressDialog.show();
        Call<PostItem> call = profileService.getPostDetails(deviceId, userId, token, userId, recentPhoto.getPostId());
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent intent = new Intent(getContext(), PostPopup.class);
                    intent.putExtra(ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra("has_footer", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendAlbumListRequest(Call<ArrayList<PhotoAlbum>> call) {

        call.enqueue(new Callback<ArrayList<PhotoAlbum>>() {

            @Override
            public void onResponse(Call<ArrayList<PhotoAlbum>> call, Response<ArrayList<PhotoAlbum>> response) {

                ArrayList<PhotoAlbum> arrayList = response.body();
                if (arrayList != null) {
                    photoAlbums.addAll(arrayList);
                    albumAdapter.notifyDataSetChanged();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<PhotoAlbum>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendPersonalPhotoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        personalPhotos.clear();
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("all_featured");
                        ArrayList<PersonalPhoto> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id, imageName;
                            id = object.getString("id");
                            imageName = object.getString("image_name");
                            arrayList.add(new PersonalPhoto(id, imageName));
                        }
                        personalPhotos.addAll(arrayList);
                        personalPhotoAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }

    private void sendRecentListRequest(Call<ArrayList<RecentPhoto>> call) {

        call.enqueue(new Callback<ArrayList<RecentPhoto>>() {

            @Override
            public void onResponse(Call<ArrayList<RecentPhoto>> call, Response<ArrayList<RecentPhoto>> response) {

                ArrayList<RecentPhoto> arrayList = response.body();
                if (arrayList != null && arrayList.size() > 0) {
                    recentPhotos.addAll(arrayList);
//                    photoAlbums.add(new PhotoAlbum(-1, getString(R.string.photo_gallery), String.valueOf(arrayList.size()), arrayList.get(0).getImageName()));
//                    albumAdapter.notifyDataSetChanged();
                    photoAdapter.notifyDataSetChanged();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<RecentPhoto>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendAlbumPhotoListRequest(Call<ArrayList<AlbumPhoto>> call, ArrayList<AlbumPhoto> albumPhotos, AlbumPhotoAdapter albumPhotoAdapter) {

        call.enqueue(new Callback<ArrayList<AlbumPhoto>>() {

            @Override
            public void onResponse(Call<ArrayList<AlbumPhoto>> call, Response<ArrayList<AlbumPhoto>> response) {

                ArrayList<AlbumPhoto> arrayList = response.body();
                if (arrayList != null) {
                    albumPhotos.addAll(arrayList);
                    albumPhotoAdapter.notifyDataSetChanged();
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumPhoto>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
//                progressDialog.hide();
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Call<String> callPersonalPhotos = profileService.getFeaturedPhotos(deviceId, token, userId, profileUserId, userId, limit, offset);
            sendPersonalPhotoRequest(callPersonalPhotos);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
    }
}
