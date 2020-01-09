package com.liker.android.Group.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupInviteData implements Serializable, Parcelable
{

    @SerializedName("invite_member")
    @Expose
    private List<InviteMember> inviteMember = new ArrayList<InviteMember>();
    public final static Parcelable.Creator<GroupInviteData> CREATOR = new Creator<GroupInviteData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GroupInviteData createFromParcel(Parcel in) {
            return new GroupInviteData(in);
        }

        public GroupInviteData[] newArray(int size) {
            return (new GroupInviteData[size]);
        }

    }
            ;
    private final static long serialVersionUID = 3773802058594911892L;

    protected GroupInviteData(Parcel in) {
        in.readList(this.inviteMember, (com.liker.android.Group.model.InviteMember.class.getClassLoader()));
    }

    public GroupInviteData() {
    }

    public List<InviteMember> getInviteMember() {
        return inviteMember;
    }

    public void setInviteMember(List<InviteMember> inviteMember) {
        this.inviteMember = inviteMember;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(inviteMember);
    }

    public int describeContents() {
        return  0;
    }

}
