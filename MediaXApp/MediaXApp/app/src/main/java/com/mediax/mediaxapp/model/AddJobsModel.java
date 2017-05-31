package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 05/05/2016.
 */
public class AddJobsModel implements Parcelable {

    public static AddJobsModel addJobsModel;

    private AddJobsModel() {

    }


    protected AddJobsModel(Parcel in) {
        title = in.readString();
        image = in.readString();
        desc = in.readString();
        isImage = in.readByte() != 0;
        createdBy = in.readString();
        zone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(desc);
        dest.writeByte((byte) (isImage ? 1 : 0));
        dest.writeString(createdBy);
        dest.writeString(zone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddJobsModel> CREATOR = new Creator<AddJobsModel>() {
        @Override
        public AddJobsModel createFromParcel(Parcel in) {
            return new AddJobsModel(in);
        }

        @Override
        public AddJobsModel[] newArray(int size) {
            return new AddJobsModel[size];
        }
    };

    public static AddJobsModel getInstance() {
        if (addJobsModel == null) {
            addJobsModel = new AddJobsModel();
        }
        return addJobsModel;
    }


    public void clear() {
        title = null;
        image = null;
        desc = null;
        isImage = false;
        createdBy = null;
    }

    private String title;
    private String image;
    private String desc;
    private boolean isImage;
    @SerializedName("created_by")
    private String createdBy;

    private String zone;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
