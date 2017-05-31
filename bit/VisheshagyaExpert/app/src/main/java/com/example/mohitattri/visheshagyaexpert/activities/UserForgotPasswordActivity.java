package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohitattri.visheshagyaexpert.R;

public class UserForgotPasswordActivity extends BaseActivity {

    Context context;
    EditText edUserEmail,edUserMobile;
    Button btnResetPassword;
    TextView tvNavigateSignInActivity;

    @Override
    protected void initViews() {
        edUserEmail=(EditText) findViewById(R.id.edUserEmail);
        edUserMobile=(EditText) findViewById(R.id.edUserMobile);
        btnResetPassword=(Button) findViewById(R.id.btnResetPassword);
        tvNavigateSignInActivity=(TextView) findViewById(R.id.tvNavigateSignInActivity);
    }

    @Override
    protected void initContext() {
        context=UserForgotPasswordActivity.this;
        currentActivity=UserForgotPasswordActivity.this;
    }

    @Override
    protected void initListners() {
        btnResetPassword.setOnClickListener(this);
        tvNavigateSignInActivity.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnResetPassword:
                break;
            case R.id.tvNavigateSignInActivity:
                startActivity(currentActivity,UserLoginActivity.class,null,false,REQUEST_TAG_NO_RESULT,false,ANIMATION_SLIDE_UP);
                break;
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
