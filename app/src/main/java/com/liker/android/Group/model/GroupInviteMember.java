
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupInviteMember implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private GroupInviteData groupInviteData;
    @SerializedName("message")
    @Expose
    private Message message;
    public final static Creator<GroupInviteMember> CREATOR = new Creator<GroupInviteMember>() {

        @SuppressWarnings({
            "unchecked"
        })
        public GroupInviteMember createFromParcel(Parcel in) {
            return new GroupInviteMember(in);
        }

        public GroupInviteMember[] newArray(int size) {
            return (new GroupInviteMember[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5981078508574370272L;

    protected GroupInviteMember(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.groupInviteData = ((GroupInviteData) in.readValue((GroupInviteData.class.getClassLoader())));
        this.message = ((Message) in.readValue((Message.class.getClassLoader())));
    }

    public GroupInviteMember() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public GroupInviteData getData() {
        return groupInviteData;
    }

    public void setData(GroupInviteData groupInviteData) {
        this.groupInviteData = groupInviteData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(groupInviteData);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
