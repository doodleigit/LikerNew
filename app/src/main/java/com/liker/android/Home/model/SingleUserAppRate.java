package com.liker.android.Home.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SingleUserAppRate implements Parcelable, Serializable {

    private String userId;

    public SingleUserAppRate(String userId) {
        this.userId = userId;
    }

    public SingleUserAppRate() {
    }

    protected SingleUserAppRate(Parcel in) {
        userId = in.readString();
    }

    public static final Creator<SingleUserAppRate> CREATOR = new Creator<SingleUserAppRate>() {
        @Override
        public SingleUserAppRate createFromParcel(Parcel in) {
            return new SingleUserAppRate(in);
        }

        @Override
        public SingleUserAppRate[] newArray(int size) {
            return new SingleUserAppRate[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
    }
}
