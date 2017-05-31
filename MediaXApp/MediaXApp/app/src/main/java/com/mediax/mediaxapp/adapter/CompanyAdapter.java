package com.mediax.mediaxapp.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.CompanyMembersActivity;
import com.mediax.mediaxapp.activity.UserDetailActivity;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.CompanyModel;

import java.util.List;

/**
 * Created by Mayank on 17/06/2016.
 */
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> implements AppConstants {

    Activity currentActivity;
    List<CompanyModel> companyModelList;

    Bundle bundle;

    View view;

    public CompanyAdapter(Activity currentActivity, List<CompanyModel> companyModelList) {
        this.currentActivity = currentActivity;
        this.companyModelList = companyModelList;
    }

    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(currentActivity).inflate(R.layout.item_company, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(CompanyAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textCompanyName.setText(companyModelList.get(pos).getName());

        holder.textCompanyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putParcelable(com.mediax.mediaxapp.constant.AppConstants.KEY_CompanyDetails, companyModelList.get(pos));
                ((BaseActivity) currentActivity).startActivity(currentActivity, CompanyMembersActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, com.mediax.mediaxapp.constant.AppConstants.ANIMATION_SLIDE_UP);

            }
        });
    }

    @Override
    public int getItemCount() {
        return companyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textCompanyName;

        public ViewHolder(View itemView) {
            super(itemView);
            textCompanyName = (TextView) itemView.findViewById(R.id.textCompanyName);

        }
    }
}
