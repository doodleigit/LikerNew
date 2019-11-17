package com.liker.android.Home.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

//import com.doodle.Authentication.model.UserInfo;
//import com.doodle.App;
//import com.doodle.Home.adapter.PostAdapter;
//import com.doodle.Home.model.PostItem;
//import com.doodle.Home.service.HomeService;
//import com.doodle.Home.holder.ImageHolder;
//import com.doodle.Home.holder.LinkScriptHolder;
//import com.doodle.Home.holder.LinkScriptYoutubeHolder;
//import com.doodle.Home.holder.TextHolder;
//import com.doodle.Home.holder.TextMimHolder;
//import com.doodle.Home.holder.VideoHolder;
//import com.doodle.Home.service.VideoPlayerRecyclerView;
//import com.doodle.Home.view.activity.Home;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.NetworkHelper;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Authentication.model.UserInfo;
import com.liker.android.Home.adapter.PostAdapter;
import com.liker.android.Home.holder.ImageHolder;
import com.liker.android.Home.holder.LinkScriptHolder;
import com.liker.android.Home.holder.LinkScriptYoutubeHolder;
import com.liker.android.Home.holder.TextHolder;
import com.liker.android.Home.holder.TextMimHolder;
import com.liker.android.Home.holder.VideoHolder;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.service.HomeService;
import com.liker.android.Home.service.TabClickListener;
import com.liker.android.Home.service.VideoPlayerRecyclerView;
import com.liker.android.Home.view.activity.Home;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreakingPost extends Fragment {


    private View v;

    public BreakingPost() {
        // Required empty public constructor
    }

    public List<PostItem> postItemList;
    //  private List<Comment> comments = new ArrayList<Comment>();
    private HomeService webService;
    private PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private int cat_id, filter = 1;
    private boolean isPublic;
    private boolean networkOk;
    private CircularProgressView progressView;
    //  private PostAdapter adapter;
    private PostAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private VideoPlayerRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling, isPaginationDone = true;
    int limit = 15;
    int offset = 0;
    private String catIds = "";
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView tvAlert;

    //Delete post item
    public static TextHolder.PostItemListener mCallback;
    public static TextMimHolder.PostItemListener mimListener;
    public static VideoHolder.PostItemListener videoListener;
    public static LinkScriptYoutubeHolder.PostItemListener youtubeListener;
    public static LinkScriptHolder.PostItemListener linkListener;
    public static ImageHolder.PostItemListener imageListener;

    PostItem deletePostItem;
    int deletePosition;
    private View currentFocusedLayout, oldFocusedLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter commonIntentFilter = new IntentFilter();
        commonIntentFilter.addAction(AppConstants.COMMON_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(commonReceiver, commonIntentFilter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.CATEGORY_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        IntentFilter postFooterIntentFilter = new IntentFilter();
        postFooterIntentFilter.addAction(AppConstants.POST_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(postChangeBroadcast, postFooterIntentFilter);

        IntentFilter permissionIntent = new IntentFilter();
        permissionIntent.addAction(AppConstants.PERMISSION_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(permissionBroadcast, permissionIntent);

        IntentFilter statusIntent = new IntentFilter();
        statusIntent.addAction(AppConstants.FOLLOW_STATUS_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(followStatusBroadcast, statusIntent);


        manager = new PrefManager(getActivity());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        postItemList = new ArrayList<>();
        deletePostItem = new PostItem();

        ((Home) getActivity()).breakingTabClickListener = new TabClickListener() {
            @Override
            public void onTabClick() {
                recyclerView.smoothScrollToPosition(0);
                refreshLayout.setRefreshing(true);
                getData();
//                recyclerView.scrollToPosition(0);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.breaking_post, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        progressView = (CircularProgressView) root.findViewById(R.id.progress_view);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_post_container);
        tvAlert = root.findViewById(R.id.alert);
        refreshLayout = root.findViewById(R.id.refreshLayout);
        recyclerView = root.findViewById(R.id.rvBreakingPost);
        recyclerView.setLayoutManager(layoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }


                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //get the recyclerview position which is completely visible and first
                    int positionView = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    Log.i("VISISBLE", positionView + "");
                    if (positionView >= 0) {
                        if (oldFocusedLayout != null) {
                            //Stop the previous video playback after new scroll
                        //    onPause();

                        }
                        currentFocusedLayout = ((LinearLayoutManager) recyclerView.getLayoutManager()).findViewByPosition(positionView);

                        //play video
                        oldFocusedLayout = currentFocusedLayout;
                      //  onResume();
                    }
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();
                Log.d("findFirstComplete", layoutManager.findFirstCompletelyVisibleItemPosition() + "");

                if (isScrolling && isPaginationDone && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    isPaginationDone = false;
                    PerformPagination();
                }


            }
        });

        mCallback = new TextHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);

            }
        };
        mimListener = new TextMimHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };


        videoListener = new VideoHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        youtubeListener = new LinkScriptYoutubeHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        linkListener = new LinkScriptHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        imageListener = new ImageHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                BreakingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

//        App.setSharePostfooter(false);
        adapter = new PostAdapter(getActivity(), postItemList, mCallback, mimListener, videoListener, youtubeListener, linkListener, imageListener, AppConstants.HOME);
        recyclerView.setMediaObjects(postItemList);
        recyclerView.setActivityContext(getActivity());
        recyclerView.setAdapter(adapter);
        getData();

        return root;
    }

    private void deletePost(PostItem deletePostItem, int deletePosition) {
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Delete entry")
                //   .setMessage("Are you sure you want to delete this post? You will permanently lose this post !")
                .setMessage("Are you sure that you want to delete this post?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkHelper.hasNetworkAccess(getContext())) {
                            Call<String> call = webService.postDelete(deviceId, profileId, token, userIds, deletePostItem.getPostId());
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
//                                postItemList.remove(deletePostItem);
//                                adapter.deleteItem(deletePosition);
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
        offset = 0;
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "breaking", catIds, filter, false);
            sendPostItemRequest(call);
        } else {
            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();
            refreshLayout.setRefreshing(false);
        }
    }

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "breaking", catIds, filter, false);
        PostItemPagingRequest(call);
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

     //   recyclerView.pausePlayer();
    }

    private void sendPostItemRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> itemList = response.body();
                if (itemList != null) {
                    postItemList.clear();
                    checkLearnAboutSiteStatus();
                    postItemList.addAll(itemList);

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

    private void checkLearnAboutSiteStatus() {
        Gson gson = new Gson();
        String json = manager.getUserInfo();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        if (userInfo.getLearnAboutSite() != null && userInfo.getLearnAboutSite().equals("0")) {
            PostItem postItem = new PostItem();
            postItem.setPostId("");
            postItem.setHasMeme("0");
            postItem.setPostType("0");
            postItemList.add(postItem);
        }
    }

    private void onPostResponse() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        try {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostResponsePagination() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        isPaginationDone = true;
    }

    private void onPostResponseFailure() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.VISIBLE);
        try {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        recyclerView.startPlayer();

    }


    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
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

    BroadcastReceiver commonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (type.equals("0")) {
                if (postItemList.size() > 0) {
                    if (postItemList.get(0).getPostType().equals("0")) {
                        postItemList.remove(0);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadInitial();
            recyclerView.scrollToPosition(0);
            catIds = intent.getStringExtra("category_ids");
            filter = intent.getIntExtra("filter", 1);
            getData();
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
                            // adapter.notifyItemChanged(position);
                            adapter.notifyDataSetChanged();
                        }


                    }
                }
            }
        }
    };

    BroadcastReceiver followStatusBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            int position = intent.getIntExtra("position", -1);
            String type = intent.getStringExtra("type");

            postItemList.remove(position);
            postItemList.add(position, postItem);
            adapter.notifyItemChanged(position);

//            if (position != -1) {
//                if (postItemList.size() >= position+1) {
//                    String id=postItemList.get(position).getPostId();
//                    String ids=postItem.getPostId();
//                    if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {
//
//                        postItemList.remove(position);
//                        postItemList.add(position, postItem);
//                        adapter.notifyItemChanged(position);
//                    /*    if("follow".equalsIgnoreCase(type)){
//                            postItemList.remove(position);
//                            postItemList.add(position, postItem);
//                            adapter.notifyItemChanged(position);
//                        }else {
//                            postItemList.remove(position);
//                           // adapter.notifyItemChanged(position);
//                            adapter.notifyDataSetChanged();
//                        }*/
//
//
//                    }
//                }
//            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.releasePlayer();
        Objects.requireNonNull(getActivity()).unregisterReceiver(commonReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(postChangeBroadcast);
        Objects.requireNonNull(getActivity()).unregisterReceiver(permissionBroadcast);
        Objects.requireNonNull(getActivity()).unregisterReceiver(followStatusBroadcast);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapter.screenChanged("LANDSCAPE");
            getActivity().sendBroadcast((
                    new Intent().putExtra("orientation", "LANDSCAPE"))
                    .setAction(AppConstants.NEW_ORIENTATION_BROADCAST));

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            adapter.screenChanged("PORTRAIT");
            getActivity().sendBroadcast((new Intent().putExtra("orientation", "PORTRAIT"))
                    .setAction(AppConstants.NEW_ORIENTATION_BROADCAST));
        }
    }
}
