package com.mediax.mediaxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class EditUserActivity extends BaseActivity {

    EditText editName;
    EditText editCompany;
    EditText editDesignation;
    EditText editContact;
    EditText editEmailId;

    Button buttonUpdate;
    UserModel userModel;

    AddPersonModel addPersonModel;


    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_edit_person_details));
        userModel = getIntent().getExtras().getParcelable(KEY_USER_DETAILS);
        editName = (EditText) findViewById(R.id.editName);
        editCompany = (EditText) findViewById(R.id.editCompany);
        editDesignation = (EditText) findViewById(R.id.editDesignation);
        editContact = (EditText) findViewById(R.id.editContact);
        editEmailId = (EditText) findViewById(R.id.editEmailId);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);


        editName.setText(userModel.getName());
        editCompany.setText(userModel.getCompName());
        editDesignation.setText(userModel.getDesignaton());
        editContact.setText(userModel.getContactNo());
        editEmailId.setText(userModel.getEmailId());
    }

    @Override
    protected void initContext() {

        currentActivity = EditUserActivity.this;
        context = EditUserActivity.this;

    }

    @Override
    protected void initListners() {
        buttonUpdate.setOnClickListener(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
    }


    @Override
    public void onAlertClicked(int alertType) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUpdate: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        initPersonModel();
                        editUserEntry();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_DELETE_USER);

                }
                break;
            }
        }
    }

    private boolean isMandatoryFields() {
        editName.setError(null);
        editCompany.setError(null);
        editDesignation.setError(null);
        editContact.setError(null);
        editEmailId.setError(null);


        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.empty_user_name));
            editName.requestFocus();
            return false;
        } else if (editCompany.getText().toString().isEmpty()) {
            editCompany.setError(getResources().getString(R.string.empty_company_name));
            editCompany.requestFocus();
            return false;
        } else if (editDesignation.getText().toString().isEmpty()) {
            editDesignation.setError(getResources().getString(R.string.empty_designation));
            editDesignation.requestFocus();
            return false;
        } else if (!Validator.getInstance().validateNumber(context, editContact.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, editContact.getText().toString());
            editContact.setError(numberError);
            editContact.requestFocus();
            return false;

        } else if (!Validator.getInstance().isValidEmail(context, editEmailId.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, editEmailId.getText().toString());

            editEmailId.setError(emailError);
            editEmailId.requestFocus();
            return false;

        }

        return true;


    }

    private void initPersonModel() {
        if (addPersonModel == null) {
            addPersonModel = new AddPersonModel();
        }

        addPersonModel.setName(editName.getText().toString());
        addPersonModel.setEmailId(editEmailId.getText().toString());
        addPersonModel.setCompName(editCompany.getText().toString());
        addPersonModel.setContactNo(editContact.getText().toString());
        addPersonModel.setDesignaton(editDesignation.getText().toString());
        addPersonModel.setRequesterUserId(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_ID));
        addPersonModel.setId(Integer.valueOf(userModel.getId()));

        addPersonModel.setActionType(ACTION_TYPE_PESRON_EDIT);

        AddPersonModel.Company company = new AddPersonModel.Company();

        company.setName(editCompany.getText().toString());

        addPersonModel.setCompany(company);
    }


    private void editUserEntry() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonEditPersonRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonEditPersonRequest = new JSONObject(gson.toJson(addPersonModel));
            Log.e("json edit pers request", jsonEditPersonRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_EDIT_PERSON = EDIT_PERSON_URL;
        JsonObjectRequest editPersonRequest = new JsonObjectRequest(Request.Method.POST, URL_EDIT_PERSON, jsonEditPersonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_edit_person), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.nwk_error_edit_person), getResources().getString(R.string.nwk_error_edit_person), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        toast(getResources().getString(R.string.message_person_editing), true);
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
                toast(getResources().getString(R.string.nwk_error_edit_person), true);
                logTesting(getResources().getString(R.string.nwk_error_edit_person), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(editPersonRequest);
    }


}
