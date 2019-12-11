package com.liker.android.Search.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.liker.android.App;
import com.liker.android.R;
import com.liker.android.Search.adapter.AdvanceSearchAdapter;
import com.liker.android.Search.model.AdvanceSearches;
import com.liker.android.Search.model.User;
import com.liker.android.Search.service.SearchService;
import com.liker.android.Tool.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleSearchFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar initialProgress,progressBar;
    private LinearLayoutManager layoutManager;

    private AdvanceSearchAdapter advanceSearchAdapter;
    private List<User> mUserList;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;

    int limit = 15;
    int offset = 0;
    boolean isScrolling, isPagination = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = SearchService.mRetrofit.create(SearchService.class);
        manager = new PrefManager(getContext());
        mUserList = new ArrayList<>();
        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.people_search_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        initialProgress = view.findViewById(R.id.initial_progress);
        progressBar = view.findViewById(R.id.progress_bar);

        advanceSearchAdapter = new AdvanceSearchAdapter(getActivity(), mUserList);
        recyclerView.setAdapter(advanceSearchAdapter);

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
        String queryResult = App.getQueryResult();
        Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 0, 1);
        sendAdvanceSearchRequest(call);
    }

    private void getPagination() {
        progressBar.setVisibility(View.VISIBLE);
        String queryResult = App.getQueryResult();
        Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 0, 1);
        sendAdvanceSearchPaginationRequest(call);
    }

    private void sendAdvanceSearchRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getUser() != null) {
                        mUserList.addAll(advanceSearches.getUser());
                        offset += limit;
                        advanceSearchAdapter.notifyDataSetChanged();
                    }
                }
                refreshLayout.setRefreshing(false);
                initialProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
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
                        mUserList.addAll(advanceSearches.getUser());
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

}

