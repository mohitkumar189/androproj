package practice.mohitattri.mybaseproject.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by mohitattri on 4/9/17.
 * Commonutils contains the most commonly used code for the application
 */

public class CommonUtils {

    private static ProgressDialog pdialog;

    public static void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
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

    public static void cancelProgressDialog(ProgressDialog pdialog) {
        pdialog.cancel();
    }


    public static void toast(Activity currentActivity,String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void toastTesting(Activity currentActivity,String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }


    public static void log(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public static void logTesting(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    protected static void toHideKeyboard(Activity currentActivity) {
        View view = currentActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
