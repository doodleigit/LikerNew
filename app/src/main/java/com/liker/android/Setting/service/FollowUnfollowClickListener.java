package com.liker.android.Setting.service;

public interface FollowUnfollowClickListener {

    void onFollowClick(String followUserId, int position);
    void onUnFollowClick(String followUserId, int position);

}
