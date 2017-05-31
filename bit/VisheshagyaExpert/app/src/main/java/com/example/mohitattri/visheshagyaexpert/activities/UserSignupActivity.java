package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.utils.Validator;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserSignupActivity extends BaseActivity {

    Context context;
    TextView tvNavigateSigninActivity, tvCheckTerms;
    Button btnSignup;
    EditText edUserName, edUserEmail, edUserMobile, edUserPassword;
    CheckBox checkAcceptTerms;

    @Override
    protected void initViews() {
        edUserEmail = (EditText) findViewById(R.id.edUserEmail);
        edUserMobile = (EditText) findViewById(R.id.edUserMobile);
        edUserName = (EditText) findViewById(R.id.edUserName);
        edUserPassword = (EditText) findViewById(R.id.edUserPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        tvNavigateSigninActivity = (TextView) findViewById(R.id.tvNavigateSignInActivity);
        checkAcceptTerms = (CheckBox) findViewById(R.id.checkAcceptTerms);
    }

    @Override
    protected void initContext() {
        context = UserSignupActivity.this;
        currentActivity = UserSignupActivity.this;
    }

    @Override
    protected void initListners() {
        btnSignup.setOnClickListener(this);
        tvNavigateSigninActivity.setOnClickListener(this);
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
        setContentView(R.layout.activity_user_signup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup: {
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    //functions should be changed
                    if (isMandatoryFieldsVishesh()) {
                        if(checkAcceptTerms.isChecked()){
                            signUpVishesh();
                        }else {
                            toast("Accept terms",false);
                        }

                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.tvNavigateSignInActivity:
                startActivity(currentActivity, UserLoginActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                break;
            case R.id.tvCheckTerms:
                break;
        }
    }

    private boolean isMandatoryFields() {
        edUserEmail.setError(null);
        edUserMobile.setError(null);
        edUserName.setError(null);
        edUserPassword.setError(null);

        if (edUserName.getText().toString().trim().isEmpty()) {

            edUserName.setError(getResources().getString(R.string.error_fullname_empty));
            edUserName.requestFocus();
            return false;
        } else if (!Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString());

            edUserEmail.setError(emailError);
            edUserEmail.requestFocus();
            return false;

        } else if (edUserPassword.getText().toString().isEmpty()) {

            edUserPassword.setError(getResources().getString(R.string.error_password_empty));
            edUserPassword.requestFocus();
            return false;

        } else if (!Validator.getInstance().validateNumber(context, edUserMobile.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, edUserMobile.getText().toString());
            edUserMobile.setError(numberError);
            edUserMobile.requestFocus();
            return false;
        }
        //initSignUpModel();
        return true;
    }

    private boolean isMandatoryFieldsVishesh() {
        edUserEmail.setError(null);
        edUserMobile.setError(null);
        edUserName.setError(null);

        if (edUserName.getText().toString().trim().isEmpty()) {

            edUserName.setError(getResources().getString(R.string.error_fullname_empty));
            edUserName.requestFocus();
            return false;
        } else if (!Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, edUserEmail.getText().toString());

            edUserEmail.setError(emailError);
            edUserEmail.requestFocus();
            return false;

        } else if (!Validator.getInstance().validateNumber(context, edUserMobile.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, edUserMobile.getText().toString());
            edUserMobile.setError(numberError);
            edUserMobile.requestFocus();
            return false;
        }
        //initSignUpModel();
        return true;
    }


    private void signUp() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        String url = "http://visheshagya-dev.herokuapp.com/api/auth/signup";
        //String url1="http://192.168.0.11/test.php";
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
                        Boolean responseError = jsonObject.getBoolean("error");
                        if (!responseError) {
                            String message = jsonObject.getString("message");
                            toast(message, false);
                            startActivity(currentActivity, UserLoginActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
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
                System.out.println("error received is : " + error.networkResponse.data);
                String body;
                //get status code here
                int statusCode = error.networkResponse.statusCode;
                if (statusCode == RESPONCE_ERROR_422) {
                    //get response body and parse with appropriate encoding
                    if (error.networkResponse.data != null) {
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
                //toast(getResources().getString(R.string.nwk_error_sign_up), true);
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                printMsg("values: ", edUserEmail.getText().toString());
                params.put("email", edUserEmail.getText().toString().trim().toLowerCase());
                params.put("firstName", edUserName.getText().toString().trim());
                params.put("password", edUserPassword.getText().toString().trim());
                params.put("mobile", edUserMobile.getText().toString().trim());
                params.put("username", edUserEmail.getText().toString().trim());
                //  params.put("lastName", edUserMobile.getText().toString().trim());
                return params;
            }
        };
        //  strReq.getUrl();
        queue.add(strReq);

        System.out.println("url is : " + strReq.getUrl().toString());
        // Adding String request to request queue
        //AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    private void signUpVishesh() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url1="http://192.168.0.11/test.php";
        String url = "http://www.visheshagya.in/webservices/registration.php";
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
                        String success = jsonObject.getString("success");
                        String error = jsonObject.getString("error");

                        if (success.equals("1") && error.equals("0")) {
                            toast("Sign up successfull", false);
                            startActivity(currentActivity, UserLoginActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        } else {
                            String message = jsonObject.getString("message");
                            toast(message, false);
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
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edUserEmail.getText().toString().trim().toLowerCase());
                params.put("name", edUserName.getText().toString().trim());
                params.put("contactNo", edUserMobile.getText().toString().trim());
                params.put("role", "1");
                return params;
            }
        };
        //  strReq.getUrl();
        queue.add(strReq);

        System.out.println("url is : " + strReq.getUrl().toString());
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
