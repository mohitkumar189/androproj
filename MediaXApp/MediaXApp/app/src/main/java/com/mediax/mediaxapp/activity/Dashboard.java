package com.mediax.mediaxapp.activity;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.adapter.SideMenuListAdapter;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.fragment.ChatsFragment;
import com.mediax.mediaxapp.fragment.ContactUsFragment;
import com.mediax.mediaxapp.fragment.DatabaseFragment;
import com.mediax.mediaxapp.fragment.JobsFragment;
import com.mediax.mediaxapp.fragment.MainBoard;
import com.mediax.mediaxapp.fragment.NewsFargment;
import com.mediax.mediaxapp.fragment.ReviewFragment;
import com.mediax.mediaxapp.fragment.SettingsFragment;
import com.mediax.mediaxapp.utils.QuickbloxLoginUtils;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.widgets.CircularImageView;
import com.quickbloxchat.samplequickbloxchat.activity.FullImageViewActivity;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.fragment.ChatFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import pushnotifications.PlayServicesHelper;

public class Dashboard extends BaseActivity {


    DrawerLayout drawerLayout;
    LinearLayout fragmentContainerWithToolbar;
    FrameLayout fragmentContainer;

    TextView textUserName;
    RecyclerView recyclerNavigationDrawer;
    ActionBarDrawerToggle actionBarDrawerToggle;

    boolean isExitable;

    String tag;

    Fragment frag;

    SideMenuListAdapter sideMenuListAdapter;
    LinearLayoutManager managerSideMenu;

    int fragmentType = 0;

    Fragment chatFragment;


    CircularImageView circleImageDrawerProfile;

    String userProfileUrl;

    PlayServicesHelper playServicesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


    }

    @Override
    protected void initViews() {

        playServicesHelper = new PlayServicesHelper(currentActivity);
        if (getIntent().hasExtra(DASHBOARD_FRAGMENT_TYPE)) {
            fragmentType = getIntent().getIntExtra(DASHBOARD_FRAGMENT_TYPE, 0);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        fragmentContainerWithToolbar = (LinearLayout) findViewById(R.id.fragmentContainerWithToolbar);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
        circleImageDrawerProfile = (CircularImageView) findViewById(R.id.circleImageDrawerProfile);
        textUserName = (TextView) findViewById(R.id.textUserName);
        recyclerNavigationDrawer = (RecyclerView) findViewById(R.id.recyclerNavigationDrawer);
        circleImageDrawerProfile = (CircularImageView) findViewById(R.id.circleImageDrawerProfile);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                drawerLayout.openDrawer(Gravity.LEFT);

                return true;
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                toHideKeyboard();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    toHideKeyboard();

                }
            }

        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setSelection(fragmentType);


        sideMenuListAdapter = new SideMenuListAdapter(currentActivity);
        managerSideMenu = new LinearLayoutManager(currentActivity);

        recyclerNavigationDrawer.setLayoutManager(managerSideMenu);
        recyclerNavigationDrawer.setAdapter(sideMenuListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userProfileUrl = SharedPreferenceUtils.getInstance(currentActivity).getString(USER_IMAGE);

        if (userProfileUrl != null && !userProfileUrl.equals("")) {
            Picasso.with(currentActivity).load(BASE_URL_IMAGES + userProfileUrl).into(circleImageDrawerProfile);
        }

        textUserName.setText(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME));
    }

    public void setSelection(int position) {
        fragmentType = position;
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (frag != null) {
            tag = frag.getClass().getName();
        } else {
            tag = ChatsFragment.class.getName();
        }

        frag = null;
        boolean isAddedBackStack = false;

        boolean isReplace = false;

        switch (position) {

            case MAINBOARD: {
                frag = new MainBoard();
                settingTitle(getResources().getString(R.string.title_main_board));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }


            case CHATS: {
                if (SharedPreferenceUtils.getInstance(currentActivity).getBoolean(IS_USER_VALID)) {
                    openingChat();
                } else {
                    isUserVerified();
                }

/*
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();

                } else {
                    //   isReplace=true;
                }


                frag = chatFragment;
                settingTitle(getResources().getString(R.string.title_chat_fragment));
                removingHomeButton();*/

                isAddedBackStack = true;
                break;
            }

            case DATABASE: {
                frag = new DatabaseFragment();
                settingTitle(getResources().getString(R.string.title_database_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case NEWS: {
                frag = new NewsFargment();
                settingTitle(getResources().getString(R.string.title_news_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case REVIEW: {
                frag = new ReviewFragment();
                settingTitle(getResources().getString(R.string.title_review_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case CONTACTUS: {
                frag = new ContactUsFragment();
                settingTitle(getResources().getString(R.string.title_contact_us_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case SETTINGS: {
                frag = new SettingsFragment();
                settingTitle(getResources().getString(R.string.title_settings_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }

            case JOBS: {
                frag = new JobsFragment();
                settingTitle(getResources().getString(R.string.title_jobs_fragment));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }


            case LOGOUT: {
                alert(currentActivity, getResources().getString(R.string.alert_message_logout), getResources().getString(R.string.alert_message_logout), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_LOGOUT);


            }

        }

        if (frag != null) {
            switchContent(frag, false, isReplace, frag.getClass().getName());
        }
    }


    @Override
    protected void initContext() {
        currentActivity = Dashboard.this;
        context = Dashboard.this;
    }

    @Override
    protected void initListners() {

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);

    }


    @Override
    public void onBackPressed() {
        if (frag.getTag() == AppConstants.FRAGMENT_CHAT) {
            ((ChatFragment) frag).backButtonPressed();
        } else {
            if (isExitable) {
                super.onBackPressed();
            } else {
                toast(getResources().getString(R.string.message_app_exit), true);
                isExitable = true;


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitable = false;
                    }
                }, APP_EXIT_TIME);
            }
        }


    }


    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }


    @Override
    public void onAlertClicked(int alertType) {

        if (alertType == ALERT_TYPE_LOGOUT) {
            logout();
        }
    }


    public void logout() {

        QuickbloxLoginUtils.getInstance(currentActivity).chatLogOut();

        SharedPreferenceUtils.getInstance(currentActivity).clearALl();
        startActivity(currentActivity, AppAccessActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
        finish();

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tag == AppConstants.FRAGMENT_CHAT) {
            ((ChatFragment) frag).toRemoveListner();
        }

    }


    private void isUserVerified() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonIsUserVerifiedRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonIsUserVerifiedRequest = new JSONObject();
            jsonIsUserVerifiedRequest.put(com.mediax.mediaxapp.constant.AppConstants.EMAIL, SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
            Log.e("json is user verifi re", jsonIsUserVerifiedRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_IS_USER_VERIFIED = IS_USER_VERIFIED_URL;
        JsonObjectRequest isUSerVerifiedRequest = new JsonObjectRequest(Request.Method.POST, URL_IS_USER_VERIFIED, jsonIsUserVerifiedRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_is_user_verified), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);


                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.nwk_response_is_user_verified), getResources().getString(R.string.nwk_response_is_user_verified), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(IS_USER_VALID, false);

                    } else {
                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(IS_USER_VALID, true);

                        openingChat();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_edit_person), true);
                logTesting(getResources().getString(R.string.nwk_error_edit_person), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(isUSerVerifiedRequest);
    }


    private void openingChat() {
        if (chatFragment == null) {
            chatFragment = new ChatFragment();

        } else {
            //   isReplace=true;
        }


        frag = chatFragment;
        settingTitle(getResources().getString(R.string.title_chat_fragment));
        removingHomeButton();
        if (frag != null) {
            switchContent(frag, false, false, frag.getClass().getName());
        }
    }


    public void startFullImageActivity(Bundle bundle) {
        startActivity(currentActivity, FullImageViewActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("come under re", "quest per result yes");
        switch (requestCode) {
            case MY_READ_PHONE_STATE_REQUEST: {
                Log.e("come to subscribe push", " yes");
                playServicesHelper.subscribeToPushNotifications(playServicesHelper.regId);
                break;
            }
        }
    }

}
