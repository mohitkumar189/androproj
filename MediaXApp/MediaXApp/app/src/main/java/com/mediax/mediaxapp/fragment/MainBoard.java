package com.mediax.mediaxapp.fragment;

import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.Dashboard;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.fragment.BaseFragment;

public class MainBoard extends BaseFragment {

    LinearLayout containerChat;
    LinearLayout containerDatabase;
    LinearLayout containerNews;
    LinearLayout containerJobs;
    LinearLayout containerFeedback;
    LinearLayout containerContactUs;
    LinearLayout containerSettings;
    LinearLayout containerLogout;


    View view;


    @Override
    public void alertOkClicked() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_board, container, false);

        return view;
    }

    @Override
    protected void initViews() {


        containerChat = (LinearLayout) view.findViewById(R.id.containerChat);
        containerDatabase = (LinearLayout) view.findViewById(R.id.containerDatabase);
        containerNews = (LinearLayout) view.findViewById(R.id.containerNews);
        containerJobs = (LinearLayout) view.findViewById(R.id.containerJobs);
        containerFeedback = (LinearLayout) view.findViewById(R.id.containerFeedback);
        containerContactUs = (LinearLayout) view.findViewById(R.id.containerContactUs);
        containerSettings = (LinearLayout) view.findViewById(R.id.containerSettings);
        containerLogout = (LinearLayout) view.findViewById(R.id.containerLogout);

    }

    @Override
    protected void initContext() {
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    protected void initListners() {

        containerChat.setOnClickListener(this);
        containerDatabase.setOnClickListener(this);
        containerNews.setOnClickListener(this);
        containerJobs.setOnClickListener(this);
        containerFeedback.setOnClickListener(this);
        containerContactUs.setOnClickListener(this);
        containerSettings.setOnClickListener(this);
        containerLogout.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        switch (view.getId()) {
            case R.id.containerChat: {
                ((Dashboard) currentActivity).setSelection(AppConstants.CHATS);

                break;
            }

            case R.id.containerDatabase: {
                ((Dashboard) currentActivity).setSelection(AppConstants.DATABASE);

                break;
            }
            case R.id.containerNews: {
                ((Dashboard) currentActivity).setSelection(AppConstants.NEWS);

                break;
            }
            case R.id.containerSettings: {
                ((Dashboard) currentActivity).setSelection(AppConstants.SETTINGS);

                break;
            }
            case R.id.containerJobs: {
                ((Dashboard) currentActivity).setSelection(AppConstants.JOBS);
                break;
            }

            case R.id.containerLogout: {
                ((Dashboard) currentActivity).setSelection(AppConstants.LOGOUT);
                break;
            }

            case R.id.containerFeedback: {
                ((Dashboard) currentActivity).setSelection(AppConstants.REVIEW);
                break;
            }
            case R.id.containerContactUs: {
                ((Dashboard) currentActivity).setSelection(AppConstants.CONTACTUS);
                break;
            }

        }

    }


}
