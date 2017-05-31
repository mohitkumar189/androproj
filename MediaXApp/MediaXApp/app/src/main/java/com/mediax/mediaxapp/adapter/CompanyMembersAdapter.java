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
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.UserDetailActivity;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.UserModel;

import java.util.List;

/**
 * Created by Mayank on 05/05/2016.
 */
public class CompanyMembersAdapter extends RecyclerView.Adapter<CompanyMembersAdapter.ViewHolder> implements AppConstants {

    Activity currentActivity;
    List<UserModel> userModelList;

    Bundle bundle;

    public CompanyMembersAdapter(Activity currentActivity, List<UserModel> userModelList) {
        this.currentActivity = currentActivity;
        this.userModelList = userModelList;
    }

    @Override
    public CompanyMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.item_company_members, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompanyMembersAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textName.setText(userModelList.get(pos).getName());
        holder.textDesignation.setText(userModelList.get(pos).getDesignaton());

        if (userModelList.get(pos).getDesignaton() == null) {
            holder.textDesignation.setText("-");
        }

        holder.containerCompanyMembers.setOnClickListener(new View.OnClickListener() {
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

        LinearLayout containerCompanyMembers;
        TextView textName;
        TextView textDesignation;

        public ViewHolder(View itemView) {
            super(itemView);
            containerCompanyMembers = (LinearLayout) itemView.findViewById(R.id.containerCompanyMembers);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textDesignation = (TextView) itemView.findViewById(R.id.textDesignation);
        }
    }
}
