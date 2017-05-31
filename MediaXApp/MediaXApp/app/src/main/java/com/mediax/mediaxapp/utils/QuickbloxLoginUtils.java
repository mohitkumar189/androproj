package com.mediax.mediaxapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.Dashboard;
import com.mediax.mediaxapp.fragment.MainBoard;
import com.mediax.mediaxapp.constant.AppConstants;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.users.model.QBUser;

import com.quickbloxchat.samplequickbloxchat.utils.*;
import com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Date;

import pushnotifications.PlayServicesHelper;

/**
 * Created by Mayank on 31/05/2016.
 */
public class QuickbloxLoginUtils implements AppConstants {

    static Context context;
    static Activity currentActivity;
    Bundle bundle;
    static QuickbloxLoginUtils quickbloxLoginUtils;

    public static QuickbloxLoginUtils getInstance(Activity currentActivity) {
        QuickbloxLoginUtils.context = currentActivity;
        QuickbloxLoginUtils.currentActivity = currentActivity;
        if (quickbloxLoginUtils == null) {
            quickbloxLoginUtils = new QuickbloxLoginUtils();
        }

        if (quickbloxLoginUtils.context == null) {
            quickbloxLoginUtils.context = context;
        }
        return quickbloxLoginUtils;
    }


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
                sendToDashboard();


            }

            @Override
            public void onError(QBResponseException error) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("session creation errors: " + error).create().show();
                // errors
            }
        });


    }


    public void quickbloxLogin() {
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
            com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils.getInstance(context).putString(QUICKBLOX_Token, token);
            com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils.getInstance(context).putLong(QUICKBLOX_LAST_AUTH_DATE_MILLIS, expirationDate.getTime());
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
    }

    private void sendToDashboard() {
        //
        ((BaseActivity) currentActivity).startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
        currentActivity.finish();

    }

    public void chatLogOut() {
        QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                ((BaseActivity) currentActivity).log("chat logout success", "yes", Log.ERROR);

            }

            @Override
            public void onError(QBResponseException e) {
                ((BaseActivity) currentActivity).log("error in chat   logout", e.toString(), Log.ERROR);
            }
        });
    }


    public void subscribeToPushNotifications(Activity currentActivity, String registrationID) {
        QBSubscription subscription = new QBSubscription(QBNotificationChannel.GCM);
        subscription.setEnvironment(QBEnvironment.DEVELOPMENT);
        //
        String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager) currentActivity.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId(); //*** use for mobiles
        } else {
            deviceId = Settings.Secure.getString(currentActivity.getContentResolver(),
                    Settings.Secure.ANDROID_ID); //*** use for tablets
        }
        subscription.setDeviceUdid(deviceId);
        //
        subscription.setRegistrationID(registrationID);
        //
        QBPushNotifications.createSubscription(subscription, new QBEntityCallback<ArrayList<QBSubscription>>() {

            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {

            }

            @Override
            public void onError(QBResponseException error) {

            }
        });
    }


}
