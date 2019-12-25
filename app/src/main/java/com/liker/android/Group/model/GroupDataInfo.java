package com.liker.android.Group.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDataInfo implements Serializable, Parcelable {

    @SerializedName("group_info")
    @Expose
    private GroupInfo groupInfo;
    @SerializedName("is_member")
    @Expose
    private boolean isMember;
    public final static Parcelable.Creator<GroupDataInfo> CREATOR = new Creator<GroupDataInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GroupDataInfo createFromParcel(Parcel in) {
            return new GroupDataInfo(in);
        }

        public GroupDataInfo[] newArray(int size) {
            return (new GroupDataInfo[size]);
        }

    };
    private final static long serialVersionUID = 6451855935166243909L;

    protected GroupDataInfo(Parcel in) {
        this.groupInfo = ((GroupInfo) in.readValue((GroupInfo.class.getClassLoader())));
        this.isMember = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public GroupDataInfo() {
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public boolean isIsMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(groupInfo);
        dest.writeValue(isMember);
    }

    public int describeContents() {
        return 0;
    }

}