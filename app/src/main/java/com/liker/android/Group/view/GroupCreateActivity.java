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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Authentication.model.UserInfo;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Home.model.PostItem;
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
    private String TAG = "GroupCreateActivity";
    private String categoryId = "", subCategoryId = "";
    private String groupId;
    private String categoryName, audience;
    private String permission;
    private String groupName;
    private String deviceId, token, userId;

    private ImageView imageCancelCreateGroup,imagePrivate;
    private PrefManager manager;
    private GroupWebservice groupWebservice;
    private boolean networkOk;
    private EditText etGroupName;
    private ViewGroup contentCategory,privatePermissionContainer;
    private TextView tvAudience, tvCreateGroup, tvEditGroup, tvCreateGroupTitle,tvPermissionPrivate,tvPrivatePermissionInfo;
    private RadioButton rbPublic, rbPrivate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        initComponent();
        setpermissionMode();
        checkPageMode();

    }

    private void setpermissionMode() {
        Gson gson = new Gson();
        String json = manager.getUserInfo();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
     int goldStar=Integer.parseInt(userInfo.goldStars);
     int silverStar=Integer.parseInt(userInfo.sliverStars);
     int totalStars=goldStar+silverStar;
     if(totalStars>0){

         rbPrivate.setEnabled(true);
         imagePrivate.setImageResource(R.drawable.ic_lock_black_24dp);
         tvPermissionPrivate.setTextColor(Color.BLACK);

     }else {

         tvPermissionPrivate.setTextColor(Color.parseColor("#DCDCDC"));
         tvPrivatePermissionInfo.setTextColor(Color.parseColor("#DCDCDC"));
         imagePrivate.setImageResource(R.drawable.ic_lock_disable_24dp);

         // android:inputType="textCapWords"
         //   android:capitalize="words"
         rbPublic.setChecked(true);
         rbPublic.setSelected(true);
         rbPrivate.setEnabled(false);
         permission = "0";
     }

    }

    private void checkPageMode() {
        if (!isNullOrEmpty(groupName) && !isNullOrEmpty(groupId)) {
            tvCreateGroupTitle.setText(getString(R.string.edit_group));
            tvEditGroup.setVisibility(View.VISIBLE);
            tvCreateGroup.setVisibility(View.GONE);
            etGroupName.append(groupName);
        } else {
            tvCreateGroupTitle.setText(getString(R.string.create_new_group));
            tvCreateGroup.setVisibility(View.VISIBLE);
            tvEditGroup.setVisibility(View.GONE);
            etGroupName.append("");
        }
    }

    private void initComponent() {
        groupId = getIntent().getExtras().getString("group_id");
        groupName = getIntent().getExtras().getString("group_name");

        if (groupId == null) {
            throw new AssertionError("Null data item received!");
        }

        if (groupName == null) {
            throw new AssertionError("Null data item received!");
        }
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
        imageCancelCreateGroup = findViewById(R.id.imageCancelCreateGroup);
        imagePrivate = findViewById(R.id.imagePrivate);
        etGroupName = findViewById(R.id.etGroupName);
        contentCategory = findViewById(R.id.contentCategory);
        privatePermissionContainer = findViewById(R.id.privatePermissionContainer);
        rbPublic = findViewById(R.id.groupPublic);
        rbPrivate = findViewById(R.id.groupPrivate);
        rbPublic.setOnClickListener(this);
        rbPrivate.setOnClickListener(this);
        contentCategory.setOnClickListener(this);
        tvAudience = findViewById(R.id.tvAudience);
        tvCreateGroup = findViewById(R.id.tvCreateGroup);
        tvPermissionPrivate = findViewById(R.id.tvPermissionPrivate);
        tvPrivatePermissionInfo = findViewById(R.id.tvPrivatePermissionInfo);
        tvEditGroup = findViewById(R.id.tvEditGroup);
        tvCreateGroupTitle = findViewById(R.id.tvCreateGroupTitle);
        imageCancelCreateGroup.setOnClickListener(this);
        tvCreateGroup.setOnClickListener(this);
        tvEditGroup.setOnClickListener(this);


      //  tvAudience.setText(manager.getPostAudience().isEmpty() ? getString(R.string.audience) : manager.getPostAudience());
        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                groupName = s.toString();
                capitalizedGroupName(groupName);

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

    private void capitalizedGroupName(String mGroupName) {
        String str = mGroupName;
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        groupName=builder.toString();

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


                if (!isNullOrEmpty(deviceId) && !isNullOrEmpty(token) && !isNullOrEmpty(userId)) {
                    if (!isNullOrEmpty(groupName)) {
                        if (!isNullOrEmpty(categoryId)) {
                            if (!isNullOrEmpty(permission)) {

                                if (networkOk) {

                                    Call<String> call = groupWebservice.createGroup(deviceId, userId, token, userId, categoryId, groupName, permission);
                                    updateGroupRequest(call);
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
            case R.id.tvEditGroup:
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                if (!isNullOrEmpty(deviceId) && !isNullOrEmpty(token) && !isNullOrEmpty(userId)) {
                    if (!isNullOrEmpty(groupName)) {
                        if (!isNullOrEmpty(categoryId)) {
                            if (!isNullOrEmpty(permission)) {

                                if (networkOk) {

                                    Call<String> call = groupWebservice.updateGroup(deviceId, userId, token, userId, groupId, categoryId, groupName, permission);
                                    updateGroupRequest(call);
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

    private void updateGroupRequest(Call<String> call) {

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

                                JSONObject messageObject = object.getJSONObject("message");
                                JSONObject successObject = messageObject.getJSONObject("success");
                                if (successObject.length() > 0) {
                                    String groupId = dataObject.getString("group_id");
                                    String successMessage = successObject.getString("message");
                                 //   manager.setPostAudience("");
                                    Toast.makeText(GroupCreateActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GroupCreateActivity.this, GroupPageActivity.class).putExtra("group_id",groupId));
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
