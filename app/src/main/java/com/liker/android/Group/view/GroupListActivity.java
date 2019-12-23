package com.liker.android.Group.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.adapter.GroupManageAdapter;
import com.liker.android.Group.adapter.GroupYouInAdapter;
import com.liker.android.Group.adapter.SuggestedGroupAdapter;
import com.liker.android.Group.model.GroupContent;
import com.liker.android.Group.model.GroupContentData;
import com.liker.android.Group.model.GroupManage;
import com.liker.android.Group.model.GroupYouIn;
import com.liker.android.Group.model.Header;
import com.liker.android.Group.model.Message;
import com.liker.android.Group.model.Success;
import com.liker.android.Group.model.SuggestedGroup;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Group.service.InviteClickListener;
import com.liker.android.Group.service.JoinClickListener;
import com.liker.android.R;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Home.holder.TextHolder.COMMENT_TYPE_KEY;

public class GroupListActivity extends AppCompatActivity {

    private String groupCategoryName;
    private Toolbar mActionBarToolbar;
    private PrefManager manager;
    private String userId, token, deviceId;
    private GroupWebservice groupWebservice;
    private boolean networkOk;
    private RecyclerView recyclerView;
    private SuggestedGroupAdapter suggestedGroupAdapter;
    private GroupManageAdapter groupManageAdapter;
    private GroupYouInAdapter groupYouInAdapter;
    ArrayList<SuggestedGroup> suggestedGroups;
    ArrayList<GroupYouIn> groupYouInData;
    ArrayList<GroupManage> groupManageData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        initialComponent();
        setGroupList(groupCategoryName);
    }

    private void setGroupList(String groupCategoryName) {

        if (networkOk) {
            switch (groupCategoryName) {
                case "Suggested group":
                    Call<GroupContent> call1 = groupWebservice.suggestedGroup(deviceId, userId, token, userId);
                    requestGroupList(call1);
                    break;
                case "Group you're in":
                    Call<GroupContent> call2 = groupWebservice.groupYouIn(deviceId, userId, token, userId);
                    requestGroupList(call2);
                    break;
                case "Group you manage":
                    Call<GroupContent> call3 = groupWebservice.groupYouManage(deviceId, userId, token, userId);
                    requestGroupList(call3);
                    break;
            }
        } else {
            Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestGroupList(Call<GroupContent> call) {
        call.enqueue(new Callback<GroupContent>() {
            @Override
            public void onResponse(Call<GroupContent> call, Response<GroupContent> response) {
                GroupContent groupContent = response.body();
                displayData(groupContent);
            }

            @Override
            public void onFailure(Call<GroupContent> call, Throwable t) {
                Log.d("message", t.getMessage());
            }
        });
    }

    private void initialComponent() {
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        progressDialog = new ProgressDialog(this);
        suggestedGroups = new ArrayList<SuggestedGroup>();
        groupYouInData = new ArrayList<GroupYouIn>();
        groupManageData = new ArrayList<GroupManage>();
        userId = manager.getProfileId();
        token = manager.getToken();
        deviceId = manager.getDeviceId();

        groupCategoryName = getIntent().getExtras().getString("group_category_name");
        if (groupCategoryName == null) {
            throw new AssertionError("Null data item received!");
        }
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(groupCategoryName);
        recyclerView = findViewById(R.id.rvGroupList);
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
        switch (groupCategoryName) {
            case "Suggested group":
                suggestedGroupAdapter = new SuggestedGroupAdapter(this, suggestedGroups, joinClickListener);
                recyclerView.setAdapter(suggestedGroupAdapter);
                break;
            case "Group you're in":
                groupYouInAdapter = new GroupYouInAdapter(this, groupYouInData, joinClickListener);
                recyclerView.setAdapter(groupYouInAdapter);
                break;
            case "Group you manage":
                groupManageAdapter = new GroupManageAdapter(this, groupManageData, joinClickListener);
                recyclerView.setAdapter(groupManageAdapter);
                break;

        }
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

                        switch (groupCategoryName) {
                            case "Suggested group":
                                suggestedGroups.get(position).setIsMember("false");
                                suggestedGroupAdapter.notifyDataSetChanged();
                                break;
                            case "Group you're in":
                                groupYouInData.get(position).setIsMember("false");
                                groupYouInAdapter.notifyDataSetChanged();

                                break;
                            case "Group you manage":
                                groupManageData.get(position).setIsMember("false");
                                groupManageAdapter.notifyDataSetChanged();
                                break;

                        }


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
                        switch (groupCategoryName) {
                            case "Suggested group":
                                suggestedGroups.get(position).setIsMember("true");
                                suggestedGroupAdapter.notifyDataSetChanged();
                                break;
                            case "Group you're in":
                                groupYouInData.get(position).setIsMember("true");
                                groupYouInAdapter.notifyDataSetChanged();

                                break;
                            case "Group you manage":
                                groupManageData.get(position).setIsMember("true");
                                groupManageAdapter.notifyDataSetChanged();
                                break;

                        }
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

    private void displayData(GroupContent groupContent) {
        if (groupContent != null) {
            boolean status = groupContent.isStatus();
            Message message = groupContent.getMessage();
            Success success = message.getSuccess();
            String successStatus = success.getMessage();
            Toast.makeText(this, successStatus, Toast.LENGTH_SHORT).show();
            if (status) {
                GroupContentData groupContentData = groupContent.getData();

                suggestedGroups.clear();
                groupYouInData.clear();
                groupManageData.clear();
                switch (groupCategoryName) {
                    case "Suggested group":
                        suggestedGroups.addAll(groupContentData.getSuggestedGroupData());
                        suggestedGroupAdapter.notifyDataSetChanged();
                        break;
                    case "Group you're in":
                        groupYouInData.addAll(groupContentData.getGroupYouInData());
                        groupYouInAdapter.notifyDataSetChanged();
                        break;
                    case "Group you manage":
                        groupManageData.addAll(groupContentData.getGroupManageData());
                        groupManageAdapter.notifyDataSetChanged();
                        break;

                }

            }
        }
    }
}
