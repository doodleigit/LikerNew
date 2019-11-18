package com.liker.android.Profile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonalPhoto implements Parcelable {

    private String id, imageName;

    public PersonalPhoto(String id, String imageName) {
        this.id = id;
        this.imageName = imageName;
    }

    protected PersonalPhoto(Parcel in) {
        id = in.readString();
        imageName = in.readString();
    }

    public static final Creator<PersonalPhoto> CREATOR = new Creator<PersonalPhoto>() {
        @Override
        public PersonalPhoto createFromParcel(Parcel in) {
            return new PersonalPhoto(in);
        }

        @Override
        public PersonalPhoto[] newArray(int size) {
            return new PersonalPhoto[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageName);
    }
}
