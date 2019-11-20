package com.liker.android.Profile.view;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.liker.android.Profile.adapter.EditPersonalPhotoAdapter;
import com.liker.android.Profile.adapter.PersonalPhotoAdapter;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.service.AddPhotoClickListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.FileUtils;
import com.liker.android.Tool.PrefManager;

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

    private ProgressDialog progressDialog;

    private ProfileService profileService;
    private PrefManager manager;
    private EditPersonalPhotoAdapter personalPhotoAdapter;
    private ArrayList<PersonalPhoto> arrayList;
    private ArrayList<String> addedPhotos;
    private String deviceId, profileUserId, token;

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
        progressDialog.setMessage(getString(R.string.saving));
        arrayList = new ArrayList<>();
        addedPhotos = new ArrayList<>();
        arrayList.addAll(Objects.requireNonNull(getIntent().getParcelableArrayListExtra("media_files")));
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getApplicationContext());
        deviceId = manager.getDeviceId();
        profileUserId = manager.getProfileId();
        token = manager.getToken();

        AddPhotoClickListener addPhotoClickListener = new AddPhotoClickListener() {
            @Override
            public void onClickListener() {
                sendImageFromGallery();
            }

            @Override
            public void onDeleteClick(String id) {
                Call<String> call = profileService.deleteFeaturedPhoto(deviceId, token, profileUserId, profileUserId, id);
                deleteFeaturedPhotoRequest(call);
            }
        };

        personalPhotoAdapter = new EditPersonalPhotoAdapter(this, arrayList, addPhotoClickListener);

        close = findViewById(R.id.close);
        save = findViewById(R.id.save);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(personalPhotoAdapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<AddFeatured> addFeatured = new ArrayList<>();
//                for (String name : addedPhotos) {
//                    addFeatured.add(new AddFeatured("", false, profileUserId, name));
//                }
                HashMap<String, String> map = new HashMap<>();

                for (int i = 0; i < addedPhotos.size(); i++) {
                    map.put("insert[" + i + "][feature_order]", "");
                    map.put("insert[" + i + "][is_featured]", "false");
                    map.put("insert[" + i + "][user_id]", profileUserId);
                    map.put("insert[" + i + "][filename]", addedPhotos.get(i));
                }

                Call<String> call = profileService.addFeaturedPhoto(deviceId, token, profileUserId, profileUserId, map);
                addFeaturedPhotoRequest(call);
            }
        });
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
        //Parsing any Media type file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        Call<String> mediaCall = profileService.addPhoto(deviceId, profileUserId, token, fileToUpload);
        addPhotoRequest(mediaCall, imageFilePath);
    }

    private void addPhotoRequest(Call<String> call, String id) {
        PersonalPhoto personalPhoto = new PersonalPhoto(id, "");
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
                            personalPhoto.setUploading(false);
                            save.setVisibility(View.VISIBLE);
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
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
