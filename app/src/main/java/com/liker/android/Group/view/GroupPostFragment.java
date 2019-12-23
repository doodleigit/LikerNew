package com.liker.android.Group.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Home.adapter.PostAdapter;
import com.liker.android.Home.holder.ImageHolder;
import com.liker.android.Home.holder.LinkScriptHolder;
import com.liker.android.Home.holder.LinkScriptYoutubeHolder;
import com.liker.android.Home.holder.TextHolder;
import com.liker.android.Home.holder.TextMimHolder;
import com.liker.android.Home.holder.VideoHolder;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.service.VideoPlayerRecyclerView;
import com.liker.android.Post.view.activity.WallPost;
import com.liker.android.Profile.service.ProfileDataFetchCompleteListener;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.doodle.App;
//import com.doodle.Comment.model.CommentItem;
//import com.doodle.Home.adapter.PostAdapter;
//import com.doodle.Home.model.PostItem;
//import com.doodle.Home.holder.ImageHolder;
//import com.doodle.Home.holder.LinkScriptHolder;
//import com.doodle.Home.holder.LinkScriptYoutubeHolder;
//import com.doodle.Home.holder.TextHolder;
//import com.doodle.Home.holder.TextMimHolder;
//import com.doodle.Home.holder.VideoHolder;
//import com.doodle.Home.service.VideoPlayerRecyclerView;
//import com.doodle.Post.view.activity.PostNew;
//import com.doodle.Post.view.activity.WallPost;
//import com.doodle.Profile.service.ProfileDataFetchCompleteListener;
//import com.doodle.Profile.service.ProfileService;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.NetworkHelper;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.Tools;

public class GroupPostFragment extends Fragment {
    View v;
    public List<PostItem> postItemList;
    private ProfileService profileService;
    private PrefManager manager;
    private String deviceId, profileuserId, profileUserName, token, userId, userImage;
    private int cat_id, filter;
    private boolean isPublic;
    private boolean networkOk;
    private ProgressBar progressView;
    private ProgressDialog progressDialog;
    //  private PostAdapter adapter;
    private PostAdapter adapter;
    private VideoPlayerRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private boolean isScrolling;
    int limit = 15;
    int offset = 0;
    private String catIds = "";

    private CardView addPostLayout;
    private TextView tvAlert;
    private CircleImageView ivImage;

    //Delete post item
    public static TextHolder.PostItemListener mCallback;
    public static TextMimHolder.PostItemListener mimListener;
    public static VideoHolder.PostItemListener videoListener;
    public static LinkScriptYoutubeHolder.PostItemListener youtubeListener;
    public static LinkScriptHolder.PostItemListener linkListener;
    public static ImageHolder.PostItemListener imageListener;

    PostItem deletePostItem;
    int deletePosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.PROFILE_PAGE_PAGINATION_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, filter);

        IntentFilter newPostFilter = new IntentFilter();
        newPostFilter.addAction(AppConstants.NEW_POST_ADD_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(newPostBroadcastReceiver, newPostFilter);

        IntentFilter postFooterIntentFilter = new IntentFilter();
        postFooterIntentFilter.addAction(AppConstants.POST_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(postChangeBroadcast, postFooterIntentFilter);

        IntentFilter permissionIntent = new IntentFilter();
        permissionIntent.addAction(AppConstants.PERMISSION_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(permissionBroadcast, permissionIntent);

        manager = new PrefManager(getActivity());
        deviceId = manager.getDeviceId();
//        profileUserName = getArguments().getString("user_name");
        profileUserName = manager.getUserName();
//        profileuserId = getArguments().getString("user_id");
        profileuserId = manager.getProfileId();
        token = manager.getToken();
        userId = manager.getProfileId();
        userImage = manager.getProfileImage();
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        postItemList = new ArrayList<>();
        deletePostItem = new PostItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.group_post_fragment_layout, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        addPostLayout = root.findViewById(R.id.add_post_layout);
        tvAlert = root.findViewById(R.id.alert);
        ivImage = root.findViewById(R.id.image);
        layoutManager = new LinearLayoutManager(getContext());
        progressView = root.findViewById(R.id.progress_view);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        v = root;

        ((GroupPageActivity) Objects.requireNonNull(getActivity())).profileDataFetchCompleteListener = new ProfileDataFetchCompleteListener() {
            @Override
            public void onComplete(String wallPermission, boolean isFollow) {
                if (wallPermission.equals("0")) {
                    addPostLayout.setVisibility(View.VISIBLE);
                } else if (wallPermission.equals("1")) {
                    addPostLayout.setVisibility(View.GONE);
                } else {
                    if (isFollow)
                        addPostLayout.setVisibility(View.VISIBLE);
                }

            }
        };

        mCallback = new TextHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);

            }
        };
        mimListener = new TextMimHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        videoListener = new VideoHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        youtubeListener = new LinkScriptYoutubeHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        linkListener = new LinkScriptHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        imageListener = new ImageHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                GroupPostFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        adapter = new PostAdapter(getActivity(), postItemList, mCallback, mimListener, videoListener, youtubeListener, linkListener, imageListener, AppConstants.WALL);
        recyclerView.setMediaObjects(postItemList);
        recyclerView.setActivityContext(getActivity());
        recyclerView.setAdapter(adapter);

        Glide.with(App.getAppContext())
                .load(userImage)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(ivImage);

        getData();

        addPostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Group post", Toast.LENGTH_SHORT).show();
                //  startActivity(new Intent(getContext(), WallPost.class).putExtra("wall_user_id", profileuserId));
                  startActivity(new Intent(getContext(), GroupNewPostActivity.class));
            }
        });

        return root;
    }

    private void deletePost(PostItem deletePostItem, int deletePosition) {
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Delete entry")
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_post))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkHelper.hasNetworkAccess(getContext())) {
                            Call<String> call = profileService.postDelete(deviceId, userId, token, userId, deletePostItem.getPostId());
                            sendDeletePostRequest(call);
                        } else {
                            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void sendDeletePostRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                postItemList.remove(deletePosition);
                                adapter.notifyDataSetChanged();
                                offset--;
                                recyclerView.smoothScrollToPosition(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });


    }

    private void getData() {
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            offset = 0;
            progressView.setVisibility(View.VISIBLE);
            Call<List<PostItem>> call = profileService.feed(deviceId, userId, token, userId, limit, offset, catIds, profileUserName, false);
            sendPostItemRequest(call);
        } else {
            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
        }
    }

    private void PerformPagination() {
        isScrolling = false;
        progressView.setVisibility(View.VISIBLE);
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            Call<List<PostItem>> call = profileService.feed(deviceId, userId, token, userId, limit, offset, catIds, profileUserName, false);
            PostItemPagingRequest(call);

        } else {
            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            isScrolling = true;
        }
    }

    private void PostItemPagingRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> list = response.body();

                if (list != null) {
                    postItemList.addAll(list);
                    String totalPostIDs;
                    List<String> postIdSet = new ArrayList<>();
                    for (PostItem temp : postItemList) {

                        postIdSet.add(temp.getPostId());
                    }
                    String separator = ", ";
                    int total = postIdSet.size() * separator.length();
                    for (String s : postIdSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : postIdSet) {
                        sb.append(separator).append(s);
                    }

                    totalPostIDs = sb.substring(separator.length()).replaceAll("\\s+", "");
                    Log.d("friends", totalPostIDs);
//                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", totalPostIDs, userIds);
//                    sendCommentItemPagingRequest(mCall);
                    offset += 15;
                    onPostResponsePagination();
                } else {
                    onPostResponsePagination();
                }
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                onPostResponsePagination();
            }
        });
    }

    private void sendPostItemRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> itemList = response.body();
                if (itemList != null) {
                    postItemList.clear();
                    postItemList.addAll(itemList);

//                    String totalPostIDs;
                    List<String> postIdSet = new ArrayList<>();
                    for (PostItem temp : postItemList) {

                        postIdSet.add(temp.getPostId());
                    }
                    String separator = ", ";
                    int total = postIdSet.size() * separator.length();
                    for (String s : postIdSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : postIdSet) {
                        sb.append(separator).append(s);
                    }
//                    totalPostIDs = sb.substring(separator.length()).replaceAll("\\s+", "");
//                    Log.d("friends", totalPostIDs);

                    offset = limit;
                    onPostResponse();
                } else {
                    postItemList.clear();
                    onPostResponseFailure();
                }

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                postItemList.clear();
                onPostResponseFailure();
            }
        });

    }

    private void onPostResponse() {
        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        progressDialog.dismiss();
        isScrolling = true;
    }

    private void onPostResponsePagination() {
        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        isScrolling = true;
    }

    private void onPostResponseFailure() {
        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.VISIBLE);
        progressDialog.dismiss();
        isScrolling = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        recyclerView.pausePlayer();
    }

    private boolean isViewShown = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;
            // call your function
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        } else {
            isViewShown = false;
        }

      /*  if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        }*/
    }

    BroadcastReceiver newPostBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isScrolling)
                PerformPagination();
        }
    };

    BroadcastReceiver postChangeBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            boolean isFooterChange = intent.getBooleanExtra("isFooterChange", true);
            int position = intent.getIntExtra("position", -1);
            if (isFooterChange) {
                if (position != -1) {
                    if (postItemList.size() >= position + 1) {
                        if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {
                            postItemList.get(position).getPostFooter().setPostTotalLike(postItem.getPostFooter().getPostTotalLike());
                            postItemList.get(position).getPostFooter().setLikeUserStatus(postItem.getPostFooter().isLikeUserStatus());
                            postItemList.get(position).setTotalComment(postItem.getTotalComment());
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
            } else {
                if (position != -1) {
                    if (postItemList.size() >= position + 1) {
                        if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {
                            postItemList.set(position, postItem);
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        }
    };

    BroadcastReceiver permissionBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            int position = intent.getIntExtra("position", -1);
            String type = intent.getStringExtra("type");

            if (position != -1) {
                if (postItemList.size() >= position + 1) {
                    if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {

                        if ("permission".equalsIgnoreCase(type)) {
                            postItemList.remove(position);
                            postItemList.add(position, postItem);
                            adapter.notifyItemChanged(position);
                        } else {
                            postItemList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.releasePlayer();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(newPostBroadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(postChangeBroadcast);
        Objects.requireNonNull(getActivity()).unregisterReceiver(permissionBroadcast);
    }


}
