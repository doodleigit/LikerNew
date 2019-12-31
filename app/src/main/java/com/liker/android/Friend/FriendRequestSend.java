package com.liker.android.Friend;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.model.TopContributorStatus;

import java.io.Serializable;

public class FriendRequestSend implements Serializable, Parcelable
{

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;


    public final static Creator<FriendRequestSend> CREATOR = new Creator<FriendRequestSend>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FriendRequestSend createFromParcel(Parcel in) {
            return new FriendRequestSend(in);
        }

        public FriendRequestSend[] newArray(int size) {
            return (new FriendRequestSend[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7563008524312210609L;

    protected FriendRequestSend(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.toUserId = ((String) in.readValue((String.class.getClassLoader())));

    }

    public FriendRequestSend() {
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

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(toUserId);

    }


    public int describeContents() {
        return 0;
    }

}