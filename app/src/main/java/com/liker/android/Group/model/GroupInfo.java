package com.liker.android.Group.model;
import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupInfo implements Serializable, Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("group_id")
    @Expose
    private String groupId;

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("creator_id")
    @Expose
    private String creatorId;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("total_member")
    @Expose
    private String totalMember;
    @SerializedName("total_post")
    @Expose
    private String totalPost;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("description")
    @Expose
    private String description;
    public final static Parcelable.Creator<GroupInfo> CREATOR = new Creator<GroupInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GroupInfo createFromParcel(Parcel in) {
            return new GroupInfo(in);
        }

        public GroupInfo[] newArray(int size) {
            return (new GroupInfo[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7748383126775667329L;

    protected GroupInfo(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.groupId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.creatorId = ((String) in.readValue((String.class.getClassLoader())));
        this.permission = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.createTime = ((String) in.readValue((String.class.getClassLoader())));
        this.totalMember = ((String) in.readValue((String.class.getClassLoader())));
        this.totalPost = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
    }

    public GroupInfo() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(String totalMember) {
        this.totalMember = totalMember;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(groupId);
        dest.writeValue(categoryId);
        dest.writeValue(creatorId);
        dest.writeValue(permission);
        dest.writeValue(status);
        dest.writeValue(createTime);
        dest.writeValue(totalMember);
        dest.writeValue(totalPost);
        dest.writeValue(imageName);
        dest.writeValue(description);
    }

    public int describeContents() {
        return 0;
    }

}
