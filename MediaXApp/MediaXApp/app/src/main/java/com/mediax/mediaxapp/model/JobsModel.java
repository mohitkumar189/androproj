package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 05/05/2016.
 */
public class JobsModel implements Parcelable {

    @SerializedName("job_id")
    private String jobId;
    @SerializedName("job_title")
    private String jobTitle;
    @SerializedName("job_desc")
    private String jobDesc;
    @SerializedName("job_image")
    private String jobImage;
    @SerializedName("created_by")
    private String createdBy;


    protected JobsModel(Parcel in) {
        jobId = in.readString();
        jobTitle = in.readString();
        jobDesc = in.readString();
        jobImage = in.readString();
        createdBy = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeString(jobTitle);
        dest.writeString(jobDesc);
        dest.writeString(jobImage);
        dest.writeString(createdBy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobsModel> CREATOR = new Creator<JobsModel>() {
        @Override
        public JobsModel createFromParcel(Parcel in) {
            return new JobsModel(in);
        }

        @Override
        public JobsModel[] newArray(int size) {
            return new JobsModel[size];
        }
    };


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
