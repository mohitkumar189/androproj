package com.mediax.mediaxapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.AddJobsActivity;
import com.mediax.mediaxapp.activity.AddNewsActivity;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.adapter.JobsAdapter;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.JobsModel;
import com.mediax.mediaxapp.model.NewsModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 05/05/2016.
 */
public class JobsFragment extends BaseFragment implements AppConstants {

    SearchView searchJobs;
    RecyclerView recyclerJobs;

    JobsAdapter jobsAdapter;

    LinearLayoutManager managerJobs;

    List<JobsModel> jobsModelList;
    List<JobsModel> jobsModelListComplete;

    Context context;

    String searchText;

    TextView textNoJobs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.from(currentActivity).inflate(R.layout.fragment_jobs, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {

        context = getActivity();
        currentActivity = getActivity();
        searchText = "";
        searchJobs = (SearchView) view.findViewById(R.id.searchJobs);
        recyclerJobs = (RecyclerView) view.findViewById(R.id.recyclerJobs);
        textNoJobs = (TextView) view.findViewById(R.id.textNoJobs);

        managerJobs = new LinearLayoutManager(currentActivity);

        jobsModelList = new ArrayList<>();
        jobsModelListComplete = new ArrayList<>();

        jobsAdapter = new JobsAdapter(currentActivity, jobsModelListComplete);

        recyclerJobs.setLayoutManager(managerJobs);
        recyclerJobs.setAdapter(jobsAdapter);
        settingJobsSearchBarListner();
        getJobs();

    }

    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {

    }

    @Override
    public void onClick(View view) {

    }


    private void getJobs() {

        //     ((BaseActivity) currentActivity).progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonNews = null;

        String URL_GET_JOBS = GET_JOBS_URL;
        if (!searchText.equals("")) {
            URL_GET_JOBS = URL_GET_JOBS + "?search_string=" + searchText + "&zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);

        } else {
            URL_GET_JOBS = URL_GET_JOBS + "?zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);

        }
        Log.e("url get jobs", URL_GET_JOBS);
        JsonObjectRequest getJobsRequest = new JsonObjectRequest(Request.Method.GET, URL_GET_JOBS, jsonNews, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //       ((BaseActivity) currentActivity).cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_ujobs), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, getResources().getString(R.string.fetch_jobs_error), getResources().getString(R.string.fetch_jobs_error), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {
                        if (message.equals(NO_JOB_FOUND)) {
                            recyclerJobsInVisible();
                            jobsAdapter.notifyDataSetChanged();
                        } else {
                            recyclerJobsVisible();
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            List<JobsModel> newsModelList = Arrays.asList(gson.fromJson(message.toString(), JobsModel[].class));
                            jobsModelListComplete.clear();
                            jobsModelListComplete.addAll(newsModelList);
                            jobsAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_sign_up), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_sign_up), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(getJobsRequest);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_jobs, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAddJobs) {
            ((BaseActivity) currentActivity).startActivity(currentActivity, AddJobsActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void recyclerJobsVisible() {
        recyclerJobs.setVisibility(View.VISIBLE);
        textNoJobs.setVisibility(View.GONE);
    }

    private void recyclerJobsInVisible() {
        recyclerJobs.setVisibility(View.GONE);
        textNoJobs.setVisibility(View.VISIBLE);
    }


    private void settingJobsSearchBarListner() {
        //   Toast.makeText(currentActivity, "hide ", Toast.LENGTH_SHORT).show();
        searchJobs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchText = newText;
                    jobsModelListComplete.clear();
                    getJobs();
                } else {
                    jobsModelListComplete.clear();
                    searchText = "";
                    getJobs();
                }

                return true;
            }
        });
    }

}
