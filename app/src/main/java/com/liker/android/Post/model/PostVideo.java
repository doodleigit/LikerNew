package com.liker.android.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostVideo implements Parcelable {
    public String videoPath;
    public String videoId;
    public String mdFive;
    public boolean isDuplicate;
    public MultipleMediaFile multipleMediaFile;



    public PostVideo(String videoPath, String videoId,boolean isDuplicate) {
        this.videoPath = videoPath;
        this.videoId = videoId;
        this.isDuplicate=isDuplicate;
    }


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }



    public PostVideo() {
    }

    protected PostVideo(Parcel in) {
        videoPath = in.readString();

    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    public static final Creator<PostVideo> CREATOR = new Creator<PostVideo>() {
        @Override
        public PostVideo createFromParcel(Parcel in) {
            return new PostVideo(in);
        }

        @Override
        public PostVideo[] newArray(int size) {
            return new PostVideo[size];
        }
    };

    public MultipleMediaFile getMultipleMediaFile() {
        return multipleMediaFile;
    }

    public void setMultipleMediaFile(MultipleMediaFile multipleMediaFile) {
        this.multipleMediaFile = multipleMediaFile;
    }

    public String getMdFive() {
        return mdFive;
    }

    public void setMdFive(String mdFive) {
        this.mdFive = mdFive;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoPath);

    }
}
