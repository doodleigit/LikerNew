
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Errors implements Serializable, Parcelable
{

    public final static Parcelable.Creator<Errors> CREATOR = new Creator<Errors>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Errors createFromParcel(Parcel in) {
            return new Errors(in);
        }

        public Errors[] newArray(int size) {
            return (new Errors[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8122181139138925939L;

    protected Errors(Parcel in) {
    }

    public Errors() {
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    public int describeContents() {
        return  0;
    }

}
