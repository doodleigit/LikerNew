
package com.liker.android.Group.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("group_members")
    @Expose
    private List<GroupMember> groupMembers = new ArrayList<GroupMember>();
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5926729120825597606L;

    protected Data(Parcel in) {
        in.readList(this.groupMembers, (GroupMember.class.getClassLoader()));
    }

    public Data() {
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(groupMembers);
    }

    public int describeContents() {
        return  0;
    }

}
