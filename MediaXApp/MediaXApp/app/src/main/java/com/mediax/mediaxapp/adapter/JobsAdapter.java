package com.mediax.mediaxapp.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.ForgotPasswordActivity;
import com.mediax.mediaxapp.activity.JobsDetailActivity;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.JobsModel;
import com.mediax.mediaxapp.widgets.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mayank on 05/05/2016.
 */
public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> implements AppConstants {

    View view;
    Activity currentActivity;
    List<JobsModel> jobsModelList;

    Bundle bundle;

    public JobsAdapter(Activity currentActivity, List<JobsModel> jobsModelList) {
        this.currentActivity = currentActivity;
        this.jobsModelList = jobsModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(currentActivity).inflate(R.layout.item_jobs, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        if (jobsModelList.get(pos).getJobImage() != null && !jobsModelList.get(pos).getJobImage().equals(NO_IMAGE)) {

            String imagUrl = BASE_URL_IMAGES + jobsModelList.get(pos).getJobImage();

            ((BaseActivity) currentActivity).logTesting("jobs image url", imagUrl, Log.ERROR);

            Picasso.with(currentActivity).load(imagUrl).into(holder.imageJobs);


        }
        else {
            Picasso.with(currentActivity).load(R.drawable.icon_jobs).into(holder.imageJobs);
       //     holder.imageJobs.setBackgroundResource(R.drawable.icon_jobs);
        }
        /*else
        {
            holder.imageJobs.setBackgroundDrawable(currentActivity.getResources().getDrawable(R.drawable.icon_jobs));
        }*/


         holder.textJobs.setText(jobsModelList.get(pos).getJobTitle());
         holder.textJobDescription.setText(jobsModelList.get(pos).getJobDesc());

        holder.containerJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putParcelable(AppConstants.JobsModel, jobsModelList.get(pos));
                ((BaseActivity) currentActivity).startActivity(currentActivity, JobsDetailActivity.class, bundle, false, AppConstants.REQUEST_TAG_NO_RESULT, false, AppConstants.ANIMATION_SLIDE_UP);

            }
        });

    }

    @Override
    public int getItemCount() {
        return jobsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJobs;
        TextView textJobDescription;
        CircularImageView imageJobs;
        LinearLayout containerJobs;

        public ViewHolder(View itemView) {
            super(itemView);

            textJobs = (TextView) itemView.findViewById(R.id.textJobs);
            textJobDescription = (TextView) itemView.findViewById(R.id.textJobDescription);
            imageJobs = (CircularImageView) itemView.findViewById(R.id.imageJobs);
            containerJobs = (LinearLayout) itemView.findViewById(R.id.containerJobs);
        }
    }
}
