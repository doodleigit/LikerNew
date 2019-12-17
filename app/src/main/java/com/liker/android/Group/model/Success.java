
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success implements Serializable, Parcelable
{

    @SerializedName("message")
    @Expose
    private String myMessage;
    public final static Parcelable.Creator<Success> CREATOR = new Creator<Success>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Success createFromParcel(Parcel in) {
            return new Success(in);
        }

        public Success[] newArray(int size) {
            return (new Success[size]);
        }

    }
    ;
    private final static long serialVersionUID = -9085830207691818131L;

    protected Success(Parcel in) {
        this.myMessage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Success() {
    }

    public String getMessage() {
        return myMessage;
    }

    public void setMessage(String message) {
        this.myMessage = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(myMessage);
    }

    public int describeContents() {
        return  0;
    }

}
