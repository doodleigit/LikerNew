package com.liker.android.Group.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupWiseData implements Serializable, Parcelable
{

    @SerializedName("category_wise_group")
    @Expose
    private List<CategoryWiseGroup> categoryWiseGroup = new ArrayList<CategoryWiseGroup>();
    public final static Parcelable.Creator<GroupWiseData> CREATOR = new Creator<GroupWiseData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GroupWiseData createFromParcel(Parcel in) {
            return new GroupWiseData(in);
        }

        public GroupWiseData[] newArray(int size) {
            return (new GroupWiseData[size]);
        }

    }
            ;
    private final static long serialVersionUID = 865086480685703934L;

    protected GroupWiseData(Parcel in) {
        in.readList(this.categoryWiseGroup, (CategoryWiseGroup.class.getClassLoader()));
    }

    public GroupWiseData() {
    }

    public List<CategoryWiseGroup> getCategoryWiseGroup() {
        return categoryWiseGroup;
    }

    public void setCategoryWiseGroup(List<CategoryWiseGroup> categoryWiseGroup) {
        this.categoryWiseGroup = categoryWiseGroup;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categoryWiseGroup);
    }

    public int describeContents() {
        return 0;
    }

}