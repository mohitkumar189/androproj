package com.example.mohitattri.visheshagyaexpert.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.constants.AppConstants;
import com.example.mohitattri.visheshagyaexpert.interfaces.AlertClicked;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity implements AppConstants, AlertClicked, View.OnClickListener {
    Activity currentActivity;
    public Bundle bundle;
    Context context;
    ProgressDialog pdialog;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ListView mDrawerList;
    private boolean doubleBackToExitPressedOnce = false;

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    public Map<String, String> linkParamsMap;

    Dialog customDialog;
    Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout layout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityLayout = (FrameLayout) layout.findViewById(R.id.activity_layout);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(layout);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // mDrawerList = (ListView) findViewById(R.id.left_drawer);
        navigationView=(NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isActionBar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (isHomeButton()) {
            settingHomeButton();
        }

        initContext();
        initViews();
        initListners();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkParamsMap = new HashMap<>();
    }

    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode, boolean animationRequired, int animationType) {
        Intent intent = new Intent(activity, newclass);
        if (bundle != null)

            intent.putExtras(bundle);
        if (!isResult && !animationRequired)
            startActivity(intent);
        else if (!isResult && animationRequired) {
            startActivity(intent);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }

        } else if (isResult && animationRequired) {
            startActivityForResult(intent, requestCode);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }
        } else
            startActivityForResult(intent, requestCode);
    }

    public void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(context);
        }


        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }

        if (!pdialog.isShowing()) {
            pdialog.show();

        }

    }

    public void cancelProgressDialog() {
        pdialog.cancel();
    }


    public void toast(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void toastTesting(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void setMessage(String msg) {
        System.out.println("message printed :" + msg);
    }

    public void settingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);
    }

    public void settingTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void removingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onAlertClicked(alertType);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();

    }

    public void printMsg(String title, String msg) {
        System.out.println("the " + title + " is : " + msg);
    }
}