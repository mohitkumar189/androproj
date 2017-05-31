package com.quickbloxchat.samplequickbloxchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DialogDetailsActivity extends BaseActivity implements AppConstants {

    private final String TAG = DialogDetailsActivity.class.getName();

    QBDialog qbDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_details);
        qbDialog = (QBDialog) getIntent().getExtras().getSerializable(EXTRA_DIALOG);


        fetchGroupMemberDetails(qbDialog);


    }

    private void fetchGroupMemberDetails(QBDialog qbDialog) {
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(1);
        pagedRequestBuilder.setPerPage(50);


        QBUsers.getUsersByIDs(qbDialog.getOccupants()
                , pagedRequestBuilder, new QBEntityCallback<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                        for (QBUser qbUser : qbUsers) {

                            Log.e(TAG, "" + qbUser.getFullName());
                            Log.e(TAG, "" + qbUser.getLogin());
                        }

                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });

    }

}
