package com.quickbloxchat.samplequickbloxchat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.adapter.ChatAdapter;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.core.Chat;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;
import com.quickbloxchat.samplequickbloxchat.core.GroupChatImpl;
import com.quickbloxchat.samplequickbloxchat.core.PrivateChatImpl;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.analytics.AnalyticsManager;
import vc908.stickerfactory.ui.OnEmojiBackspaceClickListener;
import vc908.stickerfactory.ui.OnStickerSelectedListener;
import vc908.stickerfactory.ui.fragment.StickersFragment;
import vc908.stickerfactory.ui.view.KeyboardHandleRelativeLayout;

public class ChatActivity extends BaseActivity implements KeyboardHandleRelativeLayout.KeyboardSizeChangeListener, AppConstants {


    private static final String TAG = ChatActivity.class.getSimpleName();

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

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        initViews();
        //   addingActionTitleListner();


        // Init chat if the session is active
        //
        if (isSessionActive()) {
            initChat();
        }

        ChatService.getInstance().addConnectionListener(chatConnectionListener);
    }

    private void addingActionTitleListner() {

        View view = findViewById(android.R.id.title);
        final int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
        findViewById(abTitleId).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bundle == null) {
                    bundle = new Bundle();
                }

                bundle.putSerializable(EXTRA_DIALOG, dialog);

                startActivity(currentActivity, DialogDetailsActivity.class, bundle, true, AppConstants.REQUEST_TAG_NO_RESULT, true, AppConstants.animation[0]);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ChatService.getInstance().removeConnectionListener(chatConnectionListener);
    }

    @Override
    public void onBackPressed() {
        if (isStickersFrameVisible) {
            setStickersFrameVisible(false);
            stickerButton.setImageResource(R.drawable.ic_action_insert_emoticon);
        } else {
            try {
                chat.release();
            } catch (XMPPException e) {
                Log.e(TAG, "failed to release chat", e);
            }
            super.onBackPressed();

            Intent i = new Intent(ChatActivity.this, DialogsActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void initViews() {
        recyclerMessages = (RecyclerView) findViewById(R.id.recyclerMessages);
        messageEditText = (EditText) findViewById(R.id.messageEdit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView companionLabel = (TextView) findViewById(R.id.companionLabel);

        // Setup opponents info
        //
        Intent intent = getIntent();
        dialog = (QBDialog) intent.getSerializableExtra(EXTRA_DIALOG);
        container = (RelativeLayout) findViewById(R.id.container);
        if (dialog.getType() == QBDialogType.GROUP) {
            TextView meLabel = (TextView) findViewById(R.id.meLabel);
            container.removeView(meLabel);
            container.removeView(companionLabel);
        } else if (dialog.getType() == QBDialogType.PRIVATE) {
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);
            companionLabel.setText(ChatService.getInstance().getDialogsUsers().get(opponentID).getLogin());
        }

        // Send button
        //
        sendButton = (Button) findViewById(R.id.chatSendButton);
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
        keyboardHandleLayout = (KeyboardHandleRelativeLayout) findViewById(R.id.sizeNotifierLayout);
        keyboardHandleLayout.setKeyboardSizeChangeListener(this);
        stickersFrame = findViewById(R.id.frame);
        stickerButton = (ImageView) findViewById(R.id.stickers_button);

        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
          /*      if (isStickersFrameVisible) {
                    showKeyboard();
                    stickerButton.setImageResource(R.drawable.ic_action_insert_emoticon);
                } else {
                    if (keyboardHandleLayout.isKeyboardVisible()) {
                        keyboardHandleLayout.hideKeyboard(ChatActivity.this, new KeyboardHandleRelativeLayout.KeyboardHideCallback() {
                            @Override
                            public void onKeyboardHide() {
                                stickerButton.setImageResource(R.drawable.ic_action_keyboard);
                                setStickersFrameVisible(true);
                            }
                        });
                    } else {
                        stickerButton.setImageResource(R.drawable.ic_action_keyboard);
                        setStickersFrameVisible(true);
                    }
                }*/
            }
        });

      /*  updateStickersFrameParams();
        StickersFragment stickersFragment = (StickersFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
        if (stickersFragment == null) {
            stickersFragment = new StickersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, stickersFragment).commit();
        }
        stickersFragment.setOnStickerSelectedListener(stickerSelectedListener);
        stickersFragment.setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
            @Override
            public void onEmojiBackspaceClicked() {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                messageEditText.dispatchKeyEvent(event);
            }
        });*/
    //    setStickersFrameVisible(isStickersFrameVisible);
    }

    private void showKeyboard() {
        ((InputMethodManager) messageEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendChatMessage(String messageText) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(messageText);
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setProperty(PROPERTY_USER_NAME, CHAT_USERNAME);
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
     /*   if (isVisible) {
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

    private void setStickersFrameVisible(final boolean isVisible) {
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
    }

    public void setContentBottomPadding(int padding) {
        container.setPadding(0, 0, 0, padding);
    }

    private void initChat() {

        if (dialog.getType() == QBDialogType.GROUP) {
       //     chat = new GroupChatImpl(currentActivity,this);

            // Join group chat
            //
            progressBar.setVisibility(View.VISIBLE);
            //
            joinGroupChat();

        } else if (dialog.getType() == QBDialogType.PRIVATE) {
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);

      //      chat = new PrivateChatImpl(this, opponentID);

            // Load CHat history
            //
            loadChatHistory();
        }
    }

    private void joinGroupChat() {
        ((GroupChatImpl) chat).joinGroupChat(dialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {

                // Load Chat history
                //
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException list) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                dialog.setMessage("error when join group chat: " + list.toString()).create().show();
            }
        });
    }

    private void loadChatHistory() {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setLimit(100);
        customObjectRequestBuilder.sortDesc("date_sent");

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {

                chatMessages = new ArrayList<QBChatMessage>();
                adapter = new ChatAdapter(ChatActivity.this, chatMessages);

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
                if (!ChatActivity.this.isFinishing()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                    dialog.setMessage("load chat history errors: " + errors).create().show();
                }
            }
        });
    }

    public void showMessage(QBChatMessage message) {
        chatMessages.add(message);

        runOnUiThread(new Runnable() {
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
            Log.i(TAG, "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean authenticated) {
            Log.i(TAG, "authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.i(TAG, "connectionClosed");
        }

        @Override
        public void connectionClosedOnError(final Exception e) {
            Log.i(TAG, "connectionClosedOnError: " + e.getLocalizedMessage());

            // leave active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                ChatActivity.this.runOnUiThread(new Runnable() {
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
                Log.i(TAG, "reconnectingIn: " + seconds);
            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.i(TAG, "reconnectionSuccessful");

            // Join active room
            //
            if (dialog.getType() == QBDialogType.GROUP) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joinGroupChat();
                    }
                });
            }
        }

        @Override
        public void reconnectionFailed(final Exception error) {
            Log.i(TAG, "reconnectionFailed: " + error.getLocalizedMessage());
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    initChat();
                }
            }
        });
    }

}
