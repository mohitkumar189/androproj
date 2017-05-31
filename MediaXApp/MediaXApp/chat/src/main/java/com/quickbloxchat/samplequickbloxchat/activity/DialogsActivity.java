package com.quickbloxchat.samplequickbloxchat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.adapter.DialogsAdapter;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;

import java.util.ArrayList;
import java.util.List;

import pushnotifications.Consts;
import pushnotifications.PlayServicesHelper;

public class DialogsActivity extends BaseActivity implements AppConstants {

    private static final String TAG = DialogsActivity.class.getSimpleName();

    private RecyclerView recyclerRoomsList;
    private ProgressBar progressBar;

    private PlayServicesHelper playServicesHelper;

    LinearLayoutManager managerDialogs;
    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        currentActivity = DialogsActivity.this;

        playServicesHelper = new PlayServicesHelper(this);

        recyclerRoomsList = (RecyclerView) findViewById(R.id.recyclerRoomsList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Register to receive push notifications events
        //
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushReceiver,
                new IntentFilter(Consts.NEW_PUSH_EVENT));

        // Get dialogs if the session is active
        //
        if (isSessionActive()) {
            getDialogs();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playServicesHelper.checkPlayServices();
    }

    private void getDialogs() {
        progressBar.setVisibility(View.VISIBLE);

        // Get dialogs
        //
        ChatService.getInstance().getDialogs(new QBEntityCallback<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle bundle) {
                progressBar.setVisibility(View.GONE);

                // build list view
                //
                buildListView(dialogs);
            }

            @Override
            public void onError(QBResponseException errors) {
                progressBar.setVisibility(View.GONE);

                AlertDialog.Builder dialog = new AlertDialog.Builder(DialogsActivity.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
            }
        });
    }

    void buildListView(List<QBDialog> dialogs) {
        final DialogsAdapter adapter = new DialogsAdapter(dialogs, DialogsActivity.this);
        Log.e("dialog size", "" + dialogs.size());
        managerDialogs = new LinearLayoutManager(currentActivity);
        recyclerRoomsList.setLayoutManager(managerDialogs);
        recyclerRoomsList.setAdapter(adapter);

        // choose dialog
        //

    }

    private BroadcastReceiver mPushReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            String message = intent.getStringExtra(Consts.EXTRA_MESSAGE);

            Log.i(TAG, "Receiving event " + Consts.NEW_PUSH_EVENT + " with data: " + message);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_READ_PHONE_STATE_REQUEST: {
                playServicesHelper.subscribeToPushNotifications(playServicesHelper.regId);
                break;
            }
        }
    }
}
