package com.liker.android.Group.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupContentData implements Serializable, Parcelable{

    @SerializedName("suggestedGroup")
    @Expose
    private List<SuggestedGroup> suggestedGroupsData = new ArrayList<SuggestedGroup>();
    @SerializedName("groupYouIn")
    @Expose
    private List<GroupYouIn> groupYouInData = new ArrayList<GroupYouIn>();
    @SerializedName("groupManage")
    @Expose
    private List<GroupManage> groupManageData = new ArrayList<GroupManage>();
    @SerializedName("suggested_category")
    @Expose
    private List<SuggestedCategory> suggestedCategoryData = new ArrayList<SuggestedCategory>();
    public final static Parcelable.Creator<GroupContentData> CREATOR = new Creator<GroupContentData>() {

        @SuppressWarnings({
                "unchecked"
        })
        public GroupContentData createFromParcel(Parcel in) {
            return new GroupContentData(in);
        }

        public GroupContentData[] newArray(int size) {
            return (new GroupContentData[size]);
        }

    };
    private final static long serialVersionUID = -3150127264798908366L;

    protected GroupContentData(Parcel in) {
        in.readList(this.suggestedGroupsData, (SuggestedGroup.class.getClassLoader()));
        in.readList(this.groupYouInData, (GroupYouIn.class.getClassLoader()));
        in.readList(this.groupManageData, (GroupManage.class.getClassLoader()));
        in.readList(this.suggestedCategoryData, (SuggestedCategory.class.getClassLoader()));
    }

    public GroupContentData() {
    }

    public List<SuggestedGroup> getSuggestedGroupData() {
        return suggestedGroupsData;
    }

    public void setSuggestedGroupData(List<SuggestedGroup> suggestedGroupsData) {
        this.suggestedGroupsData = suggestedGroupsData;
    }

    public List<GroupYouIn> getGroupYouInData() {
        return groupYouInData;
    }

    public void setGroupYouInData(List<GroupYouIn> groupYouInData) {
        this.groupYouInData = groupYouInData;
    }

    public List<GroupManage> getGroupManageData() {
        return groupManageData;
    }

    public void setGroupManageData(List<GroupManage> groupManageData) {
        this.groupManageData = groupManageData;
    }

    public List<SuggestedCategory> getSuggestedCategoryData() {
        return suggestedCategoryData;
    }

    public void setSuggestedCategoryData(List<SuggestedCategory> suggestedCategoryData) {
        this.suggestedCategoryData = suggestedCategoryData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(suggestedGroupsData);
        dest.writeList(groupYouInData);
        dest.writeList(groupManageData);
        dest.writeList(suggestedCategoryData);
    }

    public int describeContents() {
        return 0;
    }


}
