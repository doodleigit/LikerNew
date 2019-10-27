package com.liker.android.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostImage implements Parcelable {

    public String imagePath;
    public String imageId;
    public String mdFive;
    public boolean isDuplicate;
    MultipleMediaFile multipleMediaFile;



    public PostImage() {
    }

    public PostImage(String imagePath,String imageId) {
        this.imagePath = imagePath;
        this.imageId = imageId;
    }

    public PostImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public PostImage(String imagePath, String imageId,String mdFive,boolean isDuplicate ) {
        this.imagePath = imagePath;
        this.imageId = imageId;
        this.mdFive = mdFive;
        this.isDuplicate = isDuplicate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    protected PostImage(Parcel in) {
        imagePath = in.readString();
        imageId = in.readString();
        mdFive = in.readString();
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    public MultipleMediaFile getMultipleMediaFile() {
        return multipleMediaFile;
    }

    public void setMultipleMediaFile(MultipleMediaFile multipleMediaFile) {
        this.multipleMediaFile = multipleMediaFile;
    }

    public static final Creator<PostImage> CREATOR = new Creator<PostImage>() {
        @Override
        public PostImage createFromParcel(Parcel in) {
            return new PostImage(in);
        }

        @Override
        public PostImage[] newArray(int size) {
            return new PostImage[size];
        }
    };

    public String getMdFive() {
        return mdFive;
    }

    public void setMdFive(String mdFive) {
        this.mdFive = mdFive;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
        dest.writeString(imageId);
        dest.writeString(mdFive);
    }
}
