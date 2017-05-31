package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.utils.SharedPreferenceUtils;
import com.example.mohitattri.visheshagyaexpert.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends BaseActivity {

    private EditText edUserEmail, edUserPassword;
    private Button btnSignIn;
    private TextView tvNavigateForgotPassActivity, tvNavigateSignUpActivity;
    Context context;

    @Override
    protected void initViews() {
        edUserEmail = (EditText) findViewById(R.id.edUserEmail);
        edUserPassword = (EditText) findViewById(R.id.edUserPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvNavigateForgotPassActivity = (TextView) findViewById(R.id.tvNavigateForgotPassActivity);
        tvNavigateSignUpActivity = (TextView) findViewById(R.id.tvNavigateSignUpActivity);
    }

    @Override
    protected void initContext() {
        context = UserLoginActivity.this;
        currentActivity = UserLoginActivity.this;
    }

    @Override
    protected void initListners() {
        btnSignIn.setOnClickListener(this);
        tvNavigateForgotPassActivity.setOnClickListener(this);
        tvNavigateSignUpActivity.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (isMandatoryFields()) {
                    loginUser();
                }
                break;
            case R.id.tvNavigateForgotPassActivity:
                startActivity(currentActivity, UserForgotPasswordActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                finish();
                break;
            case R.id.tvNavigateSignUpActivity:
                startActivity(currentActivity, UserSignupActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                finish();
                break;
        }
    }

    private boolean isMandatoryFields() {
        edUserEmail.setError(null);
        edUserPassword.setError(null);
        if (!Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString());
            edUserEmail.setError(emailError);
            edUserEmail.requestFocus();
            return false;
        } else if (edUserPassword.getText().toString().isEmpty()) {
            edUserPassword.setError(getResources().getString(R.string.error_password_empty));
            edUserPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isMandatoryFieldsVishesh() {
        edUserEmail.setError(null);
        edUserPassword.setError(null);
        if (!Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString());
            edUserEmail.setError(emailError);
            edUserEmail.requestFocus();
            return false;
        } else if (edUserPassword.getText().toString().isEmpty()) {
            edUserPassword.setError(getResources().getString(R.string.error_password_empty));
            edUserPassword.requestFocus();
            return false;
        }
        return true;
    }


    private void loginUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://visheshagya-dev.herokuapp.com/api/auth/signin";
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                cancelProgressDialog();
                System.out.println("received data is : " + response.toString());
                if (response != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        //  Boolean responseError = jsonObject.getBoolean("error");
                        String token = jsonObject.getString("token");
                        if (token != null) {
                            System.out.println("token is :" + token);
                            SharedPreferenceUtils.getInstance(context).putBoolean(IS_LOGIN,true);
                            SharedPreferenceUtils.getInstance(context).putString(USER_TOKEN, token);
                            startActivity(currentActivity, AppAccessActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        }
                 /*       if (!responseError) {
                            String message = jsonObject.getString("message");
                            toast(message, false);
                            startActivity(currentActivity, UserLoginActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                cancelProgressDialog();
                int statusCode = error.networkResponse.statusCode;

                System.out.println("status code is: " + error.networkResponse.statusCode + " and data is :" + error.networkResponse.data);

                if (error.networkResponse.data != null) {
                    // System.out.println("error received is : " + error.networkResponse.data);
                    String body;
                    //get status code here
                    if (statusCode == RESPONCE_ERROR_400) {
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            System.out.println("body received is : " + body.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(body);
                                Boolean responseError = jsonObject.getBoolean("error");
                                if (responseError) {
                                    String message = jsonObject.getString("message");
                                    toast(message, false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edUserEmail.getText().toString().trim().toLowerCase());
                params.put("password", edUserPassword.getText().toString().trim());
                return params;
            }

 /*           @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = null;
                try {
                    params = super.getHeaders();
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }
                if (params == null)
                    params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }*/
        };
        queue.add(strReq);// add request queue
    }

    private void loginUserVishesh() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.visheshagya.in/webservices/login.php";
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                cancelProgressDialog();
                System.out.println("received data is : " + response.toString());
                if (response != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        //  Boolean responseError = jsonObject.getBoolean("error");
                        String success = jsonObject.getString("success");
                        String error = jsonObject.getString("error");
                        String userId = jsonObject.getString("message");
                        if (success.equals("1") && error.equals("0")) {
                            SharedPreferenceUtils.getInstance(context).putString(USER_ID, userId);
                            SharedPreferenceUtils.getInstance(context).putBoolean(IS_LOGIN, true);
                            startActivity(currentActivity, AppAccessActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        } else {
                            toast("email and password does not match", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast("Something went wrong. Please try again", false);
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edUserEmail.getText().toString().trim().toLowerCase());
                params.put("password", edUserPassword.getText().toString().trim());
                params.put("role", "1");
                return params;
            }
        };
        queue.add(strReq);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}

