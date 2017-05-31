package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mayank on 12/06/2016.
 */
public class ProfileDetailsModel implements Parcelable {

    private String region;
    private String category_name;
    private String email;
    private String name;
    private String designation;
    private String brand;
    private String linkedin_profile;
    private String mobile_no;
    private String userId;
    private String statusMessage;

    @SerializedName("user_image")
    private String userImage;

    protected ProfileDetailsModel(Parcel in) {
        region = in.readString();
        category_name = in.readString();
        email = in.readString();
        name = in.readString();
        designation = in.readString();
        brand = in.readString();
        linkedin_profile = in.readString();
        mobile_no = in.readString();
        userId = in.readString();
        statusMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(region);
        dest.writeString(category_name);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(designation);
        dest.writeString(brand);
        dest.writeString(linkedin_profile);
        dest.writeString(mobile_no);
        dest.writeString(userId);
        dest.writeString(statusMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileDetailsModel> CREATOR = new Creator<ProfileDetailsModel>() {
        @Override
        public ProfileDetailsModel createFromParcel(Parcel in) {
            return new ProfileDetailsModel(in);
        }

        @Override
        public ProfileDetailsModel[] newArray(int size) {
            return new ProfileDetailsModel[size];
        }
    };

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLinkedin_profile() {
        return linkedin_profile;
    }

    public void setLinkedin_profile(String linkedin_profile) {
        this.linkedin_profile = linkedin_profile;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


}
