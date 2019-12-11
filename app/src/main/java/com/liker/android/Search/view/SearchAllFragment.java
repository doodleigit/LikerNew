package com.liker.android.Search.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liker.android.App;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Post.view.activity.PostPopup;
import com.liker.android.R;
import com.liker.android.Search.adapter.AdvanceSearchAdapter;
import com.liker.android.Search.adapter.AdvanceSearchPostAdapter;
import com.liker.android.Search.model.AdvanceSearches;
import com.liker.android.Search.model.Post;
import com.liker.android.Search.model.User;
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

public class SearchAllFragment extends Fragment {

    View view;
    private AdvanceSearches advanceSearches;
    private List<User> mUserList;
    private List<Post> mPostList;
    private AdvanceSearchAdapter mAdapter;
    private AdvanceSearchPostAdapter searchPostAdapter;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;

    int limit = 5;
    int postOffset = 5;
    boolean isPostLoadComplete = true;
    LinearLayout userLoadMoreLayout, postLoadMoreLayout;
    FrameLayout postLoadMore;
    ProgressBar postLoadMoreProgress;
    RecyclerView userRecyclerView, postRecyclerView;
    TextView tvUserSeeAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_all_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        mUserList = new ArrayList<>();
        mPostList = new ArrayList<>();
        webService = SearchService.mRetrofit.create(SearchService.class);
        manager = new PrefManager(getContext());

        userLoadMoreLayout = view.findViewById(R.id.user_load_more_layout);
        postLoadMoreLayout = view.findViewById(R.id.post_load_more_layout);
        postLoadMore = view.findViewById(R.id.post_load_more);
        tvUserSeeAll = view.findViewById(R.id.user_see_all);;

        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView = view.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        advanceSearches = getArguments().getParcelable("search_result");
        if (advanceSearches == null) {
            throw new AssertionError("Null data item received!");
        }
        mUserList = advanceSearches.getUser();
        mPostList = advanceSearches.getPost();

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

        mAdapter = new AdvanceSearchAdapter(getActivity(), mUserList);
        searchPostAdapter = new AdvanceSearchPostAdapter(getActivity(), mPostList, postClickListener);

        userRecyclerView.setAdapter(mAdapter);
        postRecyclerView.setAdapter(searchPostAdapter);

        setLoadMoreView();

        tvUserSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SearchActivity) Objects.requireNonNull(getActivity())).viewpager.setCurrentItem(2);
            }
        });

        postLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPostLoadComplete) {
                    isPostLoadComplete = false;
                    postLoadMoreProgress.setVisibility(View.VISIBLE);
                    String queryResult = App.getQueryResult();
                    Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, postOffset, 1, 0);
                    sendPostAdvanceSearchRequest(call);
                }
            }
        });
    }
    private void setLoadMoreView() {
        if (mUserList.size() == 0) {
            userLoadMoreLayout.setVisibility(View.GONE);
        } else {
            userLoadMoreLayout.setVisibility(View.VISIBLE);
        }
        if (mPostList.size() == 0) {
            postLoadMoreLayout.setVisibility(View.GONE);
        } else {
            postLoadMoreLayout.setVisibility(View.VISIBLE);
        }
    }

    private void openLinkScript(String url) {
//        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        List<String> extractedUrls = Tools.extractUrls(url);
        if (extractedUrls != null && extractedUrls.size() > 0) {
            webLink(extractedUrls.get(0), extractedUrls.get(0));

//            Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(extractedUrls.get(0)));
//            browserIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            App.getAppContext().startActivity(browserIntents);
        }
    }

    private void webLink(String type, String link) {
        Intent termsIntent = new Intent(getContext(), SettingActivity.class);
        termsIntent.putExtra("type", type);
        termsIntent.putExtra("link", link);
        startActivity(termsIntent);
    }

    private void sendPostAdvanceSearchRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getPost() != null) {
                        if (advanceSearches.getPost().size() < limit) {
                            postLoadMoreLayout.setVisibility(View.GONE);
                        }
                        mPostList.addAll(advanceSearches.getPost());
                        postOffset += 5;
                        searchPostAdapter.notifyDataSetChanged();
                    }
                }
                isPostLoadComplete = true;
                postLoadMoreProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                isPostLoadComplete = true;
                postLoadMoreProgress.setVisibility(View.GONE);
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

}
