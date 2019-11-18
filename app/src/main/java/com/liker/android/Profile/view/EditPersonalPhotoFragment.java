package com.liker.android.Profile.view;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liker.android.Profile.adapter.PersonalPhotoAdapter;
import com.liker.android.Profile.model.PersonalPhoto;
import com.liker.android.Profile.service.AddPhotoClickListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.FileUtils;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditPersonalPhotoFragment extends DialogFragment {

    View view;
    private ImageView close;
    private RecyclerView recyclerView;
    private FloatingActionButton save;

    private ProfileService profileService;
    private PrefManager manager;
    private PersonalPhotoAdapter personalPhotoAdapter;
    private ArrayList<PersonalPhoto> arrayList;
    private ArrayList<String> addedPhotos;
    private String deviceId, profileUserId, token;

    private static final int REQUEST_TAKE_GALLARY = 101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_personal_photo_layout, container, false);

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
        arrayList = new ArrayList<>();
        addedPhotos = new ArrayList<>();
        if (getArguments() != null) {
            arrayList.addAll(Objects.requireNonNull(getArguments().getParcelableArrayList("media_files")));
        }
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileUserId = manager.getProfileId();
        token = manager.getToken();

        AddPhotoClickListener addPhotoClickListener = new AddPhotoClickListener() {
            @Override
            public void onClickListener() {
                sendImageFromGallery();
            }
        };

        personalPhotoAdapter = new PersonalPhotoAdapter(getActivity(), arrayList, addPhotoClickListener);

        close = view.findViewById(R.id.close);
        save = view.findViewById(R.id.save);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(personalPhotoAdapter);
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
                    String imageFilePath = FileUtils.getPath(getContext(), uri);
//                    addPhoto(imageFilePath);
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                String imageFilePath = FileUtils.getPath(getContext(), uri);
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
        addPhotoRequest(mediaCall);
    }

    private void addPhotoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            addedPhotos.add(object.getString("filename"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
