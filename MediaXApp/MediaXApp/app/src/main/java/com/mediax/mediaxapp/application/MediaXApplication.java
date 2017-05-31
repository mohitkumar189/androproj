package com.mediax.mediaxapp.application;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.quickblox.core.QBSettings;
import com.quickbloxchat.samplequickbloxchat.activity.ShopActivity;

import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.billing.Prices;
import vc908.stickerfactory.utils.Utils;


/**
 * Created by Mayank on 27/04/2016.
 */


public class MediaXApplication extends Application {

    private static MediaXApplication mInstance;
    private static RequestQueue mRequestQueue;
    public static String TAG = "dummy";

    public static String Tunnel_Url = "http://75136b88.ngrok.io";

    public static final String STICKER_API_KEY = "847b82c49db21ecec88c510e377b452c";

    static final String APP_ID = "40284";
    static final String AUTH_KEY = "eEMzVjXuCWchTap";
    static final String AUTH_SECRET = "esXLwvKQ-wygQJR";
    static final String ACCOUNT_KEY = "H4SkjgRxmzQP6qivevYy";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        //
        StickersManager.initialize(STICKER_API_KEY, this);

        // set current user id
        // now it device id, and it means,
        // that all purchases will be bound to current device
        StickersManager.setUserID(Utils.getDeviceId(this));
        // register our shop activity for inner currency payment
        StickersManager.setShopClass(ShopActivity.class);
        // set prices
        StickersManager.setPrices(new Prices()
                .setPricePointB("$0.99", 0.99f)
                .setPricePointC("$1.99", 1.99f));

    }


    public static synchronized MediaXApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request = settingTimeout(request);
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        //   request=settingTimeout(request);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                6000000,
                4,
                1000
        );
        request.setRetryPolicy(retryPolicy);
        getRequestQueue().add(request);
    }

    public Request settingTimeout(Request request) {
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        return request;
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

}