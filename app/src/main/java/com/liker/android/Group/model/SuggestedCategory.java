
package com.liker.android.Group.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedCategory implements Serializable, Parcelable
{

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    public final static Creator<SuggestedCategory> CREATOR = new Creator<SuggestedCategory>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SuggestedCategory createFromParcel(Parcel in) {
            return new SuggestedCategory(in);
        }

        public SuggestedCategory[] newArray(int size) {
            return (new SuggestedCategory[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2249235351193386356L;

    protected SuggestedCategory(Parcel in) {
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SuggestedCategory() {
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(catId);
        dest.writeValue(name);
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }

}
