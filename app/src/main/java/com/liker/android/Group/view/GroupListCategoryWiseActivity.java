package com.liker.android.Group.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.liker.android.Group.adapter.GroupCategoryWiseAdapter;
import com.liker.android.Group.model.CategoryWiseGroup;
import com.liker.android.Group.model.GroupWiseCateGoryInfo;
import com.liker.android.Group.model.GroupWiseData;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Group.service.JoinClickListener;
import com.liker.android.R;
import com.liker.android.Search.LikerSearch;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupListCategoryWiseActivity extends AppCompatActivity {

    private static final String TAG = "GroupListCategoryWise";
    // TODO: 12/29/2019
    private String groupCategoryId;
    private String groupCategoryName;
    private String groupCategoryImage;

    private Toolbar myToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageCategoryGroup;
    private RecyclerView rvCategoryGroupList;
    private PrefManager manager;
    private ProgressDialog progressDialog;
    private boolean networkOk;
    private String userId, token, deviceId;
    private ArrayList<CategoryWiseGroup> categoryWiseGroupList;
    private GroupCategoryWiseAdapter categoryWiseAdapter;
    private GroupWebservice groupWebservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_category_wise);
        initialComponent();
        setCateGoryWiseGroupList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void setCateGoryWiseGroupList() {

        if (networkOk) {
            Call<GroupWiseCateGoryInfo> call = groupWebservice.categoryGroup(deviceId, userId, token, userId, groupCategoryId);
            requestCategoryWiseGroupList(call);
        } else {
            Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestCategoryWiseGroupList(Call<GroupWiseCateGoryInfo> call) {
        call.enqueue(new Callback<GroupWiseCateGoryInfo>() {
            @Override
            public void onResponse(Call<GroupWiseCateGoryInfo> call, Response<GroupWiseCateGoryInfo> response) {
                if (response.isSuccessful()) {
                    GroupWiseCateGoryInfo groupWiseCateGoryInfo = response.body();
                    GroupWiseData groupWiseData = groupWiseCateGoryInfo.getData();
                    categoryWiseGroupList.addAll(groupWiseData.getCategoryWiseGroup());
                    categoryWiseAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<GroupWiseCateGoryInfo> call, Throwable t) {

            }
        });
    }


    private void initialComponent() {

        groupCategoryId = getIntent().getExtras().getString("category_id");
        groupCategoryName = getIntent().getExtras().getString("category_name");
        groupCategoryImage = getIntent().getExtras().getString("category_image");
        if (groupCategoryId == null) {
            throw new AssertionError("Null data item received!");
        }
        if (groupCategoryName == null) {
            throw new AssertionError("Null data item received!");
        }
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(groupCategoryName);
     //   collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        progressDialog = new ProgressDialog(this);
        categoryWiseGroupList = new ArrayList<CategoryWiseGroup>();
        userId = manager.getProfileId();
        token = manager.getToken();
        deviceId = manager.getDeviceId();
        //  TextView tv = findViewById(R.id.coupons_lst);
        //tv.setText(getString(R.string.lorem));
        imageCategoryGroup = findViewById(R.id.imageCategoryGroup);
        rvCategoryGroupList = findViewById(R.id.rvCategoryGroupList);
        JoinClickListener joinClickListener = new JoinClickListener() {
            @Override
            public void onJoinClick(String isGroupMember, String groupId, int position) {


                boolean isMember = Boolean.parseBoolean(isGroupMember);
                if (isMember) {
                    setLeaveMember(groupId, position);
                } else {
                    setJoinMember(groupId, position);
                }
            }

        };
        categoryWiseAdapter = new GroupCategoryWiseAdapter(this, categoryWiseGroupList, joinClickListener);
        rvCategoryGroupList.setAdapter(categoryWiseAdapter);
        String coverImage = AppConstants.USER_UPLOADED_IMAGES + groupCategoryImage;
        Picasso.with(this).load(coverImage).into(imageCategoryGroup);


    }

    private void setLeaveMember(String groupId, int position) {
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        Call<String> call = groupWebservice.leaveMembers(deviceId, userId, token, userId, groupId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        categoryWiseGroupList.get(position).setIsMember(false);
                        categoryWiseAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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

    private void setJoinMember(String groupId, int position) {
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        Call<String> call = groupWebservice.joinMembers(deviceId, userId, token, userId, groupId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        categoryWiseGroupList.get(position).setIsMember(true);
                        categoryWiseAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_wise_group_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.groupItemSearch:
                startActivity(new Intent(GroupListCategoryWiseActivity.this, LikerSearch.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
