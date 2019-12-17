package com.liker.android.Search.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.liker.android.App;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Post.view.activity.PostPopup;
import com.liker.android.R;
import com.liker.android.Search.adapter.AdvanceSearchPostAdapter;
import com.liker.android.Search.model.AdvanceSearches;
import com.liker.android.Search.model.Post;
import com.liker.android.Search.service.PostClickListener;
import com.liker.android.Search.service.SearchService;
import com.liker.android.Setting.view.SettingActivity;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostSearchFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvAlertText;
    private RecyclerView recyclerView;
    private ProgressBar initialProgress,progressBar;
    private LinearLayoutManager layoutManager;

    private AdvanceSearchPostAdapter advanceSearchAdapter;
    private List<Post> mPostList;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;
    private String queryResult;

    int limit = 15;
    int offset = 0;
    boolean isScrolling, isPagination;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = SearchService.mRetrofit.create(SearchService.class);
        manager = new PrefManager(getContext());
        mPostList = new ArrayList<>();
        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        queryResult = App.getQueryResult();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.post_search_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        tvAlertText = view.findViewById(R.id.alertText);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        initialProgress = view.findViewById(R.id.initial_progress);
        progressBar = view.findViewById(R.id.progress_bar);

        PostClickListener postClickListener = new PostClickListener() {
            @Override
            public void onPostClickListener(Post post) {
                if (post.getPostType().equals("2")) {
                    sendPostItemRequest(post.getPostId());
                } else if (post.getPostType().equals("3") || post.getPostType().equals("4")) {
                    openLinkScript(post.getPostText());
                }
            }
        };

        advanceSearchAdapter = new AdvanceSearchPostAdapter(getActivity(), mPostList, postClickListener);
        recyclerView.setAdapter(advanceSearchAdapter);
        tvAlertText.setText(getString(R.string.no_result_found_for) + " " + queryResult);

        getData();

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
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems, scrollOutItems, totalItems;
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();

                if (isScrolling && isPagination && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    isPagination = false;
                    getPagination();
                }
            }
        });

    }

    private void getData() {
        offset = 0;
        Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 1, 0);
        sendAdvanceSearchRequest(call);
    }

    private void getPagination() {
        progressBar.setVisibility(View.VISIBLE);
        Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 1, 0);
        sendAdvanceSearchPaginationRequest(call);
    }

    private void sendAdvanceSearchRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getUser() != null) {
                        mPostList.clear();
                        mPostList.addAll(advanceSearches.getPost());
                        offset += limit;
                        advanceSearchAdapter.notifyDataSetChanged();
                    }
                }
                tvAlertText.setVisibility(mPostList.size() == 0 ? View.VISIBLE : View.GONE);
                refreshLayout.setRefreshing(false);
                initialProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                tvAlertText.setVisibility(mPostList.size() == 0 ? View.VISIBLE : View.GONE);
                refreshLayout.setRefreshing(false);
                initialProgress.setVisibility(View.GONE);
            }
        });
    }

    private void sendAdvanceSearchPaginationRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getUser() != null) {
                        mPostList.addAll(advanceSearches.getPost());
                        offset += limit;
                        advanceSearchAdapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.GONE);
                isPagination = true;
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                isPagination = true;
            }
        });
    }

    private void sendPostItemRequest(String postId) {
        Call<PostItem> call = webService.getPostDetails(deviceId, profileId, token, profileId, postId);
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent intent = new Intent(getContext(), PostPopup.class);
                    intent.putExtra(AppConstants.ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra("has_footer", false);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }

    private void openLinkScript(String url) {
        List<String> extractedUrls = Tools.extractUrls(url);
        if (extractedUrls != null && extractedUrls.size() > 0) {
            webLink(extractedUrls.get(0), extractedUrls.get(0));
        }
    }

    private void webLink(String type, String link) {
        Intent termsIntent = new Intent(getContext(), SettingActivity.class);
        termsIntent.putExtra("type", type);
        termsIntent.putExtra("link", link);
        startActivity(termsIntent);
    }

}

