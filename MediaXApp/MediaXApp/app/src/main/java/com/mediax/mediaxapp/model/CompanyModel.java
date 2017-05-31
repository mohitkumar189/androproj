package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayank on 05/05/2016.
 */
public class CompanyModel implements Parcelable {

    private String name;
    private String compId;


    public CompanyModel() {

    }

    protected CompanyModel(Parcel in) {
        name = in.readString();
        compId = in.readString();
    }

    public static final Creator<CompanyModel> CREATOR = new Creator<CompanyModel>() {
        @Override
        public CompanyModel createFromParcel(Parcel in) {
            return new CompanyModel(in);
        }

        @Override
        public CompanyModel[] newArray(int size) {
            return new CompanyModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(compId);
    }
}
