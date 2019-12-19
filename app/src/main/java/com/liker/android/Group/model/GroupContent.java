
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupContent implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private GroupContentData contentData;
    @SerializedName("message")
    @Expose
    private Message message;
    public final static Creator<GroupContent> CREATOR = new Creator<GroupContent>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GroupContent createFromParcel(Parcel in) {
            return new GroupContent(in);
        }

        public GroupContent[] newArray(int size) {
            return (new GroupContent[size]);
        }

    }
    ;
    private final static long serialVersionUID = -9117477964968397281L;

    protected GroupContent(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.contentData = ((GroupContentData) in.readValue((GroupContentData.class.getClassLoader())));
        this.message = ((Message) in.readValue((Message.class.getClassLoader())));
    }

    public GroupContent() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public GroupContentData getData() {
        return contentData;
    }

    public void setData(GroupContentData contentData) {
        this.contentData = contentData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(contentData);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
