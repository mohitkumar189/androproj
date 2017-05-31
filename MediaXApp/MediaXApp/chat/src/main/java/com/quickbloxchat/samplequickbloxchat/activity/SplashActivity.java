package com.quickbloxchat.samplequickbloxchat.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

public class SplashActivity extends AppCompatActivity implements AppConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        createSessionAndLogin();

    }


    public void createSessionAndLogin() {
        // Initialise Chat service


        final QBUser user = new QBUser();
        user.setLogin(CHAT_USERNAME);
        user.setPassword(CHAT_PASSWORD);

        ChatService.getInstance().login(user, new QBEntityCallback<Void>() {

            @Override
            public void onSuccess(Void result, Bundle bundle) {
                // Go to Dialogs screen
                //
                Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(QBResponseException errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });

    }


    private void addConnectionListner() {
        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {

            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {

            }


            @Override
            public void connectionClosed() {

            }

            @Override
            public void connectionClosedOnError(Exception e) {
                // connection closed on error. It will be established soon
            }

            @Override
            public void reconnectingIn(int seconds) {

            }

            @Override
            public void reconnectionSuccessful() {

            }

            @Override
            public void reconnectionFailed(Exception e) {

            }
        };

        QBChatService.getInstance().addConnectionListener(connectionListener);
    }
}
