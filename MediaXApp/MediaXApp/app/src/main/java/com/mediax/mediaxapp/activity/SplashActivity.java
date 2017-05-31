package com.mediax.mediaxapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.fragment.MainBoard;
import com.mediax.mediaxapp.utils.LoginUtils;
import com.mediax.mediaxapp.utils.QuickbloxLoginUtils;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.utils.QuickBloxHelper;
import com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils;

import java.util.Date;

public class SplashActivity extends BaseActivity {
    Context context;

    public void createSessionAndLogin() {
        // Initialise Chat service

        final QBUser user = new QBUser();
        user.setLogin(com.mediax.mediaxapp.utils.SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
        user.setPassword(com.mediax.mediaxapp.utils.SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));

        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                SharedPreferenceUtils.getInstance(context).putInteger(QUICKBLOX_USER_ID, session.getUserId());
                toSaveQuickbloxDateAndToken();

                //      QuickbloxLoginUtils.getInstance(currentActivity).subscribeToPushNotifications(currentActivity,);
                sendToDashboard();


            }

            @Override
            public void onError(QBResponseException error) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("session creation errors: " + error).create().show();
                // errors
            }
        });


    }

    private void sendToDashboard() {
        startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SplashActivity.this;
        setContentView(R.layout.activity_splash);


   /*     com.quickbloxchat.samplequickbloxchat.activity.SplashActivity splashActivity = new com.quickbloxchat.samplequickbloxchat.activity.SplashActivity();
        splashActivity.createSessionAndLogin();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(currentActivity, AppAccessActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);


                if (LoginUtils.isLogin(context)) {
                    quickbloxLogin();

                } else {
                    startActivity(currentActivity, AppAccessActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                    finish();
                }
            }
        }, SPLASH_TIME);


    }

    private void quickbloxLogin() {
        if (QuickBloxHelper.isQuickBloxLoginFromExistingToken(context)) {
            sendToDashboard();
        } else {
            createSessionAndLogin();
        }
    }

    private void toSaveQuickbloxDateAndToken() {
        Date expirationDate = null;
        String token = null;
        try {
            token = BaseService.getBaseService().getToken();
            expirationDate = BaseService.getBaseService().getTokenExpirationDate();
            SharedPreferenceUtils.getInstance(context).putString(QUICKBLOX_Token, token);
            SharedPreferenceUtils.getInstance(context).putLong(QUICKBLOX_LAST_AUTH_DATE_MILLIS, expirationDate.getTime());

        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initContext() {

        currentActivity = SplashActivity.this;
        context = SplashActivity.this;
    }


    @Override
    protected void initViews() {

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
