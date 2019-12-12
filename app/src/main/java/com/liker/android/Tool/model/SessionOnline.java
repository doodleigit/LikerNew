package com.liker.android.Tool.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Home.model.Headers;

import java.io.Serializable;

public class SessionOnline implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("device_type")
    @Expose
    private String deviceType;

    public SessionOnline() {}

    protected SessionOnline(Parcel in) {
        headers = in.readParcelable(Headers.class.getClassLoader());
        userId = in.readString();
        url = in.readString();
        deviceType = in.readString();
    }

    public static final Creator<SessionOnline> CREATOR = new Creator<SessionOnline>() {
        @Override
        public SessionOnline createFromParcel(Parcel in) {
            return new SessionOnline(in);
        }

        @Override
        public SessionOnline[] newArray(int size) {
            return new SessionOnline[size];
        }
    };

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(headers, i);
        parcel.writeString(userId);
        parcel.writeString(url);
        parcel.writeString(deviceType);
    }
}
