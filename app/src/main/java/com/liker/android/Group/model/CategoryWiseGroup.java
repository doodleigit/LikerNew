package com.liker.android.Group.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryWiseGroup implements Serializable, Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("total_member")
    @Expose
    private String totalMember;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("total_post")
    @Expose
    private String totalPost;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("is_member")
    @Expose
    private boolean isMember;
    public final static Parcelable.Creator<CategoryWiseGroup> CREATOR = new Creator<CategoryWiseGroup>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryWiseGroup createFromParcel(Parcel in) {
            return new CategoryWiseGroup(in);
        }

        public CategoryWiseGroup[] newArray(int size) {
            return (new CategoryWiseGroup[size]);
        }

    };
    private final static long serialVersionUID = -9054169165682272348L;

    protected CategoryWiseGroup(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.groupId = ((String) in.readValue((String.class.getClassLoader())));
        this.totalMember = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.totalPost = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
        this.isMember = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public CategoryWiseGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(String totalMember) {
        this.totalMember = totalMember;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isIsMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(groupId);
        dest.writeValue(totalMember);
        dest.writeValue(categoryId);
        dest.writeValue(totalPost);
        dest.writeValue(imageName);
        dest.writeValue(isMember);
    }

    public int describeContents() {
        return 0;
    }

}