package com.liker.android.Friend.service;

import android.widget.ProgressBar;

public interface FriendRequestListener {
    void onRequestConfirmListener(String fromId,String toId, int position, ProgressBar progressBarInvite);
    void onRequestDeleteListener(String fromId,String toId, int position, ProgressBar progressBarInvite);
}
