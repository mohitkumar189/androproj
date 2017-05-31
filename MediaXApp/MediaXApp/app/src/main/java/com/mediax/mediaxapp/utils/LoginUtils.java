package com.mediax.mediaxapp.utils;

import android.content.Context;

import com.mediax.mediaxapp.constant.AppConstants;

/**
 * Created by Mayank on 27/04/2016.
 */
public class LoginUtils {


    public static boolean isLogin(Context context) {

        return SharedPreferenceUtils.getInstance(context).getSharedPreferences().getBoolean(AppConstants.IS_LOGIN, false);
    }


}
