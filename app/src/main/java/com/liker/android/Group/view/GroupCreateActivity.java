package com.liker.android.Group.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.App;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Post.model.Category;
import com.liker.android.Post.model.Subcatg;
import com.liker.android.Post.view.activity.PostCategory;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.ClearableEditText;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Tool.Tools.isNullOrEmpty;

public class GroupCreateActivity extends AppCompatActivity implements View.OnClickListener {


    //data
    private String categoryId = "", subCategoryId = "";
    private String categoryName, audience;
    private String permission;
    private String groupName;

    private ImageView imageCancelCreateGroup;
    private PrefManager manager;
    private GroupWebservice groupWebservice;
    private boolean networkOk;
    private ClearableEditText etGroupName;
    private ViewGroup contentCategory;
    private TextView tvAudience, tvCreateGroup;
    private RadioButton rbPublic, rbPrivate;
    private String TAG = "GroupCreateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        initComponent();

    }

    private void initComponent() {
        imageCancelCreateGroup = findViewById(R.id.imageCancelCreateGroup);
        etGroupName = findViewById(R.id.etGroupName);
        contentCategory = findViewById(R.id.contentCategory);
        rbPublic = findViewById(R.id.groupPublic);
        rbPrivate = findViewById(R.id.groupPrivate);
        rbPublic.setOnClickListener(this);
        rbPrivate.setOnClickListener(this);
        contentCategory.setOnClickListener(this);
        tvAudience = findViewById(R.id.tvAudience);
        tvCreateGroup = findViewById(R.id.tvCreateGroup);
        imageCancelCreateGroup.setOnClickListener(this);
        tvCreateGroup.setOnClickListener(this);
        manager = new PrefManager(this);
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);

        tvAudience.setText(manager.getPostAudience().isEmpty() ? getString(R.string.audience) : manager.getPostAudience());
        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                groupName = s.toString();

                if (!TextUtils.isEmpty(groupName)) {
                    tvCreateGroup.setTextColor(Color.WHITE);
                    tvCreateGroup.setBackgroundResource(R.drawable.btn_round_outline);
                    tvCreateGroup.setEnabled(true);

                } else {
                    tvCreateGroup.setBackgroundResource(R.drawable.btn_create_group_outline_disable);
                    tvCreateGroup.setTextColor(Color.parseColor(getString(R.string.tv_create_group_disable_color)));
                    tvCreateGroup.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.imageCancelCreateGroup:
                finish();
                break;
            case R.id.contentCategory:
                Intent intent = new Intent(GroupCreateActivity.this, PostCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                break;

            case R.id.groupPublic:

                if (rbPublic.isChecked()) {
                    rbPrivate.setChecked(false);
                    rbPrivate.setSelected(false);
                } else {
                    rbPrivate.setChecked(true);
                    rbPrivate.setSelected(true);
                }
                permission = "0";
                break;
            case R.id.groupPrivate:

                if (rbPrivate.isChecked()) {
                    rbPublic.setChecked(false);
                    rbPublic.setSelected(false);
                } else {
                    rbPrivate.setChecked(true);
                    rbPrivate.setSelected(true);
                }
                permission = "1";
                break;

            case R.id.tvCreateGroup:

                String deviceId = manager.getDeviceId();
                String token = manager.getToken();
                String userId = manager.getProfileId();
                if (!isNullOrEmpty(deviceId) && !isNullOrEmpty(token) && !isNullOrEmpty(userId)) {
                    if (!isNullOrEmpty(groupName)) {
                        if (!isNullOrEmpty(categoryId)) {
                            if (!isNullOrEmpty(permission)) {

                                if (networkOk) {

                                    Call<String> call = groupWebservice.createGroup(deviceId, userId, token, userId, categoryId, groupName, permission);
                                    createGroupRequest(call);
                                } else {
                                    Toast.makeText(this, "no internet", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "set privacy!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Select category!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Insert Group name!", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
    }

    private void createGroupRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                JSONObject dataObject = object.getJSONObject("data");
                                if (dataObject.length() > 0) {
                                    String groupId = dataObject.getString("group_id");
                                    AppSingleton.getInstance().setGroupId(groupId);

                                }
                                JSONObject messageObject = object.getJSONObject("message");
                                JSONObject successObject = messageObject.getJSONObject("success");
                                if (successObject.length() > 0) {
                                    String successMessage = successObject.getString("message");
                                    Toast.makeText(GroupCreateActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GroupCreateActivity.this, GroupPageActivity.class));
                                    finish();
                                }
                            } else {
                                String message = object.getString("message");
                                Toast.makeText(GroupCreateActivity.this, message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
//        tvPermission.setText(manager.getPostPermission());
//        String permissionData = manager.getPostPermission();
//        tvPermission.setText(permissionData);
//        setPermissionData(permissionData);
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            categoryId = mCategory.getCategoryId();
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            subCategoryId = mSubcatg.getSubCategoryId();
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
            categoryId = subCategoryId;
        }

//        if (mSubcatg != null && mCategory != null) {
//            categoryId = mCategory.getCategoryId();
//            subCategoryId = mSubcatg.getSubCategoryId();
//            App.setCategoryId(mCategory.getCategoryId());
//            categoryName = mCategory.getCategoryName();
//            audience = mSubcatg.getSubCategoryName();
//            manager.setPostAudience(audience);
//            tvAudience.setText(audience);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        tvPermission.setText(manager.getPostPermission());
        String permissionData = manager.getPostPermission();
        //  tvPermission.setText(permissionData);
        //  setPermissionData(permissionData);
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            categoryId = mCategory.getCategoryId();
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            subCategoryId = mSubcatg.getSubCategoryId();
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
            categoryId = subCategoryId;
        }


    }
}
