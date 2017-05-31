package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 29/04/2016.
 */
public class SignInUserModel implements Parcelable {


    public static SignInUserModel signInUserModel;

    private SignInUserModel() {

    }

    protected SignInUserModel(Parcel in) {
        email = in.readString();
        password = in.readString();
        isAdmin = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignInUserModel> CREATOR = new Creator<SignInUserModel>() {
        @Override
        public SignInUserModel createFromParcel(Parcel in) {
            return new SignInUserModel(in);
        }

        @Override
        public SignInUserModel[] newArray(int size) {
            return new SignInUserModel[size];
        }
    };

    public static SignInUserModel getInstance() {
        if (signInUserModel == null) {
            signInUserModel = new SignInUserModel();
        }
        return signInUserModel;
    }


    private String email;
    private String password;
    @SerializedName("is_admin")
    private boolean isAdmin;


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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
