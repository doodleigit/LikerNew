package com.liker.android.Message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Home.model.Headers;

import java.io.Serializable;

public class OnlineNotify implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public OnlineNotify() {}

    protected OnlineNotify(Parcel in) {
        headers = in.readParcelable(Headers.class.getClassLoader());
        userId = in.readString();
    }

    public static final Creator<OnlineNotify> CREATOR = new Creator<OnlineNotify>() {
        @Override
        public OnlineNotify createFromParcel(Parcel in) {
            return new OnlineNotify(in);
        }

        @Override
        public OnlineNotify[] newArray(int size) {
            return new OnlineNotify[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(headers, i);
        parcel.writeString(userId);
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
}
