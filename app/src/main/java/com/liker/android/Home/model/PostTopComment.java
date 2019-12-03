package com.liker.android.Home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liker.android.Comment.model.Comment_;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostTopComment implements Serializable, Parcelable {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("comments")
    @Expose
    private List<Comment_> comment = new ArrayList<Comment_>();

    public PostTopComment() {
    }

    protected PostTopComment(Parcel in) {

        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.comment, (Comment_.class.getClassLoader()));
    }

    public static final Creator<PostTopComment> CREATOR = new Creator<PostTopComment>() {
        @Override
        public PostTopComment createFromParcel(Parcel in) {
            return new PostTopComment(in);
        }

        @Override
        public PostTopComment[] newArray(int size) {
            return new PostTopComment[size];
        }
    };

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<Comment_> getComment() {
        return comment;
    }

    public void setComment(List<Comment_> comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeList(comment);
    }
}
