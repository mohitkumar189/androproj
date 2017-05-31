package com.example.mohitattri.visheshagyaexpert.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpertAccountsFragment extends BaseFragment {


    private Context context;
    private Activity currentActivity;
    private EditText edUserAccountNumber, edUserAccountHolderName, edUserIFSCCode, edUserBankName, edUserBankAddress, edUserAccountMicrNo;
    private RadioGroup radioGroupAccountType;
    private RadioButton radioSavingType, radioCurrentType;
    private Button btnAddAccount;
    private String accountType;

    public ExpertAccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        edUserAccountNumber = (EditText) currentActivity.findViewById(R.id.edUserAccountNumber);
        edUserAccountHolderName = (EditText) currentActivity.findViewById(R.id.edUserAccountHolderName);
        edUserIFSCCode = (EditText) currentActivity.findViewById(R.id.edUserIFSCCode);
        edUserBankName = (EditText) currentActivity.findViewById(R.id.edUserBankName);
        edUserBankAddress = (EditText) currentActivity.findViewById(R.id.edUserBankAddress);
        edUserAccountMicrNo = (EditText) currentActivity.findViewById(R.id.edUserAccountMicrNo);
        radioSavingType = (RadioButton) currentActivity.findViewById(R.id.radioSavingType);
        radioCurrentType = (RadioButton) currentActivity.findViewById(R.id.radioCurrentType);
        btnAddAccount = (Button) currentActivity.findViewById(R.id.btnAddAccount);

    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        btnAddAccount.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expert_accounts, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddAccount:
                toast(context, "clicked");
                if (isMandatoryFields()) {
                    saveAccountDetails();
                }
                break;
        }
    }

    private boolean isMandatoryFields() {
        edUserAccountNumber.setError(null);
        edUserAccountHolderName.setError(null);
        edUserIFSCCode.setError(null);
        edUserBankName.setError(null);
        edUserBankAddress.setError(null);
        edUserAccountMicrNo.setError(null);
        if (edUserAccountNumber.getText().toString().trim().equals("")) {
            edUserAccountNumber.setError("error");
            edUserAccountNumber.requestFocus();
            return false;
        } else if (edUserAccountHolderName.getText().toString().trim().equals("")) {
            edUserAccountHolderName.setError("error");
            edUserAccountHolderName.requestFocus();
            return false;
        } else if (edUserIFSCCode.getText().toString().trim().equals("")) {
            edUserIFSCCode.setError("error");
            edUserIFSCCode.requestFocus();
            return false;
        } else if (edUserBankName.getText().toString().trim().equals("")) {
            edUserBankName.setError("error");
            edUserBankName.requestFocus();
            return false;
        } else if (edUserBankAddress.getText().toString().trim().equals("")) {
            edUserBankAddress.setError("error");
            edUserBankAddress.requestFocus();
            return false;
        } else if (edUserAccountMicrNo.getText().toString().trim().equals("")) {
            edUserAccountMicrNo.setError("error");
            edUserAccountMicrNo.requestFocus();
            return false;
        } else if (radioCurrentType.isChecked() || radioSavingType.isChecked()) {
            if (radioCurrentType.isChecked()) {
                accountType = "current";
            } else if (radioSavingType.isChecked()) {
                accountType = "savings";
            }
        } else {
            toast(getActivity(), "select account type");
            return false;
        }

        return true;
    }


    private void saveAccountDetails() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://visheshagya-dev.herokuapp.com/api/users/account";
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                cancelProgressDialog();
                System.out.println("received data is : " + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        String message = jsonObject.getString("message");
                        toast(getActivity(), message);
                    } else {
                        String message = jsonObject.getString("message");
                        toast(getActivity(), message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                cancelProgressDialog();
                int statusCode = error.networkResponse.statusCode;
//
                //  System.out.println("status code is: " + error.networkResponse.statusCode + " and data is :" + error.networkResponse.data);

                if (error != null) {
                    // System.out.println("error received is : " + error.networkResponse.data);
                    String body;
                    //get status code here
                    if (statusCode > RESPONCE_ERROR_400) {
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
                                    toast(getActivity(), message);
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
                params.put("accountNo", edUserAccountNumber.getText().toString().trim());
                params.put("accountName", edUserAccountHolderName.getText().toString());
                params.put("ifscCode", edUserIFSCCode.getText().toString());
                params.put("bankName", edUserBankName.getText().toString());
                params.put("bankAddress", edUserBankAddress.getText().toString());
                params.put("micrCode", edUserAccountHolderName.getText().toString());
                params.put("accountType", accountType);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-access-token", SharedPreferenceUtils.getInstance(context).getString(USER_TOKEN));
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                return headers;
            }

            /*    @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params =  super.getHeaders();
                    if(params==null)params = new HashMap<>();
                    params.put("x-access-token", SharedPreferenceUtils.getInstance(context).getString(USER_TOKEN));
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }*/
        };
        queue.add(strReq);// add request queue


    }
}
