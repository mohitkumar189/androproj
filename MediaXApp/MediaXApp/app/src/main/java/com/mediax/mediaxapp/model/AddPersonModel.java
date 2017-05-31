package com.mediax.mediaxapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 15/06/2016.
 */
public class AddPersonModel implements Parcelable {

    @SerializedName("userId")
    private int id;
    private String name;
    private String compName;
    private String designation;
    @SerializedName("contact_no")
    private String contactNo;
    @SerializedName("email_id")
    private String emailId;


    private Company company;

    private String requesterUserId;
    private String actionType;

    private String zone;


    public AddPersonModel() {

    }


    protected AddPersonModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        compName = in.readString();
        designation = in.readString();
        contactNo = in.readString();
        emailId = in.readString();
        company = in.readParcelable(Company.class.getClassLoader());
        requesterUserId = in.readString();
        actionType = in.readString();
        zone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(compName);
        dest.writeString(designation);
        dest.writeString(contactNo);
        dest.writeString(emailId);
        dest.writeParcelable(company, flags);
        dest.writeString(requesterUserId);
        dest.writeString(actionType);
        dest.writeString(zone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddPersonModel> CREATOR = new Creator<AddPersonModel>() {
        @Override
        public AddPersonModel createFromParcel(Parcel in) {
            return new AddPersonModel(in);
        }

        @Override
        public AddPersonModel[] newArray(int size) {
            return new AddPersonModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(String requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public static Creator<AddPersonModel> getCREATOR() {
        return CREATOR;
    }

    public static class Company implements Parcelable {
        private String name;

        public Company(Parcel in) {
            name = in.readString();
        }

        public Company() {

        }

        public static final Creator<Company> CREATOR = new Creator<Company>() {
            @Override
            public Company createFromParcel(Parcel in) {
                return new Company(in);
            }

            @Override
            public Company[] newArray(int size) {
                return new Company[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
        }



    }
}
