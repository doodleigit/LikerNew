package com.liker.android.Friend;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Home.model.Headers;

import java.io.Serializable;

public class FriendRequestStatus implements Serializable, Parcelable
{

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("friend_user_id")
    @Expose
    private String friendUserId;


    public final static Creator<FriendRequestStatus> CREATOR = new Creator<FriendRequestStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FriendRequestStatus createFromParcel(Parcel in) {
            return new FriendRequestStatus(in);
        }

        public FriendRequestStatus[] newArray(int size) {
            return (new FriendRequestStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7563008524312210609L;

    protected FriendRequestStatus(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.friendUserId = ((String) in.readValue((String.class.getClassLoader())));

    }

    public FriendRequestStatus() {
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(friendUserId);

    }


    public int describeContents() {
        return 0;
    }

}