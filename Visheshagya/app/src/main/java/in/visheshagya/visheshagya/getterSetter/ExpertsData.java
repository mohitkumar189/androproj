/*
    @Auther MOHIT KUMAR
    Created on 19/08/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.getterSetter;

public class ExpertsData {
    ///Expert Personal Details///
    public String expertId;
    public String expertName;
    public String expertProfileImageName;
    public String expertProfileSummary;
    public String expertCareerStartYear;

    ///Expert Professional Details///
    public String expertInstituteName;
    public String expertCategoryName;
    public String expertAddress1;
    public String expertAddress2;
    public String expertCityName;
    public String expertSkills;
    public String expertPinCode;
    public String expertCountry;

    ///Expert Fees Details///
    public String expertAudioFee;
    public String expertVideoFee;
    public String expertMeetFee;

    public int expertVerifiedDetail;

    //Constructor to save data inside the ARRAYLIST
    public ExpertsData(String expertName, String expertProfileImageName, String expertProfileSummary,
                       String expertCareerStartYear, String expertInstituteName, String expertCategoryName,
                       String expertAddress1, String expertAddress2, String expertCityName, String expertPinCode,
                       String expertCountry, String expertAudioFee, String expertVideoFee, String expertMeetFee, int expertVerifiedDetail) {
        this.expertName = expertName;
        this.expertProfileImageName = expertProfileImageName;
        this.expertProfileSummary = expertProfileSummary;
        this.expertCareerStartYear = expertCareerStartYear;
        this.expertInstituteName = expertInstituteName;
        this.expertCategoryName = expertCategoryName;
        this.expertAddress1 = expertAddress1;
        this.expertAddress2 = expertAddress2;
        this.expertCityName = expertCityName;
        this.expertPinCode = expertPinCode;
        this.expertCountry = expertCountry;
        this.expertAudioFee = expertAudioFee;
        this.expertVideoFee = expertVideoFee;
        this.expertMeetFee = expertMeetFee;
        this.expertVerifiedDetail = expertVerifiedDetail;
    }

    //Constructor to save data inside the ARRAYLIST
    public ExpertsData(String expertName, String expertProfileImageName, String expertProfileSummary,
                       String expertCareerStartYear, String expertInstituteName, String expertCategoryName,
                       String expertCityName, String expertSkills, String expertAudioFee, String expertVideoFee, String expertMeetFee) {
        this.expertName = expertName;
        this.expertProfileImageName = expertProfileImageName;
        this.expertProfileSummary = expertProfileSummary;
        this.expertCareerStartYear = expertCareerStartYear;
        this.expertInstituteName = expertInstituteName;
        this.expertCategoryName = expertCategoryName;
        this.expertCityName = expertCityName;
        this.expertSkills = expertSkills;
        this.expertAudioFee = expertAudioFee;
        this.expertVideoFee = expertVideoFee;
        this.expertMeetFee = expertMeetFee;
    }

    public ExpertsData(String expertId, String expertName, String expertCareerStartYear, String expertInstituteName, String expertCityName, String expertAudioFee, String expertVideoFee, String expertMeetFee) {
        this.expertId = expertId;
        this.expertName = expertName;
        this.expertCareerStartYear = expertCareerStartYear;
        this.expertInstituteName = expertInstituteName;

        this.expertCityName = expertCityName;
        this.expertAudioFee = expertAudioFee;
        this.expertVideoFee = expertVideoFee;
        this.expertMeetFee = expertMeetFee;

    }
    //Getter Setters for data manipulation

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getExpertProfileImageName() {
        return expertProfileImageName;
    }

    public void setExpertProfileImageName(String expertProfileImageName) {
        this.expertProfileImageName = expertProfileImageName;
    }

    public String getExpertProfileSummary() {
        return expertProfileSummary;
    }

    public void setExpertProfileSummary(String expertProfileSummary) {
        this.expertProfileSummary = expertProfileSummary;
    }

    public String getExpertCareerStartYear() {
        return expertCareerStartYear;
    }

    public void setExpertCareerStartYear(String expertCareerStartYear) {
        this.expertCareerStartYear = expertCareerStartYear;
    }

    public String getExpertInstituteName() {
        return expertInstituteName;
    }

    public void setExpertInstituteName(String expertInstituteName) {
        this.expertInstituteName = expertInstituteName;
    }

    public String getExpertCategoryName() {
        return expertCategoryName;
    }

    public void setExpertCategoryName(String expertCategoryName) {
        this.expertCategoryName = expertCategoryName;
    }

    public String getExpertAddress1() {
        return expertAddress1;
    }

    public void setExpertAddress1(String expertAddress1) {
        this.expertAddress1 = expertAddress1;
    }

    public String getExpertAddress2() {
        return expertAddress2;
    }

    public void setExpertAddress2(String expertAddress2) {
        this.expertAddress2 = expertAddress2;
    }

    public String getExpertCityName() {
        return expertCityName;
    }

    public void setExpertCityName(String expertCityName) {
        this.expertCityName = expertCityName;
    }

    public String getExpertSkills() {
        return expertSkills;
    }

    public void setExpertSkills(String expertSkills) {
        this.expertSkills = expertSkills;
    }

    public String getExpertPinCode() {
        return expertPinCode;
    }

    public void setExpertPinCode(String expertPinCode) {
        this.expertPinCode = expertPinCode;
    }

    public String getExpertCountry() {
        return expertCountry;
    }

    public void setExpertCountry(String expertCountry) {
        this.expertCountry = expertCountry;
    }

    public String getExpertAudioFee() {
        return expertAudioFee;
    }

    public void setExpertAudioFee(String expertAudioFee) {
        this.expertAudioFee = expertAudioFee;
    }

    public String getExpertVideoFee() {
        return expertVideoFee;
    }

    public void setExpertVideoFee(String expertVideoFee) {
        this.expertVideoFee = expertVideoFee;
    }

    public String getExpertMeetFee() {
        return expertMeetFee;
    }

    public void setExpertMeetFee(String expertMeetFee) {
        this.expertMeetFee = expertMeetFee;
    }

    public int getExpertVerifiedDetail() {
        return expertVerifiedDetail;
    }

    public void setExpertVerifiedDetail(int expertVerifiedDetail) {
        this.expertVerifiedDetail = expertVerifiedDetail;
    }
}
