package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.constants.AppConstants;

public class AppAccessActivity extends BaseActivity {


    Context context;
    FrameLayout flMyProfile,flMyAppointments,flELocker,flMyOffice,flMyPractice;

    @Override
    protected void initViews() {
        flMyProfile=(FrameLayout) findViewById(R.id.flMyProfile);
        flMyAppointments=(FrameLayout) findViewById(R.id.flMyAppointments);
        flELocker=(FrameLayout) findViewById(R.id.flELocker);
        flMyOffice=(FrameLayout) findViewById(R.id.flMyOffice);
        flMyPractice=(FrameLayout) findViewById(R.id.flMyPractice);
    }

    @Override
    protected void initContext() {
        context = AppAccessActivity.this;
        currentActivity = AppAccessActivity.this;
    }

    @Override
    protected void initListners() {
        flMyProfile.setOnClickListener(this);
        flMyAppointments.setOnClickListener(this);
        flELocker.setOnClickListener(this);
        flMyOffice.setOnClickListener(this);
        flMyPractice.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_access);
    }

    @Override
    public void onClick(View view) {
        int res_id=view.getId();
        switch (res_id){
            case R.id.flMyProfile:
                startActivity(currentActivity,MyProfileActivity.class,null,false,0,true,AppConstants.ANIMATION_SLIDE_UP);
                break;
            case R.id.flMyAppointments:
                startActivity(currentActivity,MyAppointmentsActivity.class,null,false,0,true,AppConstants.ANIMATION_SLIDE_UP);
                break;
            case R.id.flELocker:
                startActivity(currentActivity,ELockerActivity.class,null,false,0,true,AppConstants.ANIMATION_SLIDE_UP);
                break;
            case R.id.flMyOffice:
                startActivity(currentActivity,MyOfficeActivity.class,null,false,0,true,AppConstants.ANIMATION_SLIDE_UP);
                break;
            case R.id.flMyPractice:
                startActivity(currentActivity,MyPracticeActivity.class,null,false,0,true,AppConstants.ANIMATION_SLIDE_UP);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
