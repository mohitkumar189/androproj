package com.example.android.adapters;

/**
 * Created by mohitattri on 4/13/17.
 */

public class AppItems {
    private String appName;
    private int AppIcon;

    public AppItems(String appName, int appIcon) {
        this.appName = appName;
        AppIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(int appIcon) {
        AppIcon = appIcon;
    }
}
