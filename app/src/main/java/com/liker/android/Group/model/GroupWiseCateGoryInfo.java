package com.liker.android.Group.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupWiseCateGoryInfo implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private GroupWiseData data;
    @SerializedName("message")
    @Expose
    private Message message;
    public final static Parcelable.Creator<GroupWiseCateGoryInfo> CREATOR = new Creator<GroupWiseCateGoryInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GroupWiseCateGoryInfo createFromParcel(Parcel in) {
            return new GroupWiseCateGoryInfo(in);
        }

        public GroupWiseCateGoryInfo[] newArray(int size) {
            return (new GroupWiseCateGoryInfo[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6389043207593078591L;

    protected GroupWiseCateGoryInfo(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.data = ((GroupWiseData) in.readValue((Data.class.getClassLoader())));
        this.message = ((Message) in.readValue((Message.class.getClassLoader())));
    }

    public GroupWiseCateGoryInfo() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public GroupWiseData getData() {
        return data;
    }

    public void setData(GroupWiseData data) {
        this.data = data;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(data);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}
