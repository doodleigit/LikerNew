package com.liker.android.Message.model;

import java.io.Serializable;

public class FriendInfo implements Serializable {

    private String userName, toUserId, fullName, photo, likes, stars, online, chatboxTurnOnOff;

    public FriendInfo(String userName, String toUserId, String fullName, String photo, String likes, String stars, String online, String chatboxTurnOnOff) {
        this.userName = userName;
        this.toUserId = toUserId;
        this.fullName = fullName;
        this.photo = photo;
        this.likes = likes;
        this.stars = stars;
        this.online = online;
        this.chatboxTurnOnOff = chatboxTurnOnOff;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getChatboxTurnOnOff() {
        return chatboxTurnOnOff;
    }

    public void setChatboxTurnOnOff(String chatboxTurnOnOff) {
        this.chatboxTurnOnOff = chatboxTurnOnOff;
    }
}
