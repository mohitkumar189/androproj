package com.quickbloxchat.samplequickbloxchat.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.model.QBFile;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.activity.BaseActivity;
import com.quickbloxchat.samplequickbloxchat.activity.FullImageViewActivity;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;
import com.quickbloxchat.samplequickbloxchat.utils.Helper;
import com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils;
import com.quickbloxchat.samplequickbloxchat.utils.TimeUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vc908.stickerfactory.StickersManager;

/**
 * Created by Mayank on 09/05/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements AppConstants {

    public final List<QBChatMessage> chatMessages;
    private Activity currentActivity;

    Bundle bundle;

    QBChatMessage qbChatMessageContact;

    private enum ChatItemType {
        Message,
        Sticker
    }

    public ChatAdapter(Activity currentActivity, List<QBChatMessage> chatMessages) {
        this.currentActivity = currentActivity;
        this.chatMessages = chatMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ChatItemType.Sticker.ordinal()) {
            view = LayoutInflater.from(currentActivity).inflate(R.layout.list_item_sticker, parent, false);
        } else {
            view = LayoutInflater.from(currentActivity).inflate(R.layout.list_item_message, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        QBChatMessage chatMessage = chatMessages.get(position);

        QBUser currentUser = ChatService.getInstance().getCurrentUser();
        boolean isOutgoing = chatMessage.getSenderId() == null || chatMessage.getSenderId().equals(currentUser.getId());
        setAlignment(holder, isOutgoing);

        if (chatMessage.getAttachments() != null && chatMessage.getAttachments().size() > 0) {
            showImage(holder, chatMessage);
            switchViewDependUponMessage(holder, IMAGE_MESSAGE);
        } else if (chatMessage.getProperty(PROPERTY_IS_CONTACT_SHARING).toString().equals("YES")) {
            showContact(holder, chatMessage);
            switchViewDependUponMessage(holder, CONTACT_MESSAGE);
        } else {
            switchViewDependUponMessage(holder, SIMPLE_MESSAGE);
            if (StickersManager.isSticker(chatMessage.getBody())) {
                StickersManager.with(currentActivity)
                        .loadSticker(chatMessage.getBody())
                        .into(holder.stickerView);
            } else if (holder.txtMessage != null) {
                holder.txtMessage.setText(chatMessage.getBody());
            }
            if (chatMessage.getSenderId() != null) {
                //  holder.txtInfo.setText(chatMessage.getSenderId() + ": " + getTimeText(chatMessage));

            } else {
                holder.txtInfo.setText(getTimeText(chatMessage));
            }
            if (chatMessage.getProperty(PROPERTY_USER_NAME) != null) {
                holder.txtInfo.setText(chatMessage.getProperty(PROPERTY_USER_NAME).toString());

            }
        }


    }


    private void showContact(ViewHolder holder, final QBChatMessage chatMessage)

    {
        holder.textContactName.setText(chatMessage.getProperty(GIVEN_NAME).toString() + chatMessage.getProperty(FAMILY_NAME).toString());

        if (chatMessage.getProperty(PROPERTY_USER_NAME).equals(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME))) {
            holder.buttonAddContact.setVisibility(View.VISIBLE);
            holder.buttonAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qbChatMessageContact=chatMessage;
                    toCheckWriteContactPermissions();

                }
            });
        } else {
            holder.buttonAddContact.setVisibility(View.GONE);
        }


    }

    public void alertAddContact(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                addContactWithMultipleNumber(qbChatMessageContact);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();


    }


/*    private void addContactToPhoneBook(QBChatMessage chatMessage) {
        JSONObject jsonPhoneNumber = null;
        try {
            jsonPhoneNumber = new JSONObject(chatMessage.getProperty(PHONE_NUMBER_JSON).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(Contacts.People.NUMBER, jsonPhoneNumber.getString());
        values.put(Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(Contacts.People.LABEL, chatMessage.getProperty(GIVEN_NAME).toString());
        values.put(Contacts.People.NAME, chatMessage.getProperty(GIVEN_NAME).toString() + " " + chatMessage.getProperty(FAMILY_NAME).toString());
        Uri dataUri = currentActivity.getContentResolver().insert(Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.TYPE_MOBILE);
        values.put(Contacts.People.NUMBER, phone);
        updateUri = currentActivity.getContentResolver().insert(updateUri, values);
    }*/


    private void addContactWithMultipleNumber(QBChatMessage chatMessage) {
        JSONObject jsonPhoneNumber = null;
        try {
            jsonPhoneNumber = new JSONObject(chatMessage.getProperty(PHONE_NUMBER_JSON).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data


        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, chatMessage.getProperty(GIVEN_NAME).toString() + " " + chatMessage.getProperty(FAMILY_NAME).toString())
                .build());

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data

        if (jsonPhoneNumber.has("Mobile")) {
            try {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, jsonPhoneNumber.getString("Mobile"))
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Adding insert operation to operations list
        // to  insert Home Phone Number in the table ContactsContract.Data

        if (jsonPhoneNumber.has("Home")) {
            try {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, jsonPhoneNumber.getString("Home"))
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (jsonPhoneNumber.has("Home Fax")) {
            try {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, jsonPhoneNumber.getString("Home Fax"))
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME)
                        .build());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (jsonPhoneNumber.has("Work Fax")) {
            try {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, jsonPhoneNumber.getString("Work Fax"))
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME)
                        .build());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

/*
        // Adding insert operation to operations list
        // to insert Home Email in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, etHomeEmail.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());

        // Adding insert operation to operations list
        // to insert Work Email in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, etWorkEmail.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());*/

        try {
            // Executing all the insert operations as a single database transaction
            currentActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(currentActivity, "Contact is successfully added", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    private void showImage(ViewHolder holder, QBChatMessage chatMessage) {
        for (final QBAttachment attachment : chatMessage.getAttachments()) {
            //  Integer fileId = Integer.parseInt(attachment.getId());

            String fileId = attachment.getId();


            Log.e("attachment url", " " + attachment.getUrl());


        /*    QBFile file = new QBFile();
            file.setId(Integer.valueOf(fileId));*/


           /* .resize(Helper.toPixels(currentActivity, 218), Helper.toPixels(currentActivity, 192)).centerCrop()*/

            Picasso.with(currentActivity).load(attachment.getUrl()).into(holder.imageChat, new Callback() {
                @Override
                public void onSuccess() {
                    Log.e("image load success", "yes");
                }

                @Override
                public void onError() {
                    Log.e("image load failed", "yes");
                }
            });


            holder.imageChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bundle == null) {
                        bundle = new Bundle();
                    }

                    bundle.putString(IMAGE_URL, attachment.getUrl());

                    Intent i = new Intent(currentActivity, FullImageViewActivity.class);
                    i.putExtras(bundle);
                    currentActivity.startActivity(i);


                }
            });

    /*        Picasso.with(currentActivity).load(attachment.getUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    int h = Helper.toPixels(currentActivity, 218); // height in pixels
                    int w = Helper.toPixels(currentActivity, 192); // width in pixels
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, w, h, true);

                    holder.imageChat.setImageBitmap(scaled);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.e("image load failed", "yes");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });*/

     /*       if (file.getPublicUrl()!=null)
            {

                String publicUrl = file.getPublicUrl();

                Log.e("attachment public url",  " "+  publicUrl);
                Picasso.with(currentActivity).load(publicUrl).into(holder.imageChat);
            }*/








         /*   // download a file
            QBContent.downloadFileTask(fileId, new QBEntityCallback<InputStream>() {
                @Override
                public void onSuccess(final InputStream inputStream, Bundle params) {
                    // process file

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap imageBitmap = FileUtils.inputStreamToByteArray(inputStream);

                            holder.imageChat.setImageBitmap(imageBitmap);
                        }
                    });
                    thread.start();


                }

                @Override
                public void onError(QBResponseException errors) {
                    // errors
                }
            });*/
        }
    }


   /* class ConvertToBitmap extends AsyncTask<InputStream, InputStream, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(InputStream... inputStreams) {
            Bitmap imageBitmap = FileUtils.inputStreamToByteArray(inputStreams[0]);
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            holder.imageChat.setImageBitmap(bitmap);
        }
    }
*/

    private void setAlignment(ViewHolder holder, boolean isOutgoing) {
        if (!isOutgoing) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
            if (holder.txtMessage != null) {
                holder.contentWithBG.setBackgroundResource(R.drawable.incoming_message_bg);
                //  holder.contentWithBG.setBackgroundResource(R.drawable.icon_chat_edittext_right);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.txtMessage.setLayoutParams(layoutParams);
            } else {
                holder.contentWithBG.setBackgroundResource(android.R.color.transparent);
            }
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);

            if (holder.txtMessage != null) {
                holder.contentWithBG.setBackgroundResource(R.drawable.outgoing_message_bg);
                //   holder.contentWithBG.setBackgroundResource(R.drawable.image_chat_edittext_left);
                layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
                layoutParams.gravity = Gravity.LEFT;
                holder.txtMessage.setLayoutParams(layoutParams);
            } else {
                holder.contentWithBG.setBackgroundResource(android.R.color.transparent);
            }
        }
    }


    private String getTimeText(QBChatMessage message) {
        return TimeUtils.millisToLongDHMS(message.getDateSent() * 1000);
    }


    @Override
    public int getItemCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return StickersManager.isSticker(chatMessages.get(position).getBody())
                ? ChatItemType.Sticker.ordinal()
                : ChatItemType.Message.ordinal();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMessage;
        public TextView txtInfo;
        public ImageView imageChat;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public ImageView stickerView;

        public LinearLayout containerContactSharing;
        public LinearLayout contentWithBackground;

        public TextView textContactName;
        public Button buttonAddContact;

        public ViewHolder(View itemView) {
            super(itemView);

            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            content = (LinearLayout) itemView.findViewById(R.id.content);
            contentWithBG = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
            txtInfo = (TextView) itemView.findViewById(R.id.txtInfo);
            stickerView = (ImageView) itemView.findViewById(R.id.sticker_image);
            imageChat = (ImageView) itemView.findViewById(R.id.imageChat);
            containerContactSharing = (LinearLayout) itemView.findViewById(R.id.containerContactSharing);
            contentWithBackground = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
            textContactName = (TextView) itemView.findViewById(R.id.textContactName);
            buttonAddContact = (Button) itemView.findViewById(R.id.buttonAddContact);
        }
    }


    private void switchViewDependUponMessage(ViewHolder holder, String type) {
        switch (type) {
            case SIMPLE_MESSAGE: {
                holder.imageChat.setVisibility(View.GONE);
                holder.txtMessage.setVisibility(View.VISIBLE);

                holder.contentWithBackground.setVisibility(View.VISIBLE);
                holder.containerContactSharing.setVisibility(View.GONE);
                holder.txtInfo.setVisibility(View.VISIBLE);
                break;
            }
            case IMAGE_MESSAGE: {
                holder.imageChat.setVisibility(View.VISIBLE);
                holder.txtMessage.setVisibility(View.GONE);

                holder.contentWithBackground.setVisibility(View.VISIBLE);
                holder.containerContactSharing.setVisibility(View.GONE);
                holder.txtInfo.setVisibility(View.VISIBLE);
                break;
            }
            case CONTACT_MESSAGE: {

                holder.contentWithBackground.setVisibility(View.GONE);
                holder.containerContactSharing.setVisibility(View.VISIBLE);
                holder.txtInfo.setVisibility(View.GONE);

                break;
            }
        }
    }



    private void toCheckWriteContactPermissions() {
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                    Manifest.permission.WRITE_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Log.e("to Show Read Permission", "yes");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        REQUEST_TAG_WRITE_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

        }
    }



}
