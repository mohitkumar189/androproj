package com.mediax.mediaxapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.model.AddPersonModel;
import com.mediax.mediaxapp.model.UserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UserDetailActivity extends BaseActivity {

    TextView textName;
    TextView textCompany;
    TextView textDesignation;
    TextView textContact;
    TextView textEmailId;

    UserModel userModel;

    AddPersonModel addPersonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ueer_detail);


    }


    @Override
    protected void initViews() {
        userModel = getIntent().getExtras().getParcelable(KEY_USER_DETAILS);
        textName = (TextView) findViewById(R.id.textName);
        textCompany = (TextView) findViewById(R.id.textCompany);
        textDesignation = (TextView) findViewById(R.id.textDesignation);
        textContact = (TextView) findViewById(R.id.textContact);
        textEmailId = (TextView) findViewById(R.id.textEmailId);

        textName.setText(userModel.getName());
        textCompany.setText(userModel.getCompName());
        textDesignation.setText(userModel.getDesignaton());
        textContact.setText(userModel.getContactNo());
        textEmailId.setText(userModel.getEmailId());
    }


    @Override
    protected void initContext() {
        currentActivity = UserDetailActivity.this;
        context = UserDetailActivity.this;
    }


    private void initPersonModel() {
        if (addPersonModel == null) {
            addPersonModel = new AddPersonModel();
        }

        addPersonModel.setName(textName.getText().toString());
        if (textEmailId.getText().toString().isEmpty())
        {
            addPersonModel.setEmailId("-");
        }
        else
        {
            addPersonModel.setEmailId(textEmailId.getText().toString());
        }

        addPersonModel.setCompName(textCompany.getText().toString());
        addPersonModel.setContactNo(textContact.getText().toString());
        addPersonModel.setDesignaton(textDesignation.getText().toString());
        addPersonModel.setRequesterUserId(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_ID));
        addPersonModel.setId(Integer.valueOf(userModel.getId()));

        addPersonModel.setActionType(ACTION_TYPE_PESRON_DELETE);

        AddPersonModel.Company company = new AddPersonModel.Company();

        company.setName(textCompany.getText().toString());

        addPersonModel.setCompany(company);
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
        switch (alertType) {
            case ALERT_TYPE_DELETE_USER: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    initPersonModel();
                    deleteUser();
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.label_delete_user), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_DELETE_USER);

                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }


    private void deleteUser() {
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonDeletePersonRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonDeletePersonRequest = new JSONObject(gson.toJson(addPersonModel));
            Log.e("json del person request", jsonDeletePersonRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_DELETE_PERSON = DELETE_PERSON_URL;
        JsonObjectRequest deletePersonRequest = new JsonObjectRequest(Request.Method.POST, URL_DELETE_PERSON, jsonDeletePersonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_delete_person), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.nwk_error_delete_person), getResources().getString(R.string.nwk_error_delete_person), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        toast(getResources().getString(R.string.message_person_deletion), true);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_delete_person), true);
                logTesting(getResources().getString(R.string.nwk_error_delete_person), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(deletePersonRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == R.id.btnEdit) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putParcelable(com.mediax.mediaxapp.constant.AppConstants.KEY_USER_DETAILS, userModel);

            startActivity(currentActivity, EditUserActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

            return true;
        } else if (item != null && item.getItemId() == R.id.btnDelete) {
            alert(currentActivity, getResources().getString(R.string.alert_delete_user), getResources().getString(R.string.label_delete_user), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_DELETE_USER);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
