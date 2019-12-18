package com.liker.android.Search.view;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.App;
import com.liker.android.R;
import com.liker.android.Search.adapter.AdvanceSearchAdapter;
import com.liker.android.Search.model.AdvanceSearches;
import com.liker.android.Search.model.User;
import com.liker.android.Search.service.FollowUnfollowClickListener;
import com.liker.android.Search.service.SearchService;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleSearchFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvAlertText;
    private RecyclerView recyclerView;
    private ProgressBar initialProgress,progressBar;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;

    private AdvanceSearchAdapter advanceSearchAdapter;
    private List<User> mUserList;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;
    private String queryResult;

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
        queryResult = App.getQueryResult();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.people_search_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        refreshLayout = view.findViewById(R.id.refreshLayout);
        tvAlertText = view.findViewById(R.id.alertText);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        initialProgress = view.findViewById(R.id.initial_progress);
        progressBar = view.findViewById(R.id.progress_bar);

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

        advanceSearchAdapter = new AdvanceSearchAdapter(getActivity(), mUserList, profileId, followUnfollowClickListener);
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
        Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 0, 1);
        sendAdvanceSearchRequest(call);
    }

    private void getPagination() {
        progressBar.setVisibility(View.VISIBLE);
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
                        mUserList.clear();
                        mUserList.addAll(advanceSearches.getUser());
                        offset += limit;
                        advanceSearchAdapter.notifyDataSetChanged();
                    }
                }
                tvAlertText.setVisibility(mUserList.size() == 0 ? View.VISIBLE : View.GONE);
                refreshLayout.setRefreshing(false);
                initialProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                tvAlertText.setVisibility(mUserList.size() == 0 ? View.VISIBLE : View.GONE);
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

    private void setFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = webService.setFollow(deviceId, token, profileId, profileId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
//                        mUserList.get(position).setIsFollowed(true);
                        advanceSearchAdapter.notifyItemChanged(position);
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
        Call<String> call = webService.setUnFollow(deviceId, token, profileId, profileId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
//                        mUserList.get(position).setIsFollowed(false);
                        advanceSearchAdapter.notifyItemChanged(position);
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
        Call<String> call = webService.sendBrowserNotification(deviceId, token, profileId, profileId, followUserId, "0", "follow");
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

