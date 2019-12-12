package com.liker.android.Group.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.liker.android.App;
import com.liker.android.Group.adapter.GroupMemberAdapter;
import com.liker.android.Group.model.Data;
import com.liker.android.Group.model.GroupMember;
import com.liker.android.Group.model.MyGroupMember;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Profile.adapter.FollowersAdapter;
import com.liker.android.Profile.model.Followers;
import com.liker.android.Profile.model.FollowersResult;
import com.liker.android.Profile.service.FollowUnfollowClickListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.doodle.Profile.adapter.FollowersAdapter;
//import com.doodle.Profile.model.Followers;
//import com.doodle.Profile.model.FollowersResult;
//import com.doodle.Profile.service.FollowUnfollowClickListener;
//import com.doodle.Profile.service.ProfileService;
//import com.doodle.R;
//import com.doodle.Tool.PrefManager;

public class GroupMemberFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvAlertText;

    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private ProfileService profileService;
    private PrefManager manager;
    private FollowersAdapter followersAdapter;
    private GroupMemberAdapter groupMemberAdapter;
    private ArrayList<FollowersResult> followers;
    private List<GroupMember> groupMembers;
    private String deviceId, profileUserId, token, userId;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;


    //GROUP-MEMBER===
   private GroupWebservice groupWebservice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.members_fragment_layout, container, false);

        initialComponent();
        sendGroupMemberListRequest();
        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
       // progressDialog.show();
        progressDialog.hide();
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        manager = new PrefManager(getContext());
        //followers = new ArrayList<>();
        groupMembers = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
       // profileUserId = getArguments().getString("user_id");
        profileUserId = manager.getProfileId();
        progressBar = view.findViewById(R.id.progress_bar);
        tvAlertText = view.findViewById(R.id.alertText);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        FollowUnfollowClickListener followUnfollowClickListener = new FollowUnfollowClickListener() {
            @Override
            public void onFollowClick(String followUserId, int position) {
                setFollow(followUserId, position);
            }

            @Override
            public void onUnFollowClick(String followUserId, int position) {
                setUnFollow(followUserId, position);
            }
        };

       // followersAdapter = new FollowersAdapter(getActivity(), followers, followUnfollowClickListener);
        groupMemberAdapter = new GroupMemberAdapter(getActivity(), groupMembers, followUnfollowClickListener);

       // recyclerView.setAdapter(followersAdapter);
        recyclerView.setAdapter(groupMemberAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                sendGroupMemberListRequest();
            }
        });

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
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    sendFriendListPaginationRequest();
                }
            }
        });
    }

    private void sendGroupMemberListRequest() {
        Call<MyGroupMember> call = groupWebservice.groupMembers(deviceId, userId, token, userId, AppSingleton.getInstance().getGroupId());
        call.enqueue(new Callback<MyGroupMember>() {
            @Override
            public void onResponse(Call<MyGroupMember> call, Response<MyGroupMember> response) {
               // followers.clear();
//                groupMembers.clear();
                MyGroupMember myGroupMember=response.body();
                Data data=myGroupMember.getData();
                groupMembers=data.getGroupMembers();

                if (groupMembers.size() > 0) {
                    offset += 10;
                }
                if (groupMembers.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }

                groupMemberAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MyGroupMember> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
             //   followers.clear();
                groupMembers.clear();
              //  followersAdapter.notifyDataSetChanged();
                groupMemberAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void sendFriendListPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MyGroupMember> call = groupWebservice.groupMembers(deviceId,userId, token,  userId, AppSingleton.getInstance().getGroupId());
        call.enqueue(new Callback<MyGroupMember>() {
            @Override
            public void onResponse(Call<MyGroupMember> call, Response<MyGroupMember> response) {
                Log.d("MyGroupMember",response.body().toString());
                MyGroupMember myGroupMember=response.body();
                Data data=myGroupMember.getData();
                groupMembers=data.getGroupMembers();
                if (groupMembers.size()> 0) {
                    groupMemberAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MyGroupMember> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
             //   followers.clear();
                groupMembers.clear();
              //  followersAdapter.notifyDataSetChanged();
                groupMemberAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = profileService.setFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                     //   followers.get(position).setIsFollowed(true);
                        groupMembers.get(position).setIsFollowed(true);
                      //  followersAdapter.notifyItemChanged(position);
                        groupMemberAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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

    private void setUnFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = profileService.setUnFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                     //   followers.get(position).setIsFollowed(false);
                        groupMembers.get(position).setIsFollowed(false);
                      //  followersAdapter.notifyItemChanged(position);
                        groupMemberAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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

    private void sendBrowserNotification(String followUserId) {
        Call<String> call = profileService.sendBrowserNotification(deviceId, token, userId, userId, followUserId, "0", "follow");
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
