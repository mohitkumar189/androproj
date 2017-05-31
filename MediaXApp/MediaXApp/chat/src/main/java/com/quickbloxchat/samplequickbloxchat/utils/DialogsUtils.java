package com.quickbloxchat.samplequickbloxchat.utils;

import android.os.Bundle;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.io.File;

/**
 * Created by Mayank on 09/05/2016.
 */
public class DialogsUtils {

    private static DialogsUtils dialogsUtils;

    public static DialogsUtils getInstance() {
        if (dialogsUtils == null) {
            dialogsUtils = new DialogsUtils();
        }
        return dialogsUtils;
    }

    private void uploadPhoto(final QBDialog qbDialog, File file) {
        File filePhoto = new File("dialog_avatar.png");
        Boolean fileIsPublic = false;
        QBContent.uploadFileTask(filePhoto, fileIsPublic, null, new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile file, Bundle params) {
                qbDialog.setPhoto(file.getId().toString());
            }

            @Override
            public void onError(QBResponseException errors) {
                // error
            }
        });
    }
}
