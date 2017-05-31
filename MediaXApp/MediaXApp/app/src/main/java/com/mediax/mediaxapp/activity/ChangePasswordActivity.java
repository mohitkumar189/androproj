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
import com.mediax.mediaxapp.model.ChangePasswordModel;
import com.mediax.mediaxapp.model.SignInUserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {

    EditText editOldPassword;
    EditText editNewPassword;

    Button buttonChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_change_password));
        editOldPassword = (EditText) findViewById(R.id.editOldPassword);
        editNewPassword = (EditText) findViewById(R.id.editNewPassword);

        buttonChangePassword = (Button) findViewById(R.id.buttonChangePassword);

    }

    @Override
    protected void initContext() {
        context = ChangePasswordActivity.this;
        currentActivity = ChangePasswordActivity.this;

    }

    @Override
    protected void initListners() {
        buttonChangePassword.setOnClickListener(this);
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
        editOldPassword.setError(null);
        editNewPassword.setError(null);
        if (editOldPassword.getText().toString().isEmpty()) {
            editOldPassword.setError(getResources().getString(R.string.error_empty_old_password));
            editOldPassword.requestFocus();
            return false;
        } else if (editNewPassword.getText().toString().isEmpty()) {
            editNewPassword.setError(getResources().getString(R.string.error_empty_new_password));
            editNewPassword.requestFocus();
            return false;
        }
        initModel();
        return true;

    }

    private void initModel() {
        ChangePasswordModel.getInstance().setOldPassword(editOldPassword.getText().toString());
        ChangePasswordModel.getInstance().setNewPassword(editNewPassword.getText().toString());
        ChangePasswordModel.getInstance().setUserId(Integer.parseInt(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_ID)));
    }


    private void changePassword() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonChangePassword = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonChangePassword = new JSONObject(gson.toJson(ChangePasswordModel.getInstance()));
            Log.e("json change pas request", jsonChangePassword.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_CHANGE_PASSWORD = CHANGE_PASSWORD_URL;
        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST, URL_CHANGE_PASSWORD, jsonChangePassword, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_change_apssword), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);


                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.nwk_old_password_incorrect), getResources().getString(R.string.nwk_old_password_incorrect), getResources().getString(R.string.label_ok_button), getResources().getString(R.string.label_cancel_button), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        toast(getResources().getString(R.string.msg_password_changed), true);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.error_change_password), true);
                logTesting(getResources().getString(R.string.error_change_password), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(changePasswordRequest);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonChangePassword: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        changePassword();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                }


                break;
            }
        }

    }
}
