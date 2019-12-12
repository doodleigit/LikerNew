
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable, Parcelable
{

    @SerializedName("errors")
    @Expose
    private Errors errors;
    @SerializedName("success")
    @Expose
    private Success success;
    public final static Parcelable.Creator<Message> CREATOR = new Creator<Message>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return (new Message[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1902067705333183454L;

    protected Message(Parcel in) {
        this.errors = ((Errors) in.readValue((Errors.class.getClassLoader())));
        this.success = ((Success) in.readValue((Success.class.getClassLoader())));
    }

    public Message() {
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errors);
        dest.writeValue(success);
    }

    public int describeContents() {
        return  0;
    }

}
