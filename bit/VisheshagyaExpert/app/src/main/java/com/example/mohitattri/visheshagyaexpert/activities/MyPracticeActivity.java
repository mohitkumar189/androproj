package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.mohitattri.visheshagyaexpert.R;

public class MyPracticeActivity extends BaseActivity {


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_practice);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initContext() {

        currentActivity = MyPracticeActivity.this;
        context = MyPracticeActivity.this;
    }

    @Override
    protected void initListners() {

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
    public void onClick(View view) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
