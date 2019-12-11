package com.liker.android.Tool.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Home.model.Headers;

import java.io.Serializable;

public class AddTraffic implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("pathname")
    @Expose
    private String pathName;
    @SerializedName("pathname_new")
    @Expose
    private String pathNameNew;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("stay_time")
    @Expose
    private int stayTime;
    @SerializedName("notify")
    @Expose
    private boolean notify;

    public AddTraffic() {}

    protected AddTraffic(Parcel in) {
        headers = in.readParcelable(Headers.class.getClassLoader());
        userId = in.readString();
        pathName = in.readString();
        pathNameNew = in.readString();
        deviceType = in.readString();
        stayTime = in.readInt();
        notify = in.readByte() != 0;
    }

    public static final Creator<AddTraffic> CREATOR = new Creator<AddTraffic>() {
        @Override
        public AddTraffic createFromParcel(Parcel in) {
            return new AddTraffic(in);
        }

        @Override
        public AddTraffic[] newArray(int size) {
            return new AddTraffic[size];
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

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathNameNew() {
        return pathNameNew;
    }

    public void setPathNameNew(String pathNameNew) {
        this.pathNameNew = pathNameNew;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(headers, i);
        parcel.writeString(userId);
        parcel.writeString(pathName);
        parcel.writeString(pathNameNew);
        parcel.writeString(deviceType);
        parcel.writeInt(stayTime);
        parcel.writeByte((byte) (notify ? 1 : 0));
    }
}
