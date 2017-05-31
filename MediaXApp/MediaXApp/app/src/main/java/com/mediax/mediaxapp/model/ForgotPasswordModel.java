package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayank on 18/07/2016.
 */
public class ForgotPasswordModel implements Parcelable {


    private String email;
    private String password;



    public static ForgotPasswordModel forgotPasswordModel;

    private ForgotPasswordModel() {

    }


    public static ForgotPasswordModel getInstance() {
        if (forgotPasswordModel == null) {
            forgotPasswordModel = new ForgotPasswordModel();
        }
        return forgotPasswordModel;
    }


    protected ForgotPasswordModel(Parcel in) {
        email = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ForgotPasswordModel> CREATOR = new Creator<ForgotPasswordModel>() {
        @Override
        public ForgotPasswordModel createFromParcel(Parcel in) {
            return new ForgotPasswordModel(in);
        }

        @Override
        public ForgotPasswordModel[] newArray(int size) {
            return new ForgotPasswordModel[size];
        }
    };







    // getter setter


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
}
