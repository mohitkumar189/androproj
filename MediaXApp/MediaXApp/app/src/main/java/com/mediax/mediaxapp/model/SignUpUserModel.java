package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 27/04/2016.
 */
public class SignUpUserModel implements Parcelable {


    public static SignUpUserModel signUpUserModel;

    private SignUpUserModel() {

    }

    public static SignUpUserModel getInstance() {
        if (signUpUserModel == null) {
            signUpUserModel = new SignUpUserModel();
        }
        return signUpUserModel;
    }

    private String name;
    private String email;
    private String password;
    @SerializedName("mobile_no")
    private String mobileNo;
    private String brand;
    @SerializedName("category_id")
    private String categoryId;
    private String designation;
    @SerializedName("linkedin_profile")
    private String linkedinProfile;
    private String region;

    protected SignUpUserModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        mobileNo = in.readString();
        brand = in.readString();
        categoryId = in.readString();
        designation = in.readString();
        linkedinProfile = in.readString();
        region = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(mobileNo);
        dest.writeString(brand);
        dest.writeString(categoryId);
        dest.writeString(designation);
        dest.writeString(linkedinProfile);
        dest.writeString(region);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpUserModel> CREATOR = new Creator<SignUpUserModel>() {
        @Override
        public SignUpUserModel createFromParcel(Parcel in) {
            return new SignUpUserModel(in);
        }

        @Override
        public SignUpUserModel[] newArray(int size) {
            return new SignUpUserModel[size];
        }
    };


    public static SignUpUserModel getSignUpUserModel() {
        return signUpUserModel;
    }

    public static void setSignUpUserModel(SignUpUserModel signUpUserModel) {
        SignUpUserModel.signUpUserModel = signUpUserModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
