package com.mediax.mediaxapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.ChangePasswordActivity;
import com.mediax.mediaxapp.activity.Dashboard;
import com.mediax.mediaxapp.activity.ProfileActivity;
import com.mediax.mediaxapp.constant.AppConstants;

/**
 * Created by Mayank on 28/04/2016.
 */
public class SettingsFragment extends BaseFragment implements SwitchCompat.OnCheckedChangeListener, AppConstants {

    TextView textProfile;
    TextView textPushNotification;
    TextView textSound;
    TextView textChangePassword;
    TextView textLogout;

    SwitchCompat switchPush;
    SwitchCompat switchSound;

    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    protected void initViews() {
        textProfile = (TextView) view.findViewById(R.id.textProfile);
        textPushNotification = (TextView) view.findViewById(R.id.textPushNotification);
        textSound = (TextView) view.findViewById(R.id.textSound);
        textChangePassword = (TextView) view.findViewById(R.id.textChangePassword);
        textLogout = (TextView) view.findViewById(R.id.textLogout);

        switchPush = (SwitchCompat) view.findViewById(R.id.switchPush);
        switchSound = (SwitchCompat) view.findViewById(R.id.switchSound);
    }


    @Override
    protected void initListners() {
        textProfile.setOnClickListener(this);
        textChangePassword.setOnClickListener(this);
        textLogout.setOnClickListener(this);

        switchPush.setOnCheckedChangeListener(this);
        switchSound.setOnCheckedChangeListener(this);
    }

    @Override
    public void alertOkClicked() {

    }


    @Override
    protected void initContext() {

    }


    @Override
    public void onClick(View view) {

        textProfile.setOnClickListener(this);
        textChangePassword.setOnClickListener(this);
        textLogout.setOnClickListener(this);

        switchPush.setOnClickListener(this);
        switchSound.setOnClickListener(this);

        switch (view.getId()) {
            case R.id.textProfile: {
                ((BaseActivity) currentActivity).startActivity(currentActivity, ProfileActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

                break;
            }

            case R.id.textChangePassword: {
                ((BaseActivity) currentActivity).startActivity(currentActivity, ChangePasswordActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

                break;
            }

            case R.id.textLogout: {
                ((Dashboard) currentActivity).logout();
                break;
            }


        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switchPush: {
                break;
            }

            case R.id.switchSound: {
                break;
            }
        }
    }
}
