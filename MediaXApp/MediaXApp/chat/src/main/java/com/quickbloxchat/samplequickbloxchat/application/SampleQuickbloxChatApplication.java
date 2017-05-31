package com.quickbloxchat.samplequickbloxchat.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.quickblox.core.QBSettings;
import com.quickbloxchat.samplequickbloxchat.activity.ShopActivity;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;

import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.billing.Prices;
import vc908.stickerfactory.utils.Utils;

/**
 * Created by Mayank on 05/05/2016.
 */
public class SampleQuickbloxChatApplication extends Application implements AppConstants {

    public static final String STICKER_API_KEY = "847b82c49db21ecec88c510e377b452c";

    public SampleQuickbloxChatApplication() {
    }

    private static SampleQuickbloxChatApplication sampleQuickbloxChatApplication;


    public static SampleQuickbloxChatApplication getInstance() {
        if (sampleQuickbloxChatApplication == null) {
            sampleQuickbloxChatApplication = new SampleQuickbloxChatApplication();
        }
        return sampleQuickbloxChatApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }


    public int getAppVersion() {
        return 1;
      /*  try {
            PackageInfo packageInfo = sampleQuickbloxChatApplication.getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
