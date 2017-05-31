package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayank on 15/06/2016.
 */
public class UserModel implements Parcelable {

    private String id;
    private String name;
    private String compName;
    private String designation;
    private String contactNo;
    private String emailId;


    public UserModel() {

    }

    protected UserModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        compName = in.readString();
        designation = in.readString();
        contactNo = in.readString();
        emailId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(compName);
        dest.writeString(designation);
        dest.writeString(contactNo);
        dest.writeString(emailId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getDesignaton() {
        return designation;
    }

    public void setDesignaton(String designaton) {
        this.designation = designaton;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
