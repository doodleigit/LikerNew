
package com.liker.android.Friend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NewFriendNotificationItem implements Serializable, Parcelable
{

    @SerializedName("friend_request_available")
    @Expose
    private String friendRequestAvailable;
    @SerializedName("friends")
    @Expose
    private List<Friend> friends = new ArrayList<Friend>();
    public final static Creator<NewFriendNotificationItem> CREATOR = new Creator<NewFriendNotificationItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewFriendNotificationItem createFromParcel(Parcel in) {
            return new NewFriendNotificationItem(in);
        }

        public NewFriendNotificationItem[] newArray(int size) {
            return (new NewFriendNotificationItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8148143190584893206L;

    protected NewFriendNotificationItem(Parcel in) {
        this.friendRequestAvailable = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.friends, (Friend.class.getClassLoader()));
    }

    public NewFriendNotificationItem() {
    }

    public String getFriendRequestAvailable() {
        return friendRequestAvailable;
    }

    public void setFriendRequestAvailable(String friendRequestAvailable) {
        this.friendRequestAvailable = friendRequestAvailable;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(friendRequestAvailable);
        dest.writeList(friends);
    }

    public int describeContents() {
        return  0;
    }

}
