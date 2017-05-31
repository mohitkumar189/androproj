package practice.mohitattri.mybaseproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import practice.mohitattri.mybaseproject.interfaces.MyAppConstants;

/**
 * Created by mohitattri on 4/9/17.
 */

public class MySharedPreferencesUtils implements MyAppConstants{
    static MySharedPreferencesUtils sharedPreferenceUtils;
    static SharedPreferences sharedPreferences;
    static Context context;

    private MySharedPreferencesUtils() {

    }

    public static MySharedPreferencesUtils getInstance(Context context) {
        if (sharedPreferenceUtils == null) {
            sharedPreferenceUtils = new MySharedPreferencesUtils();
        }

        if (sharedPreferenceUtils.context == null) {
            sharedPreferenceUtils.context = context;
        }
        return sharedPreferenceUtils;
    }

    protected SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(MyAppConstants.APP_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    public String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();

    }

    public int getInteger(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public void putInteger(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public void clearALl() {
        getSharedPreferences().edit().clear().commit();
    }
}
