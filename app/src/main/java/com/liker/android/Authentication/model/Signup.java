package com.liker.android.Authentication.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Signup implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("bounce_data")
    @Expose
    private String bounceData;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("token")
    @Expose
    private String token;
    public final static Parcelable.Creator<Signup> CREATOR = new Creator<Signup>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Signup createFromParcel(Parcel in) {
            return new Signup(in);
        }

        public Signup[] newArray(int size) {
            return (new Signup[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1859542296683172806L;

    protected Signup(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.bounceData = ((String) in.readValue((String.class.getClassLoader())));
        this.userInfo = ((UserInfo) in.readValue((UserInfo.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Signup() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBounceData() {
        return bounceData;
    }

    public void setBounceData(String bounceData) {
        this.bounceData = bounceData;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(bounceData);
        dest.writeValue(userInfo);
        dest.writeValue(token);
    }

    public int describeContents() {
        return 0;
    }

}