package com.liker.android.Profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFeatured {

    @SerializedName("feature_order")
    @Expose
    private String featureOrder;
    @SerializedName("is_featured")
    @Expose
    private boolean isFeatured;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("filename")
    @Expose
    private String fileName;

    public AddFeatured(String featureOrder, boolean isFeatured, String userId, String fileName) {
        this.featureOrder = featureOrder;
        this.isFeatured = isFeatured;
        this.userId = userId;
        this.fileName = fileName;
    }

    public String getFeatureOrder() {
        return featureOrder;
    }

    public void setFeatureOrder(String featureOrder) {
        this.featureOrder = featureOrder;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
