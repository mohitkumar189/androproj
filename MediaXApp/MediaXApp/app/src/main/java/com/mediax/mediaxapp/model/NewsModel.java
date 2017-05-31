package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 28/04/2016.
 */
public class NewsModel implements Parcelable {

    @SerializedName("news_id")
    private String newsId;
    @SerializedName("news_title")
    private String newsTitle;
    @SerializedName("news_desc")
    private String newsDesc;
    @SerializedName("news_image")
    private String newsImage;
    @SerializedName("created_by")
    private String createdBy;


    protected NewsModel(Parcel in) {
        newsId = in.readString();
        newsTitle = in.readString();
        newsDesc = in.readString();
        newsImage = in.readString();
        createdBy = in.readString();
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(newsId);
        parcel.writeString(newsTitle);
        parcel.writeString(newsDesc);
        parcel.writeString(newsImage);
        parcel.writeString(createdBy);
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }


}
