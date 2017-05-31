/*
    @Auther MOHIT KUMAR
    Created on 24/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */

package in.visheshagya.visheshagya.getterSetter;

public class AppointmentsData {
    public String expertName;
    public String appointmentTiming;
    public String consultationType;
    public String appointmentStatus;
    public String appointmentAction;

    public AppointmentsData(String expertName, String appointmentTiming, String consultationType, String appointmentStatus, String appointmentAction) {
        this.expertName = expertName;
        this.appointmentTiming = appointmentTiming;
        this.consultationType = consultationType;
        this.appointmentStatus = appointmentStatus;
        this.appointmentAction = appointmentAction;
    }

    public AppointmentsData(String expertName, String appointmentTiming, String consultationType, String appointmentStatus) {
        this.expertName = expertName;
        this.appointmentTiming = appointmentTiming;
        this.consultationType = consultationType;
        this.appointmentStatus = appointmentStatus;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getAppointmentTiming() {
        return appointmentTiming;
    }

    public void setAppointmentTiming(String appointmentTiming) {
        this.appointmentTiming = appointmentTiming;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentAction() {
        return appointmentAction;
    }

    public void setAppointmentAction(String appointmentAction) {
        this.appointmentAction = appointmentAction;
    }
}
