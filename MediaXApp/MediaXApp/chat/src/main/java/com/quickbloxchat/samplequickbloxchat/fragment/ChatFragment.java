package com.quickbloxchat.samplequickbloxchat.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.auth.QBAuth;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.activity.BaseActivity;
import com.quickbloxchat.samplequickbloxchat.activity.ChatActivity;
import com.quickbloxchat.samplequickbloxchat.activity.DialogDetailsActivity;
import com.quickbloxchat.samplequickbloxchat.activity.DialogsActivity;
import com.quickbloxchat.samplequickbloxchat.adapter.ChatAdapter;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.core.Chat;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;
import com.quickbloxchat.samplequickbloxchat.core.GroupChatImpl;
import com.quickbloxchat.samplequickbloxchat.core.PrivateChatImpl;
import com.quickbloxchat.samplequickbloxchat.utils.FileUtils;
import com.quickbloxchat.samplequickbloxchat.utils.Helper;
import com.quickbloxchat.samplequickbloxchat.utils.SharedPreferenceUtils;
import com.quickbloxchat.samplequickbloxchat.utils.TimeUtils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pushnotifications.PlayServicesHelper;
import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.analytics.AnalyticsManager;
import vc908.stickerfactory.ui.OnEmojiBackspaceClickListener;
import vc908.stickerfactory.ui.OnStickerSelectedListener;
import vc908.stickerfactory.ui.fragment.StickersFragment;
import vc908.stickerfactory.ui.view.KeyboardHandleRelativeLayout;

public class ChatFragment extends BaseFragment implements KeyboardHandleRelativeLayout.KeyboardSizeChangeListener, AppConstants {


    private static final String TAG = "hi";

    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";


    private EditText messageEditText;
    private RecyclerView recyclerMessages;
    private Button sendButton;
    private ProgressBar progressBar;
    private ChatAdapter adapter;

    private Chat chat;
    private QBDialog dialog;
    private KeyboardHandleRelativeLayout keyboardHandleLayout;
    private View stickersFrame;
    private boolean isStickersFrameVisible;
    private ImageView stickerButton;
    private RelativeLayout container;

    private LinearLayoutManager managerMessages;

    private Activity currentActivity;
    private List<QBChatMessage> chatMessages;


    PlayServicesHelper playServicesHelper;

    ImageView attachButton;


    View view;

    protected static final int GALLERY_PICTURE = 1;
    File output;
    protected int CAMERA_REQUEST = 0;
    protected int PICKFILE_RESULT_CODE = 2;
    protected int PICKFILE_RESULT_CODE_WORD = 3;

    File cameraImageFile;

    String cameraImageFilePath;

    protected static final int PICK_CONTACT = 0x020;

    JSONObject objectPhoneNumber;

    String given;

    String family;

    boolean isLogoutChat = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("create view called", "yes");
        view = inflater.inflate(R.layout.activity_chat, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("activity created called", "yes");

        super.onActivityCreated(savedInstanceState);
/*

        playServicesHelper = new PlayServicesHelper(getActivity());

        currentActivity = getActivity();
        loginUserToChat();*/


    }


/*    @Override
    public void onDestroy() {

        Log.e("destroy called", "yes");
        ChatService.getInstance().removeConnectionListener(chatConnectionListener);
        super.onDestroy();
    }*/


    @Override
    public void onResume() {
        super.onResume();

        if (playServicesHelper == null) {
            playServicesHelper = new PlayServicesHelper(getActivity());
            playServicesHelper.checkPlayServices();
        }


        currentActivity = getActivity();


        if (isLogoutChat) {
            loginUserToChat();
        }


    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public void alertOkClicked() {

    }

    @Override
    public void initViews() {

        Log.e("view caled", "yes");
        recyclerMessages = (RecyclerView) view.findViewById(R.id.recyclerMessages);
        messageEditText = (EditText) view.findViewById(R.id.messageEdit);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        attachButton = (ImageView) view.findViewById(R.id.attachButton);


        // Setup opponents info
        //

        toGetDialog();


    }

    private void initDialog() {
        Log.e("init dialog caled", "yes");
        TextView companionLabel = (TextView) view.findViewById(R.id.companionLabel);
//        dialog = (QBDialog) getArguments().getSerializable(EXTRA_DIALOG);
    /*    Intent intent = getIntent();
        dialog = (QBDialog) intent.getSerializableExtra(EXTRA_DIALOG);*/
        container = (RelativeLayout) view.findViewById(R.id.container);
        if (dialog.getType() == QBDialogType.GROUP) {
            TextView meLabel = (TextView) view.findViewById(R.id.meLabel);
            container.removeView(meLabel);
            container.removeView(companionLabel);
        } else if (dialog.getType() == QBDialogType.PRIVATE) {
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);
            companionLabel.setText(ChatService.getInstance().getDialogsUsers().get(opponentID).getLogin());
        }

        // Send button
        //
        sendButton = (Button) view.findViewById(R.id.chatSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                sendChatMessage(messageText);

            }
        });

        // Stickers
        keyboardHandleLayout = (KeyboardHandleRelativeLayout) view.findViewById(R.id.sizeNotifierLayout);
        keyboardHandleLayout.setKeyboardSizeChangeListener(this);
        stickersFrame = view.findViewById(R.id.frame);
        stickerButton = (ImageView) view.findViewById(R.id.stickers_button);

/*
        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sticker clicked", "yes");
                if (isStickersFrameVisible) {
                    Log.e("sticker frame visible", "yes");
                    showKeyboard();
                    setStickersFrameVisible(false);
                    stickerButton.setImageResource(R.drawable.ic_action_insert_emoticon);
                } else {
                    Log.e("sticker frame visible", "no");
                    if (keyboardHandleLayout.isKeyboardVisible()) {
                        Log.e("keyboard  visible", "yes");
                        keyboardHandleLayout.hideKeyboard(currentActivity, new KeyboardHandleRelativeLayout.KeyboardHideCallback() {
                            @Override
                            public void onKeyboardHide() {
                                Log.e("keyboard  hide", "yes");
                                stickerButton.setImageResource(R.drawable.ic_action_keyboard);
                                setStickersFrameVisible(true);
                            }
                        });
                    } else {
                        Log.e("keyboard  visible", "no");
                        stickerButton.setImageResource(R.drawable.ic_action_keyboard);
                        setStickersFrameVisible(true);
                    }
                }
            }
        });
*/

   /*     updateStickersFrameParams();
        StickersFragment stickersFragment = (StickersFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame);
        if (stickersFragment == null) {
            stickersFragment = new StickersFragment();

        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, stickersFragment).commit();

        stickersFragment.setOnStickerSelectedListener(stickerSelectedListener);
        stickersFragment.setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
            @Override
            public void onEmojiBackspaceClicked() {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                messageEditText.dispatchKeyEvent(event);
            }
        });
        setStickersFrameVisible(isStickersFrameVisible);*/
        initChat();
    }

    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {
        attachButton.setOnClickListener(this);
    }

    private void showKeyboard() {
        ((InputMethodManager) messageEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendChatMessage(String messageText) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(messageText);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setProperty(PROPERTY_IS_CONTACT_SHARING, "NO");
        chatMessage.setProperty(PROPERTY_USER_NAME, SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME));
        chatMessage.setDateSent(new Date().getTime() / 1000);

        try {
            chat.sendMessage(chatMessage);
            AnalyticsManager.getInstance().onUserMessageSent(StickersManager.isSticker(messageText));
        } catch (XMPPException e) {
            Log.e(TAG, "failed to send a message", e);
        } catch (SmackException sme) {
            Log.e(TAG, "failed to send a message", sme);
        }

        messageEditText.setText("");

        if (dialog.getType() == QBDialogType.PRIVATE) {
            showMessage(chatMessage);
        }
    }


    private OnStickerSelectedListener stickerSelectedListener = new OnStickerSelectedListener() {
        @Override
        public void onStickerSelected(String code) {
            if (StickersManager.isSticker(code)) {
                sendChatMessage(code);
//                setStickersFrameVisible(false);
            } else {
                // append emoji to edit
                messageEditText.append(code);
            }
        }
    };

    @Override
    public void onKeyboardVisibilityChanged(boolean isVisible) {
      /*  if (isVisible) {
            setStickersFrameVisible(false);
            stickerButton.setImageResource(R.drawable.ic_action_insert_emoticon);
        } else {
            if (isStickersFrameVisible) {
                stickerButton.setImageResource(R.drawable.ic_action_keyboard);
            } else {
                stickerButton.setImageResource(R.drawable.ic_action_insert_emoticon);
            }
        }*/
    }

/*    private void setStickersFrameVisible(final boolean isVisible) {
        stickersFrame.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        isStickersFrameVisible = isVisible;
        if (stickersFrame.getHeight() != vc908.stickerfactory.utils.KeyboardUtils.getKeyboardHeight()) {
            updateStickersFrameParams();
        }
        final int padding = isVisible ? vc908.stickerfactory.utils.KeyboardUtils.getKeyboardHeight() : 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            keyboardHandleLayout.post(new Runnable() {
                @Override
                public void run() {
                    setContentBottomPadding(padding);
                    scrollDown();
                }
            });
        } else {
            setContentBottomPadding(padding);
        }
        scrollDown();
    }

    private void updateStickersFrameParams() {
        stickersFrame.getLayoutParams().height = vc908.stickerfactory.utils.KeyboardUtils.getKeyboardHeight();
    }*/

    public void setContentBottomPadding(int padding) {
        container.setPadding(0, 0, 0, padding);
    }

    private void initChat() {
        Log.e("init chat caled", "yes");
        if (dialog.getType() == QBDialogType.GROUP) {
            chat = new GroupChatImpl(currentActivity, this);

            // Join group chat
            //
            progressBar.setVisibility(View.VISIBLE);
            //
            joinGroupChat();

        } else if (dialog.getType() == QBDialogType.PRIVATE) {
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);

            chat = new PrivateChatImpl(currentActivity, this, opponentID);

            // Load CHat history
            //
            loadChatHistory();
        }
    }

    private void joinGroupChat() {
        Log.e("join group chat call", "yes");
        if (chat != null) {
            ((GroupChatImpl) chat).joinGroupChat(dialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void result, Bundle bundle) {
                    Log.e("join group success ", "yes");
                    // Load Chat history
                    //
                    loadChatHistory();
                }

                @Override
                public void onError(QBResponseException list) {
                    Log.e("join group error ", list.toString());
              /*      AlertDialog.Builder dialog = new AlertDialog.Builder(currentActivity);
                    dialog.setMessage("error when join group chat: " + list.toString()).create().show();*/
                }
            });
        }

    }

    private void loadChatHistory() {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setLimit(100);
        customObjectRequestBuilder.sortDesc("date_sent");

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {

                chatMessages = new ArrayList<QBChatMessage>();
                adapter = new ChatAdapter(currentActivity, chatMessages);

                LinearLayoutManager managerMessages = new LinearLayoutManager(currentActivity);

                recyclerMessages.setLayoutManager(managerMessages);
                recyclerMessages.setAdapter(adapter);

                for (int i = messages.size() - 1; i >= 0; --i) {
                    QBChatMessage msg = messages.get(i);
                    showMessage(msg);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException errors) {
                if (!currentActivity.isFinishing()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(currentActivity);
                    dialog.setMessage("load chat history errors: " + errors).create().show();
                }
            }
        });
    }

    public void showMessage(QBChatMessage message) {
        chatMessages.add(message);

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });
    }

    private void scrollDown() {
        if (adapter != null && adapter.chatMessages != null) {
            recyclerMessages.scrollToPosition(adapter.chatMessages.size() - 1);
        }

    }


    ConnectionListener chatConnectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.e(TAG, "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean authenticated) {
            Log.e(TAG, "authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.e(TAG, "connectionClosed");
        }

        @Override
        public void connectionClosedOnError(final Exception e) {
            Log.e(TAG, "connectionClosedOnError: " + e.getLocalizedMessage());

            // leave active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((GroupChatImpl) chat).leave();
                    }
                });
            }
        }

        @Override
        public void reconnectingIn(final int seconds) {
            if (seconds % 5 == 0) {
                Log.e(TAG, "reconnectingIn: " + seconds);
            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.e(TAG, "reconnectionSuccessful");

            // Join active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joinGroupChat();
                    }
                });
            }
        }

        @Override
        public void reconnectionFailed(final Exception error) {
            Log.e(TAG, "reconnectionFailed: " + error.getLocalizedMessage());
        }
    };


    //
    // ApplicationSessionStateCallback
    //

    @Override
    public void onStartSessionRecreation() {

    }

    @Override
    public void onFinishSessionRecreation(final boolean success) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    initChat();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.attachButton) {
            openChooser();
            // sendMessageWithAttachment();
        }


    }


    public void toGetDialog() {
        Log.e("get dialog called", "  yes");
        final QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(100);
        //  requestBuilder.gt(DIALOG_NAME, SharedPreferenceUtils.getInstance(currentActivity).getString());
        class GetDialogs extends AsyncTask<String, ArrayList<QBDialog>, ArrayList<QBDialog>> {

            @Override
            protected ArrayList<QBDialog> doInBackground(String... strings) {
                ArrayList<QBDialog> dialogs = null;
                try {
                    dialogs = QBChatService.getChatDialogs(null, requestBuilder, new Bundle());
                } catch (QBResponseException e) {
                    Log.e("dialog error ", e.toString());
                    e.printStackTrace();
                }
                return dialogs;
            }

            @Override
            protected void onPostExecute(ArrayList<QBDialog> dialogs) {
                if (dialogs != null) {
                    Log.e("total Dialog", " " + dialogs.size());
                    if (dialogs.size() > 0) {
                        dialog = dialogs.get(0);
                        if (isAdded()) {
                            initDialog();
                        }

                    }
                } else {
                    Log.e("dialog error ", "yes");
                }
                super.onPostExecute(dialogs);
            }
        }

        new GetDialogs().execute();

    }

    public void toRemoveListner() {

        QBChatService.getInstance().removeConnectionListener(chatConnectionListener);
    }


    public void backButtonPressed() {
        if (isStickersFrameVisible) {
            //setStickersFrameVisible(false);
            stickerButton.setImageResource(com.quickbloxchat.samplequickbloxchat.R.drawable.ic_action_insert_emoticon);
        } else {
            try {
                chat.release();
                Log.e(TAG, "chat relaese");
            } catch (XMPPException e) {
                Log.e(TAG, "failed to release chat", e);
            }

/*
            Intent i = new Intent(currentActivity, DialogsActivity.class);
            startActivity(i);
            finish();*/
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        logoutChatinBackground();
    }

    public void loginUserToChat() {


        final QBUser user = new QBUser();

        class LoginChat extends AsyncTask<String, Boolean, Boolean> {

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    QBChatService.getInstance().login(user);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                } catch (SmackException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                QBChatService.getInstance().addConnectionListener(chatConnectionListener);
                if (aBoolean) {
                    Log.e("chat login", "successfull");
                    initViews();
                    initContext();
                    initListners();
                } else {
                    Log.e("chat login", "error");
                }

            }
        }


        if (QBChatService.getInstance().isLoggedIn()) {
            Log.e("chat alreay login", "yes");
            initViews();
            initContext();
            initListners();
        } else {

            user.setLogin(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
            user.setPassword(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
            user.setId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(QUICKBLOX_USER_ID));
            Log.e("come under", "login chat");
            Log.e("user user id", "" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(QUICKBLOX_USER_ID));


            new LoginChat().execute();

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        isLogoutChat = true;
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            Log.e("come under camera resu", "yes");
//            Uri filePath = data.getData();
            //   Log.e("file path", filePath.getPath());
            File resizedFile = FileUtils.resizeImage(cameraImageFilePath);
            sendMessageWithAttachment(resizedFile);

            //   sendMessageWithAttachment(cameraImageFile);

        } else if (requestCode == GALLERY_PICTURE && resultCode == getActivity().RESULT_OK) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Log.e("file name", filePath.getLastPathSegment());
                sendMessageWithAttachment(FileUtils.createFile(currentActivity, bitmapImage, Helper.generateRandomNoTimeMillis() + "" + ".jpeg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_CONTACT) {
            String cNumber = null;
            String contactName = null;
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {


                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getActivity().getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        cNumber = phones.getString(phones.getColumnIndex("data1"));

                        int columnIndexForId = c.getColumnIndex(ContactsContract.Contacts._ID);
                        String contactId = c.getString(columnIndexForId);


                        getAllNumbers(contactId);

                        Log.e("number is:", cNumber);

                        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + contactId;
                        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                        Cursor nameCur = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                        while (nameCur.moveToNext()) {
                            given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                            family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                            String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                            Log.e("given name", " " + given);
                            Log.e("family name", " " + family);
                            Log.e("display name", " " + display);
                        }


                    }
                    contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String primaryName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                    Log.e("name is:", contactName);
                    Log.e("primary name is:", " " + primaryName);

                }
                //    c.close();
            }

            sendContact(given, family, objectPhoneNumber, cNumber);
        }

    }


    private void getAllNumbers(String contactId) {
        objectPhoneNumber = new JSONObject();
        Cursor pCur = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
        while (pCur.moveToNext()) {
            String phoneType = "";
            int type = pCur.getInt(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE));
            switch (type) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    phoneType = "Home";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    phoneType = "Mobile";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    phoneType = "Work";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    phoneType = "Home Fax";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    phoneType = "Work Fax";
                    break;
                default:
                    phoneType = "Other";
                    break;
            }
            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            try {
                objectPhoneNumber.put(phoneType, phoneNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("contact no all", phoneType + phoneNo);
        }
    }


/*    private void openChooser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(picDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (picDialogItems[item].equals(picDialogItems[1])) {
                    Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pictureActionIntent.setType("image*//*");
                    pictureActionIntent.putExtra("return-data", true);
                    startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                } else if (picDialogItems[item].equals(picDialogItems[0])) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    cameraImageFilePath =
                            Environment.getExternalStorageDirectory() + "/" + Helper.generateRandomNoTimeMillis() + ".jpeg";
                    cameraImageFile = new File(cameraImageFilePath);
                    Uri output = Uri.fromFile(cameraImageFile);
                  *//*  output = new File(cameraImageFile, String.valueOf(Helper.generateRandomNoTimeMillis()).subSequence(0, 10) + ".jpeg");*//*
                    i.putExtra(MediaStore.EXTRA_OUTPUT, output);

                    startActivityForResult(i, CAMERA_REQUEST);
                } else if (picDialogItems[item].equals(picDialogItems[2])) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });
        builder.show();
    }*/


    private void openChooser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(picDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (picDialogItems[item].equals(picDialogItems[1])) {

                    toCheckImagePermissions();
                } else if (picDialogItems[item].equals(picDialogItems[0])) {
                    toCheckCameraPermissions();
                } else if (picDialogItems[item].equals(picDialogItems[2])) {
                    toCheckReadContactPermissions();
                }
            }
        });
        builder.show();
    }

    private void sendMessageWithAttachment(File filePhoto) {

        Boolean fileIsPublic = true;
        QBContent.uploadFileTask(filePhoto, fileIsPublic, null, new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile file, Bundle params) {
                Log.e("successfully uploa im", "yes");
                // create a message
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setProperty("save_to_history", "1"); // Save a message to history
                chatMessage.setProperty(PROPERTY_USER_NAME, SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME));
                chatMessage.setDateSent(new Date().getTime() / 1000);
                // attach a photo
                QBAttachment attachment = new QBAttachment("photo");
                attachment.setId(file.getId().toString());
                attachment.setUrl(file.getPublicUrl());
                attachment.setName("hello new photo");
                chatMessage.addAttachment(attachment);

                try {
                    chat.sendMessage(chatMessage);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                // send a message
                // ...
            }

            @Override
            public void onError(QBResponseException errors) {
                // error
                Log.e("error uploa im", errors.toString());
            }
        });
    }


    private void sendContact(String firstName, String lastName, JSONObject objectPhoneNumber, String number) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(firstName + number);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setProperty(PROPERTY_IS_CONTACT_SHARING, "YES");
        chatMessage.setProperty(GIVEN_NAME, firstName);
        chatMessage.setProperty(FAMILY_NAME, lastName);
        chatMessage.setProperty(PHONE_NUMBER_JSON, objectPhoneNumber.toString());


        chatMessage.setProperty(PROPERTY_USER_NAME, SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME));
        chatMessage.setDateSent(new Date().getTime() / 1000);

        try {
            chat.sendMessage(chatMessage);
            AnalyticsManager.getInstance().onUserMessageSent(StickersManager.isSticker(GIVEN_NAME + FAMILY_NAME + number));
        } catch (XMPPException e) {
            Log.e(TAG, "failed to send a message", e);
        } catch (SmackException sme) {
            Log.e(TAG, "failed to send a message", sme);
        }

        messageEditText.setText("");

        if (dialog.getType() == QBDialogType.PRIVATE) {
            showMessage(chatMessage);
        }
    }


    private void logoutChatinBackground() {
        Log.e(TAG, "ome under logout chat");
        boolean isLoggedIn = QBChatService.getInstance().isLoggedIn();
        if (!isLoggedIn) {
            return;
        }

        if (!isLogoutChat) {
            return;
        }

        QBChatService.getInstance().logout(new QBEntityCallback() {


            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Log.e(TAG, "logout success");
                QBChatService.getInstance().destroy();
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e(TAG, "logout error" + errors.toString());
            }
        });
    }


    private void toCheckImagePermissions() {
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Log.e("to Show Read Permission", "yes");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_TAG_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            isLogoutChat = false;
            Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pictureActionIntent.setType("image/*");
            pictureActionIntent.putExtra("return-data", true);
            startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
        }
    }

    private void toCheckCameraPermissions() {
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Log.e("to Show Read Permission", "yes");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_TAG_ACCESS_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            isLogoutChat = false;
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraImageFilePath =
                    Environment.getExternalStorageDirectory() + "/" + Helper.generateRandomNoTimeMillis() + ".jpeg";
            cameraImageFile = new File(cameraImageFilePath);
            Uri output = Uri.fromFile(cameraImageFile);
                  /*  output = new File(cameraImageFile, String.valueOf(Helper.generateRandomNoTimeMillis()).subSequence(0, 10) + ".jpeg");*/
            i.putExtra(MediaStore.EXTRA_OUTPUT, output);

            startActivityForResult(i, CAMERA_REQUEST);
        }
    }

    private void toCheckReadContactPermissions() {
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Log.e("to Show Read Permission", "yes");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_TAG_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            isLogoutChat = false;
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
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


            case REQUEST_TAG_ACCESS_CAMERA: {
                isLogoutChat = false;
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraImageFilePath =
                        Environment.getExternalStorageDirectory() + "/" + Helper.generateRandomNoTimeMillis() + ".jpeg";
                cameraImageFile = new File(cameraImageFilePath);
                Uri output = Uri.fromFile(cameraImageFile);
                  /*  output = new File(cameraImageFile, String.valueOf(Helper.generateRandomNoTimeMillis()).subSequence(0, 10) + ".jpeg");*/
                i.putExtra(MediaStore.EXTRA_OUTPUT, output);

                startActivityForResult(i, CAMERA_REQUEST);
                break;
            }

            case REQUEST_TAG_READ_EXTERNAL_STORAGE: {
                isLogoutChat = false;
                Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pictureActionIntent.setType("image/*");
                pictureActionIntent.putExtra("return-data", true);
                startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                break;
            }

            case REQUEST_TAG_READ_CONTACTS: {
                isLogoutChat = false;
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                break;
            }

            case REQUEST_TAG_WRITE_CONTACTS: {

                adapter.alertAddContact(currentActivity, currentActivity.getResources().getString(R.string.messgaeAddContact), currentActivity.getResources().getString(R.string.messgaeAddContact), currentActivity.getResources().getString(R.string.ok), currentActivity.getResources().getString(R.string.cancel), true, false, ALERT_TYPE_CONTACT);

                break;
            }


        }
    }


}
