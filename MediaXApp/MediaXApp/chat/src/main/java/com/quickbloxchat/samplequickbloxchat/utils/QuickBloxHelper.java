package com.quickbloxchat.samplequickbloxchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quickblox.auth.QBAuth;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.server.BaseService;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mayank on 31/05/2016.
 */
public class QuickBloxHelper implements AppConstants {


    public static boolean isQuickBloxLoginFromExistingToken(Context context) {

        Date expirationDate = null;
        String token = null;

        token = SharedPreferenceUtils.getInstance(context).getString(QUICKBLOX_Token);

        if (token.equals("") || (SharedPreferenceUtils.getInstance(context).getLong(QUICKBLOX_LAST_AUTH_DATE_MILLIS) == 0)) {
            Log.e("existing token valid", "no");
            return false;
        }
        Log.e("existing token value", " " + token + " ");
        Log.e("existing token exp time", " " + SharedPreferenceUtils.getInstance(context).getLong(QUICKBLOX_LAST_AUTH_DATE_MILLIS));
        expirationDate = new Date(SharedPreferenceUtils.getInstance(context).getLong(QUICKBLOX_LAST_AUTH_DATE_MILLIS));

// save to secure storage when your application goes offline or to the background


        // recreate session on next start app
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR, 2);
        Date twoHoursAfter = cal.getTime();
        Log.e("current time", " " + currentDate.getTime() + " ");

        if (expirationDate.after(currentDate))

        {
            Log.e("existing token valid", "yes");
            try {
                QBAuth.createFromExistentToken(token, expirationDate);
            } catch (BaseServiceException e) {
                Log.e("existing token except", e.toString());
                e.printStackTrace();
            }
            return true;
        } else

        {
            Log.e("existing token valid", "no");

            // create a session
            return false;
        }
    }


}
