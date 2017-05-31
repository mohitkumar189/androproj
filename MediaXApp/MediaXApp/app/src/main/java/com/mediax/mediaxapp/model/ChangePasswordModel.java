package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 04/05/2016.
 */
public class ChangePasswordModel implements Parcelable {


    public static ChangePasswordModel changePasswordModel;

    private ChangePasswordModel() {

    }


    @SerializedName("user_id")
    private int userId;
    @SerializedName("old_pwd")
    private String oldPassword;
    @SerializedName("new_pwd")
    private String  newPassword;


    protected ChangePasswordModel(Parcel in) {
        userId = in.readInt();
        oldPassword = in.readString();
        newPassword = in.readString();
    }


    public static ChangePasswordModel getInstance() {
        if (changePasswordModel == null) {
            changePasswordModel = new ChangePasswordModel();
        }
        return changePasswordModel;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(oldPassword);
        dest.writeString(newPassword);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChangePasswordModel> CREATOR = new Creator<ChangePasswordModel>() {
        @Override
        public ChangePasswordModel createFromParcel(Parcel in) {
            return new ChangePasswordModel(in);
        }

        @Override
        public ChangePasswordModel[] newArray(int size) {
            return new ChangePasswordModel[size];
        }
    };


    public static ChangePasswordModel getChangePasswordModel() {
        return changePasswordModel;
    }

    public static void setChangePasswordModel(ChangePasswordModel changePasswordModel) {
        ChangePasswordModel.changePasswordModel = changePasswordModel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String  getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String  oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String  getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String  newPassword) {
        this.newPassword = newPassword;
    }
}
