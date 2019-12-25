package com.liker.android.Group.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.adapter.GroupInviteAdapter;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Group.service.InviteClickListener;
import com.liker.android.Profile.model.Followers;
import com.liker.android.Profile.model.FollowersResult;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupInviteActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvAlertText;

    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private ProfileService profileService;
    private GroupWebservice groupWebservice;
    private PrefManager manager;
    private GroupInviteAdapter groupInviteAdapter;
    private ArrayList<FollowersResult> followers;
    private String deviceId, profileUserId, token, userId;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private TextView tvBackGroupPage;
    private ImageView imageCancelInviteGroup;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invite);

        initialComponent();
        sendFriendListRequest();

        recyclerView = findViewById(R.id.recyclerView);
        tvBackGroupPage = findViewById(R.id.tvBackGroupPage);
        imageCancelInviteGroup = findViewById(R.id.imageCancelInviteGroup);
        imageCancelInviteGroup.setOnClickListener(this);
        tvBackGroupPage.setOnClickListener(this);
    }

    private void initialComponent() {

        groupId = getIntent().getExtras().getString("group_id");
        if (groupId == null) {
            throw new AssertionError("Null data item received!");
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        manager = new PrefManager(this);
        followers = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
//        profileUserId = getArguments().getString("user_id");
        profileUserId = manager.getProfileId();
        progressBar = findViewById(R.id.progress_bar);
        tvAlertText = findViewById(R.id.alertText);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        InviteClickListener inviteClickListener = new InviteClickListener() {

            @Override
            public void onInviteClick(String followUserId, int position) {

                setInviteGroupMember(followUserId, position);

             /*   @Override
                public void onFollowClick(String followUserId, int position) {
                    setFollow(followUserId, position);
                }

                @Override
                public void onUnFollowClick(String followUserId, int position) {
                    setUnFollow(followUserId, position);
                }
*/
            }

        };

        groupInviteAdapter = new GroupInviteAdapter(this, followers, inviteClickListener);

        recyclerView.setAdapter(groupInviteAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                sendFriendListRequest();
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

        EditText etSearchGroup = findViewById(R.id.etSearchGroup);

        etSearchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<FollowersResult> filteredList = new ArrayList<>();

        for (FollowersResult item : followers) {
            String name = item.getFirstName() + " " + item.getLastName();
            if (name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        groupInviteAdapter.filterList(filteredList);
      //  followers.clear();
        //followers.addAll(filteredList);
    }

    private void setInviteGroupMember(String followUserId, int position) {
        String[] inviteUsers = new String[]{followUserId};
        //   String[] inviteUsers={followUserId};
        progressDialog.show();
        Call<String> call = groupWebservice.inviteMembers(deviceId, userId, token, userId, groupId, inviteUsers);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        followers.get(position).setGroupMemberInvite(true);
                        groupInviteAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(GroupInviteActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                    JSONObject dataObject = obj.getJSONObject("data");
                    JSONObject messageObject = obj.getJSONObject("message");
                    JSONObject successObject = messageObject.getJSONObject("success");
                    String message = successObject.getString("message");
                    Toast.makeText(GroupInviteActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void sendFriendListRequest() {
        Call<Followers> call = profileService.getFollowers(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                followers.clear();
                Followers follower = response.body();
                if (follower != null) {
                    followers.addAll(follower.getFollowersResult());
                    offset += 10;
                }
                if (followers.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                groupInviteAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followers.clear();
                groupInviteAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void sendFriendListPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<Followers> call = profileService.getFollowers(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                Followers follower = response.body();
                if (follower != null) {
                    followers.addAll(follower.getFollowersResult());
                    groupInviteAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followers.clear();
                groupInviteAdapter.notifyDataSetChanged();
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
                        followers.get(position).setIsFollowed(true);
                        groupInviteAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(GroupInviteActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
                        followers.get(position).setIsFollowed(false);
                        groupInviteAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(GroupInviteActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.tvBackGroupPage:
                startActivity(new Intent(this, GroupPageActivity.class));
                finish();
                break;
            case R.id.imageCancelInviteGroup:
                startActivity(new Intent(this, GroupPageActivity.class));
                finish();
                break;
        }

    }
}
