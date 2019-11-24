package com.liker.android.Profile.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liker.android.Profile.adapter.EditPersonalPhotoAdapter;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.service.AddPhotoClickListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.FileUtils;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.SwipeAndDragHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalPhotoActivity extends AppCompatActivity {

    private ImageView close;
    private RecyclerView recyclerView;
    private FloatingActionButton save;
    private ProgressBar progressBar;

    private ProgressDialog progressDialog;

    private ProfileService profileService;
    private PrefManager manager;
    private EditPersonalPhotoAdapter personalPhotoAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private ArrayList<PersonalPhoto> personalPhotos;
    private ArrayList<String> addedPhotos;
    private String deviceId, profileUserId, token;
    int limit = 25;
    int offset = 0;
    private boolean isScrolling;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;

    private static final int REQUEST_TAKE_GALLARY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_photo);

        initialComponent();
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        personalPhotos = new ArrayList<>();
        addedPhotos = new ArrayList<>();
//        personalPhotos.addAll(Objects.requireNonNull(getIntent().getParcelableArrayListExtra("media_files")));
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getApplicationContext());
        deviceId = manager.getDeviceId();
        profileUserId = manager.getProfileId();
        token = manager.getToken();

        AddPhotoClickListener addPhotoClickListener = new AddPhotoClickListener() {
            @Override
            public void onClickListener() {
                checkGalleryPermission();
            }

            @Override
            public void onDeleteClick(String id) {
                Call<String> call = profileService.deleteFeaturedPhoto(deviceId, token, profileUserId, profileUserId, id);
                deleteFeaturedPhotoRequest(call);
            }
        };

        personalPhotoAdapter = new EditPersonalPhotoAdapter(this, personalPhotos, addPhotoClickListener);

        close = findViewById(R.id.close);
        save = findViewById(R.id.save);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personalPhotoAdapter);

        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(personalPhotoAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        personalPhotoAdapter.setTouchHelper(touchHelper);
        recyclerView.setAdapter(personalPhotoAdapter);
        touchHelper.attachToRecyclerView(recyclerView);

        getPhotos();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if (isScrolling && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    isScrolling = false;
                    getPagination();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                for (int i = 0; i < addedPhotos.size(); i++) {
                    map.put("insert[" + i + "][feature_order]", "");
                    map.put("insert[" + i + "][is_featured]", "false");
                    map.put("insert[" + i + "][user_id]", profileUserId);
                    map.put("insert[" + i + "][filename]", addedPhotos.get(i));
                }
                for (int i = 0; i < personalPhotos.size(); i++) {
                    if (personalPhotos.get(i).isFeatured()) {
                        map.put("update[" + i + "][id]", personalPhotos.get(i).getId());
                        map.put("update[" + i + "][image_name]", personalPhotos.get(i).getImageName());
                    }
                }

                Call<String> call = profileService.addFeaturedPhoto(deviceId, token, profileUserId, profileUserId, map);
                addFeaturedPhotoRequest(call);
            }
        });
    }

    private void getPhotos() {
        Call<String> callPersonalPhotos = profileService.getFeaturedPhotos(deviceId, token, profileUserId, profileUserId, profileUserId, limit, offset);
        sendPersonalPhotoRequest(callPersonalPhotos);
    }

    private void getPagination() {
        Call<String> callPersonalPhotos = profileService.getFeaturedPhotos(deviceId, token, profileUserId, profileUserId, profileUserId, limit, offset);
        sendPersonalPhotoPaginationRequest(callPersonalPhotos);
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLARY);
        } else {
            sendImageFromGallery();
        }
    }

    public void sendImageFromGallery() {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1, "Select images"), REQUEST_TAKE_GALLARY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLARY && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    String imageFilePath = FileUtils.getPath(getApplicationContext(), uri);
                    addPhoto(imageFilePath);
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                String imageFilePath = FileUtils.getPath(getApplicationContext(), uri);
                addPhoto(imageFilePath);
            }
        }
    }

    private void addPhoto(String imageFilePath) {
        File file = new File(imageFilePath);
        long fileSize = file.length();
        //Parsing any Media type file
        if (fileSize < 8 * 1000000) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            Call<String> mediaCall = profileService.addPhoto(deviceId, profileUserId, token, fileToUpload);
            addPhotoRequest(mediaCall, imageFilePath);
        }
    }

    private void sendPersonalPhotoRequest(Call<String> call) {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        offset += limit;
                        personalPhotos.clear();
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("all_featured");
                        JSONArray array = jsonObject.getJSONObject("data").getJSONArray("is_featured");
                        ArrayList<PersonalPhoto> featuredArray = new ArrayList<>();
                        ArrayList<PersonalPhoto> allFeaturedArray = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String id, imageName;
                            id = object.getString("id");
                            imageName = object.getString("image_name");
                            featuredArray.add(new PersonalPhoto(id, imageName, true));
                            allFeaturedArray.add(new PersonalPhoto(id, imageName, true));
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id, imageName;
                            id = object.getString("id");
                            imageName = object.getString("image_name");
                            boolean hasAlready = false;
                            for (PersonalPhoto personalPhoto : featuredArray) {
                                if (personalPhoto.getId().equals(id)) {
                                    hasAlready = true;
                                }
                            }
                            if (!hasAlready)
                                allFeaturedArray.add(new PersonalPhoto(id, imageName, false));
                        }
                        personalPhotos.addAll(allFeaturedArray);
                        personalPhotoAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void sendPersonalPhotoPaginationRequest(Call<String> call) {
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        offset += limit;
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("all_featured");
                        JSONArray array = jsonObject.getJSONObject("data").getJSONArray("is_featured");
                        ArrayList<PersonalPhoto> featuredArray = new ArrayList<>();
                        ArrayList<PersonalPhoto> allFeaturedArray = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String id, imageName;
                            id = object.getString("id");
                            imageName = object.getString("image_name");
                            featuredArray.add(new PersonalPhoto(id, imageName, true));
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id, imageName;
                            id = object.getString("id");
                            imageName = object.getString("image_name");
                            boolean hasAlready = false;
                            for (PersonalPhoto personalPhoto : featuredArray) {
                                if (personalPhoto.getId().equals(id)) {
                                    hasAlready = true;
                                }
                            }
                            if (!hasAlready)
                                allFeaturedArray.add(new PersonalPhoto(id, imageName, false));
                        }
                        personalPhotos.addAll(allFeaturedArray);
                        personalPhotoAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addPhotoRequest(Call<String> call, String id) {
        PersonalPhoto personalPhoto = new PersonalPhoto(id, "", false);
        personalPhoto.setUploading(true);
        personalPhotoAdapter.addUploadingAnimation(personalPhoto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean success = false;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            addedPhotos.add(object.getString("filename"));
                            personalPhoto.setImageName(object.getString("filename"));
                            personalPhoto.setUploading(false);
                            personalPhotoAdapter.stopUploadingAnimation(id, personalPhoto);
                            success = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!success) {
                    personalPhotoAdapter.removeUpload(id);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                personalPhotoAdapter.removeUpload(id);
            }
        });
    }

    private void addFeaturedPhotoRequest(Call<String> call) {
        progressDialog.setMessage(getString(R.string.saving));
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                sendBroadcast(new Intent().setAction(AppConstants.PERSONAL_PHOTO_BROADCAST));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void deleteFeaturedPhotoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
