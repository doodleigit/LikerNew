package com.liker.android.Post.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LinkScrapItem implements Serializable, Parcelable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("images")
    @Expose
    private List<ImageItem> images = new ArrayList<ImageItem>();
    @SerializedName("content_type")
    @Expose
    private int contentType;
    @SerializedName("total_images")
    @Expose
    private int totalImages;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    public final static Parcelable.Creator<LinkScrapItem> CREATOR = new Creator<LinkScrapItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LinkScrapItem createFromParcel(Parcel in) {
            return new LinkScrapItem(in);
        }

        public LinkScrapItem[] newArray(int size) {
            return (new LinkScrapItem[size]);
        }

    }
            ;
    private final static long serialVersionUID = -4049072007974873047L;

    protected LinkScrapItem(Parcel in) {
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.host = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.images, (ImageItem.class.getClassLoader()));
        this.contentType = ((int) in.readValue((int.class.getClassLoader())));
        this.totalImages = ((int) in.readValue((int.class.getClassLoader())));
        this.youtube = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LinkScrapItem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageItem> getImages() {
        return images;
    }

    public void setImages(List<ImageItem> images) {
        this.images = images;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeValue(host);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeList(images);
        dest.writeValue(contentType);
        dest.writeValue(totalImages);
        dest.writeValue(youtube);
    }

    public int describeContents() {
        return 0;
    }


}
