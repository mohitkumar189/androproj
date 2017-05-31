package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayank on 11/07/2016.
 */
public class ContactUsModel implements Parcelable {

    private String username;
    private String message;


    public ContactUsModel(Parcel in) {
        username = in.readString();
        message = in.readString();
    }

    public ContactUsModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactUsModel> CREATOR = new Creator<ContactUsModel>() {
        @Override
        public ContactUsModel createFromParcel(Parcel in) {
            return new ContactUsModel(in);
        }

        @Override
        public ContactUsModel[] newArray(int size) {
            return new ContactUsModel[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
