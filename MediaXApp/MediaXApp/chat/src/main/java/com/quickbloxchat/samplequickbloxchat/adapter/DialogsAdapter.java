package com.quickbloxchat.samplequickbloxchat.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.users.model.QBUser;
import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.activity.ChatActivity;
import com.quickbloxchat.samplequickbloxchat.activity.DialogsActivity;
import com.quickbloxchat.samplequickbloxchat.core.ChatService;

import org.w3c.dom.Text;

import java.util.List;

import vc908.stickerfactory.StickersManager;

/**
 * Created by Mayank on 08/05/2016.
 */
public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {


    private List<QBDialog> dataSource;
    private LayoutInflater inflater;
    Activity currentActivity;

    public DialogsAdapter(List<QBDialog> dataSource, Activity ctx) {
        this.dataSource = dataSource;
        this.inflater = LayoutInflater.from(ctx);
        this.currentActivity = ctx;

    }

    @Override
    public DialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(currentActivity).inflate(R.layout.list_item_room, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DialogsAdapter.ViewHolder holder, int position) {

        final int pos = position;
        QBDialog dialog = dataSource.get(position);
        dialog.getOccupants();

        if (dialog.getType().equals(QBDialogType.GROUP)) {
            holder.textRoomName.setText(dialog.getName());
        } else {
            // get opponent name for private dialog
            //
            Integer opponentID = ChatService.getInstance().getOpponentIDForPrivateDialog(dialog);
            QBUser user = ChatService.getInstance().getDialogsUsers().get(opponentID);
            if (user != null) {
                holder.textRoomName.setText(user.getLogin() == null ? user.getFullName() : user.getLogin());
            }
        }
        if (dialog.getLastMessage() != null && StickersManager.isSticker(dialog.getLastMessage())) {
            holder.textRoomLastMessage.setText("Sticker");
        } else {
            holder.textRoomLastMessage.setText(dialog.getLastMessage());
        }
        holder.textGroupType.setText(dialog.getType().toString());

        holder.containerItemRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBDialog selectedDialog = (QBDialog) dataSource.get(pos);

                Bundle bundle = new Bundle();
                bundle.putSerializable(ChatActivity.EXTRA_DIALOG, selectedDialog);

                // Open chat activity
                //
                ChatActivity.start(currentActivity, bundle);

                currentActivity.finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textRoomName;
        TextView textRoomLastMessage;
        TextView textGroupType;

        RelativeLayout containerItemRoom;

        public ViewHolder(View itemView) {
            super(itemView);

            textRoomName = (TextView) itemView.findViewById(R.id.textRoomName);
            textRoomLastMessage = (TextView) itemView.findViewById(R.id.textRoomLastMessage);
            textGroupType = (TextView) itemView.findViewById(R.id.textGroupType);
            containerItemRoom = (RelativeLayout) itemView.findViewById(R.id.containerItemRoom);
        }
    }
}
