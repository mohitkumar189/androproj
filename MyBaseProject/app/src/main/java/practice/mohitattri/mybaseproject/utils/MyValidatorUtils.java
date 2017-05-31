package practice.mohitattri.mybaseproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import practice.mohitattri.mybaseproject.R;
import practice.mohitattri.mybaseproject.interfaces.MyAppConstants;

/**
 * Created by mohitattri on 4/9/17.
 * Validator class returns true or false for the validation
 * it returns message for the string validation
 */

public class MyValidatorUtils {
    private static MyValidatorUtils validator;

    private MyValidatorUtils() {

    }

    public static MyValidatorUtils getInstance() {
        if (validator == null) {
            validator = new MyValidatorUtils();
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

    public static boolean isExternalURL(String str) {
        if(str == null) return false;
        return str.indexOf( "http://" ) == 0 || str.indexOf( "https://" ) == 0 || str.indexOf( "www." ) == 0;
    }

    public static boolean isValidString( String val ) {
        return ( val != null && !"".equals( val ) && !"null".equals( val.toLowerCase() ) );
    }

    public final static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isLogin(Context context) {

        return MySharedPreferencesUtils.getInstance(context).getSharedPreferences().getBoolean(MyAppConstants.IS_LOGIN, false);
    }

}
