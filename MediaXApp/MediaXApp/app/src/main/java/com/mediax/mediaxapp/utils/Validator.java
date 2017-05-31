package com.mediax.mediaxapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mediax.mediaxapp.R;

/**
 * Created by Mayank on 27/04/2016.
 */
public class Validator {

    private static Validator validator;

    private Validator() {

    }

    public static Validator getInstance() {
        if (validator == null) {
            validator = new Validator();
        }
        return validator;
    }


    public static String isValidEmail(Context context, CharSequence target) {
        if (target == null) {
            return context.getString(R.string.error_empty_email_id);

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return context.getString(R.string.error_invalid_email_id);
        }
        return "";
    }


    public String validateNumber(Context context, String s) {


        if (s.length() != 10) {


            return context.getString(R.string.error_phone_no_invalid);
        }
        if (s.charAt(0) == '0') {

            return context.getString(R.string.error_additional_zero);
        }

        return "";
    }


    public final static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
