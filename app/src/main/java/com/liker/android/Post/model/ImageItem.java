package com.liker.android.Post.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ImageItem implements Serializable, Parcelable
{

    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("width")
    @Expose
    private int width;
    public final static Parcelable.Creator<ImageItem> CREATOR = new Creator<ImageItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        public ImageItem[] newArray(int size) {
            return (new ImageItem[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5473614164809449000L;

    protected ImageItem(Parcel in) {
        this.img = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((int) in.readValue((int.class.getClassLoader())));
        this.width = ((int) in.readValue((int.class.getClassLoader())));
    }

    public ImageItem() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(img);
        dest.writeValue(height);
        dest.writeValue(width);
    }

    public int describeContents() {
        return 0;
    }

}