package com.liker.android.Notification.service;

public interface NotificationClickListener {

    void onNotificationPostActionClick(String postId, boolean isCommentAction);
    void onNotificationClick(String notificationId);

}
