package com.mediax.mediaxapp.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.AppAccessActivity;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.UserDetailActivity;
import com.mediax.mediaxapp.model.DatabaseModel;
import com.mediax.mediaxapp.model.UserModel;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;

import java.util.List;

/**
 * Created by Mayank on 28/04/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements AppConstants {

    View view;
    Activity currentActivity;
    List<UserModel> userModelList;

    Bundle bundle;

    public UserAdapter(Activity currentActivity, List<UserModel> userModelList) {
        this.currentActivity = currentActivity;
        this.userModelList = userModelList;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(currentActivity).inflate(R.layout.items_database, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textName.setText(userModelList.get(pos).getName());
        holder.textCompany.setText(userModelList.get(pos).getCompName());
        holder.containerUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putParcelable(com.mediax.mediaxapp.constant.AppConstants.KEY_USER_DETAILS, userModelList.get(pos));
                ((BaseActivity) currentActivity).startActivity(currentActivity, UserDetailActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, com.mediax.mediaxapp.constant.AppConstants.ANIMATION_SLIDE_UP);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textCompany;
        LinearLayout containerUsers;

        public ViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.textName);
            textCompany = (TextView) itemView.findViewById(R.id.textCompany);
            containerUsers = (LinearLayout) itemView.findViewById(R.id.containerUsers);
        }
    }
}
