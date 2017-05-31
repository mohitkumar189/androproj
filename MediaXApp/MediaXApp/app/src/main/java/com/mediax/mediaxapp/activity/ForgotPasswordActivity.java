package com.mediax.mediaxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.model.ForgotPasswordModel;
import com.mediax.mediaxapp.model.SignInUserModel;
import com.mediax.mediaxapp.utils.Helper;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ForgotPasswordActivity extends BaseActivity {

    EditText editRecoveryEmail;
    Button buttonResetPassword;

    LinearLayout containerMailSended;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    protected void initContext() {

        currentActivity = ForgotPasswordActivity.this;
        context = ForgotPasswordActivity.this;

    }

    @Override
    protected void initViews() {
        editRecoveryEmail = (EditText) findViewById(R.id.editRecoveryEmail);
        buttonResetPassword = (Button) findViewById(R.id.buttonResetPassword);
        containerMailSended = (LinearLayout) findViewById(R.id.containerMailSended);
    }


    @Override
    protected void initListners() {
        buttonResetPassword.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonResetPassword: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        ressetPasswordAndSendMail();
                    }
                } else {

                }
                break;
            }
        }

    }


    private boolean isMandatoryFields() {
        editRecoveryEmail.setError(null);
        if (!Validator.isValidEmail(context, editRecoveryEmail.getText().toString()).equals(""))

        {
            editRecoveryEmail.setError(Validator.isValidEmail(context, editRecoveryEmail.getText().toString()));
            editRecoveryEmail.requestFocus();
            return false;

        }

        initPassWordModel();

        return true;
    }

    private void initPassWordModel() {
        ForgotPasswordModel.getInstance().setPassword("" + Helper.generateRandomPassword());
        ForgotPasswordModel.getInstance().setEmail(editRecoveryEmail.getText().toString());
    }


    private void ressetPasswordAndSendMail() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonForgotPaswordRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonForgotPaswordRequest = new JSONObject(gson.toJson(ForgotPasswordModel.getInstance()));
            Log.e("json forgo pswd request", jsonForgotPaswordRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_FORGOT_PASSWORD = FORGOT_PASSWORD_URL;
        JsonObjectRequest forgotPasswordRequest = new JsonObjectRequest(Request.Method.POST, URL_FORGOT_PASSWORD, jsonForgotPaswordRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_forgot_password), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                        toast(getResources().getString(R.string.emailNotExist), true);
                    } else {

                        containerMailSended.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_forgot_password), true);
                logTesting(getResources().getString(R.string.nwk_error_forgot_password), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(forgotPasswordRequest);
    }


}
