
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedGroup implements Serializable, Parcelable,ListItem
{

    @SerializedName("creator_id")
    @Expose
    public String creatorId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("total_member")
    @Expose
    public String totalMember;
    @SerializedName("total_post")
    @Expose
    public String totalPost;
    @SerializedName("image_name")
    @Expose
    public String imageName;



    public final static Creator<SuggestedGroup> CREATOR = new Creator<SuggestedGroup>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SuggestedGroup createFromParcel(Parcel in) {
            return new SuggestedGroup(in);
        }

        public SuggestedGroup[] newArray(int size) {
            return (new SuggestedGroup[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3136757616409821745L;

    protected SuggestedGroup(Parcel in) {
        this.creatorId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.totalMember = ((String) in.readValue((String.class.getClassLoader())));
        this.totalPost = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SuggestedGroup() {
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(creatorId);
        dest.writeValue(name);
        dest.writeValue(totalMember);
        dest.writeValue(totalPost);
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }


    @Override
    public int getItemType() {
        return ListItem.TYPE_ITEM;
    }
}
