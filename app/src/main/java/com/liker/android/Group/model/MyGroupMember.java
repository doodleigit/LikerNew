
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyGroupMember implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private Message message;
    public final static Parcelable.Creator<MyGroupMember> CREATOR = new Creator<MyGroupMember>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MyGroupMember createFromParcel(Parcel in) {
            return new MyGroupMember(in);
        }

        public MyGroupMember[] newArray(int size) {
            return (new MyGroupMember[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2832127224518841944L;

    protected MyGroupMember(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.message = ((Message) in.readValue((Message.class.getClassLoader())));
    }

    public MyGroupMember() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
        return  0;
    }

}
