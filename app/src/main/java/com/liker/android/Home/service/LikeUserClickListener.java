package com.liker.android.Home.service;

public interface LikeUserClickListener {

    void onFollowClick(String followUserId, int position);
    void onUnFollowClick(String followUserId, int position);

}
