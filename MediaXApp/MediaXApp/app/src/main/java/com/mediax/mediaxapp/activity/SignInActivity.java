package com.mediax.mediaxapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.gson.annotations.SerializedName;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.SignInUserModel;
import com.mediax.mediaxapp.model.SignUpUserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SignInActivity extends BaseActivity {


    EditText editUserName;
    EditText editPassword;

    Button buttonSignIn;

    TextView textForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }


    @Override
    protected void initContext() {

        currentActivity = SignInActivity.this;
        context = SignInActivity.this;
    }

    @Override
    protected void initViews() {

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        textForgotPassword = (TextView) findViewById(R.id.textForgotPassword);

    }


    @Override
    protected void initListners() {


        buttonSignIn.setOnClickListener(this);

        textForgotPassword.setOnClickListener(this);

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


    private void signIn() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonSignInRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonSignInRequest = new JSONObject(gson.toJson(SignInUserModel.getInstance()));
            Log.e("json log in request", jsonSignInRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_SIGN_IN = SIGNIN_URL;
        JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGN_IN, jsonSignInRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_sign_in), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_ID, response.getString(USER_ID));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_NAME, editUserName.getText().toString());
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_IMAGE, response.getString(USER_IMAGE));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_PROFILE_NAME, response.getString(USER_PROFILE_NAME));
                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(IS_LOGIN, true);
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.REGION, response.getString(ZONE));

                        setResult(RESULT_OK);
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
                toast(getResources().getString(R.string.nwk_error_sign_in), true);
                logTesting(getResources().getString(R.string.nwk_error_sign_in), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(signInRequest);
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.buttonSignIn: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        signIn();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                }
                break;
            }

            case R.id.textForgotPassword: {
                startActivity(currentActivity, ForgotPasswordActivity.class, bundle, false, REQUEST_TAG_FORGOT_PASSWORD_ACTIVITY, false, ANIMATION_SLIDE_UP);

                break;
            }
        }
    }


    private boolean isMandatoryFields() {
        editUserName.setError(null);
        editPassword.setError(null);

        if (editUserName.getText().toString().isEmpty()) {
            editUserName.setError(getResources().getString(R.string.error_empty_username));
            editUserName.requestFocus();
            return false;
        } else if (editPassword.getText().toString().isEmpty()) {

            editPassword.setError(getResources().getString(R.string.error_empty_password));
            editPassword.requestFocus();
            return false;
        }
        setLoginData();
        return true;
    }

    private void setLoginData() {
        SignInUserModel.getInstance().setAdmin(false);
        SignInUserModel.getInstance().setEmail(editUserName.getText().toString());
        SignInUserModel.getInstance().setPassword(editPassword.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAG_FORGOT_PASSWORD_ACTIVITY: {

                    break;
                }
            }
        }
    }
}
