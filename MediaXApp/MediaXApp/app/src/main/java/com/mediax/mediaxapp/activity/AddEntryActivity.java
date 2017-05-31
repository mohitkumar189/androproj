package com.mediax.mediaxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.AddPersonModel;
import com.mediax.mediaxapp.model.SignUpUserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddEntryActivity extends BaseActivity {

    EditText editName;
    EditText editDesignation;
    EditText editContact;
    EditText editEmailId;

    AutoCompleteTextView autoCompleteCompany;

    Button buttonAddEntry;

    AddPersonModel addPersonModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
    }


    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_add_member));

        editName = (EditText) findViewById(R.id.editName);
        editDesignation = (EditText) findViewById(R.id.editDesignation);
        editContact = (EditText) findViewById(R.id.editContact);
        editEmailId = (EditText) findViewById(R.id.editEmailId);

        autoCompleteCompany = (AutoCompleteTextView) findViewById(R.id.autoCompleteCompany);

        buttonAddEntry = (Button) findViewById(R.id.buttonAddEntry);

    }

    @Override
    protected void initContext() {
        currentActivity = AddEntryActivity.this;
        context = AddEntryActivity.this;
    }

    @Override
    protected void initListners() {
//        buttonAddEntry.setOnClickListener(this);
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

    private boolean isMandatoryFields() {
        editName.setError(null);
        editDesignation.setError(null);
        editContact.setError(null);
        editEmailId.setError(null);

        autoCompleteCompany.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.error_name_empty));
            editName.requestFocus();
            return false;
        } else if (editDesignation.getText().toString().isEmpty()) {
            editDesignation.setError(getResources().getString(R.string.error_user_designation_empty));
            editDesignation.requestFocus();
            return false;
        } else if (editContact.getText().toString().isEmpty()) {
            editContact.setError(getResources().getString(R.string.error_contact_empty));
            editContact.requestFocus();
            return false;
        } else if (editEmailId.getText().toString().isEmpty()) {
            editEmailId.setError(getResources().getString(R.string.error_emailId_empty));
            editEmailId.requestFocus();
            return false;
        } else if (autoCompleteCompany.getText().toString().isEmpty()) {
            autoCompleteCompany.setError(getResources().getString(R.string.error_company_empty));
            autoCompleteCompany.requestFocus();
            return false;
        }

        initPersonModel();
        return true;

    }

    private void initPersonModel() {
        if (addPersonModel == null) {
            addPersonModel = new AddPersonModel();
        }

        addPersonModel.setName(editName.getText().toString());
        addPersonModel.setEmailId(editEmailId.getText().toString());
        addPersonModel.setCompName(autoCompleteCompany.getText().toString());
        addPersonModel.setContactNo(editContact.getText().toString());
        addPersonModel.setDesignaton(editDesignation.getText().toString());
        addPersonModel.setRequesterUserId(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_ID));
        addPersonModel.setId(0);

        addPersonModel.setZone(SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION));

        addPersonModel.setActionType(ACTION_TYPE_PESRON_ADD);

        AddPersonModel.Company company = new AddPersonModel.Company();

        company.setName(autoCompleteCompany.getText().toString());

        addPersonModel.setCompany(company);
    }


    private void addEntry() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddPersonRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddPersonRequest = new JSONObject(gson.toJson(addPersonModel));
            Log.e("json add person request", jsonAddPersonRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_ADD_PERSON = ADD_PERSON_URL;
        JsonObjectRequest addPersonRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_PERSON, jsonAddPersonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_add_person), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.nwk_error_add_person), getResources().getString(R.string.nwk_error_add_person), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        toast(getResources().getString(R.string.message_pesron_submitted_for_review_activity), true);
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
                toast(getResources().getString(R.string.nwk_error_add_person), true);
                logTesting(getResources().getString(R.string.nwk_error_add_person), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(addPersonRequest);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buttonAddEntry: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        // to save contact
                        addEntry();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);


                }
                return true;

            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }
}
