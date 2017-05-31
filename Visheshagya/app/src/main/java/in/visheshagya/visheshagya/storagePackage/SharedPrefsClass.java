package in.visheshagya.visheshagya.storagePackage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsClass {

    private static final String DEFAULT = "N/A";
    private static final Boolean DEFAULT_FALSE = false;
    private static final String NAME_KEY = "nameKey";
    private static final String LOGIN_STATUS = "statusKey";
    private static final String USER_TYPE = "userType";  /// 1 for expert and 2 for client
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String STATUS_APPLICATION = "status_application";
    private static final String CLIENT_ID = "clientId";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_MOBILE = "userMobile";
    private static final String USER_NAME = "userName";
    SharedPreferences sharedpreferences;
    private String mobileNumber;
    private Context context;

    public SharedPrefsClass(Context context) {
        this.context = context;
    }

    ///Get Login Status///////////
    public Boolean getLoginStatus() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            //return true;
            return sharedpreferences.getBoolean(LOGIN_STATUS, DEFAULT_FALSE); // TRUE if user is LOGGED In
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_FALSE;
        }
    }

    ///Set Login Status///////////
    public void setLoginStatus(Boolean loginStatus) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LOGIN_STATUS, loginStatus);
        editor.commit();
        //return loginStatus;
    }

    ///Get Login Name///////////
    public String getNameData() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try {
            return sharedpreferences.getString(NAME_KEY, DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT;
        }
    }

    ////// get client id////
    public String getClientId() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            return sharedpreferences.getString(CLIENT_ID, null);//// null if not find any value
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //// set client id////
    public void setClientId(String clientId) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(CLIENT_ID, clientId);
        editor.commit();
    }

    ///Get Login Name///////////
    public String getUserName() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            return sharedpreferences.getString(USER_NAME, DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT;
        }
    }

    // to set user name
    public void setUserName(String userName) {
        this.mobileNumber = mobileNumber;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    ///Get Application status ///////////
    public boolean getApplicationStatus() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            return sharedpreferences.getBoolean(STATUS_APPLICATION, DEFAULT_FALSE);//// FALSE if application is not opened
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_FALSE;
        }
    }

    ///Set Application status ///////////
    public void setApplicationStatus(Boolean applicationOpened) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(STATUS_APPLICATION, applicationOpened);
        editor.commit();
    }

    ///Clear Login Credentials///////////
    public void clearLoginCreds() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    // to get email address
    public String getEmailAddress() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            return sharedpreferences.getString(USER_EMAIL, DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT;
        }
    }

    // to set email address
    public void setEmailAddress(String emailAddress) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_EMAIL, emailAddress);
        editor.commit();
    }

    // to get mobile number
    public String getMobileNumber() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try {
            return sharedpreferences.getString(USER_MOBILE, DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT;
        }
    }

    // to set mobile number
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_MOBILE, mobileNumber);
        editor.commit();
    }

    // to get creds existance
    public boolean getLoginExistance() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(LOGIN_STATUS)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getEmailExistance() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(USER_EMAIL)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getMobileExistance() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(USER_MOBILE)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getNameExistance() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(USER_NAME)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getClientIdExistance() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(CLIENT_ID)) {
            return true;
        } else {
            return false;
        }
    }


}
