package com.mediax.mediaxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.adapter.CompanyMembersAdapter;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.CompanyModel;
import com.mediax.mediaxapp.model.UserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompanyMembersActivity extends BaseActivity {

    TextView textNoMembers;
    RecyclerView recyclerCompanyMembers;


    List<UserModel> companyMembersModelList;
    List<UserModel> companyMembersModelListComplate;

    CompanyMembersAdapter companyMembersAdapter;

    LinearLayoutManager managerCompanyMembers;
    CompanyModel companyModel;
    String companyId;
    String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_members);
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_person_details));
        companyModel = getIntent().getExtras().getParcelable(KEY_CompanyDetails);
        companyId = companyModel.getCompId();
        companyName = companyModel.getName();

        textNoMembers = (TextView) findViewById(R.id.textNoMembers);
        recyclerCompanyMembers = (RecyclerView) findViewById(R.id.recyclerCompanyMembers);

        companyMembersModelList = new ArrayList<>();
        companyMembersModelListComplate = new ArrayList<>();

        companyMembersAdapter = new CompanyMembersAdapter(currentActivity, companyMembersModelListComplate);

        managerCompanyMembers = new LinearLayoutManager(currentActivity);

        recyclerCompanyMembers.setLayoutManager(managerCompanyMembers);
        recyclerCompanyMembers.setAdapter(companyMembersAdapter);

        getCompanyMembers();


    }

    @Override
    protected void initContext() {
        currentActivity = CompanyMembersActivity.this;
        context = CompanyMembersActivity.this;

    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }


    @Override
    public void onAlertClicked(int alertType) {

    }


    private void getCompanyMembers() {

        companyMembersModelList.clear();
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String URL_SEARCH_COMPANY = GET_EMPLOYEES_URL + companyId + "&zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);
        JsonObjectRequest searchCompanyRequest = new JsonObjectRequest(Request.Method.GET, URL_SEARCH_COMPANY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        if (message.equals(NO_EMPLOYEES_FOUND)) {
                            hideRecycler();
                        } else {
                            showRecycler();
                            Log.e("all company employ resp", "i" + message);
                            Gson gson = new Gson();
                            companyMembersModelList = Arrays.asList(gson.fromJson(message, UserModel[].class));
                            addCompanyName(companyMembersModelList);
                            companyMembersModelListComplate.addAll(companyMembersModelList);

                            companyMembersAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.error_get_company), true);
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.error_get_company), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(searchCompanyRequest);
    }


    private void addCompanyName(List<UserModel> companyMembersModelList) {
        for (int i = 0; i < companyMembersModelList.size(); i++) {
            if (companyMembersModelList.get(i) != null) {
                companyMembersModelList.get(i).setCompName(companyName);
            }

        }
    }


    private void hideRecycler() {
        recyclerCompanyMembers.setVisibility(View.GONE);
        textNoMembers.setVisibility(View.VISIBLE);
        textNoMembers.setText(getResources().getString(R.string.no_employees_found));
    }

    private void showRecycler() {
        recyclerCompanyMembers.setVisibility(View.VISIBLE);
        textNoMembers.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

    }
}
