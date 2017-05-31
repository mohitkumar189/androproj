package com.mediax.mediaxapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.ContactUsModel;
import com.mediax.mediaxapp.utils.Helper;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Mayank on 28/04/2016.
 */
public class ContactUsFragment extends BaseFragment implements AppConstants {

    EditText editUserEmail;
    EditText editMessage;

    Button buttonSend;

    ContactUsModel contactUsModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.from(currentActivity).inflate(R.layout.fragment_contact_us, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        editUserEmail = (EditText) view.findViewById(R.id.editUserEmail);
        editMessage = (EditText) view.findViewById(R.id.editMessage);
        buttonSend = (Button) view.findViewById(R.id.buttonSend);
    }


    private void initContactUsModel() {
        if (contactUsModel == null) {
            contactUsModel = new ContactUsModel();
        }

        contactUsModel.setUsername(editUserEmail.getText().toString());

        contactUsModel.setMessage(editMessage.getText().toString());


    }


    private boolean isMandatoryFields() {
        editUserEmail.setError(null);
        editMessage.setError(null);
        if (!Validator.isValidEmail(context, editUserEmail.getText().toString()).equals("")) {
            editUserEmail.setError(Validator.isValidEmail(context, editUserEmail.getText().toString()));
            editUserEmail.requestFocus();
            return false;
        } else if (editMessage.getText().toString().isEmpty()) {
            editMessage.setError(getResources().getString(R.string.emptyMessage));
            editMessage.requestFocus();
            return false;
        }

        initContactUsModel();
        return true;
    }

    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {
        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSend: {

                if (Validator.isNetworkAvailable(context)) {
                    if (isMandatoryFields()) {
                        contactUsEntry();
                    }
                } else {
                    ((BaseActivity) currentActivity).alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                }

                break;
            }
        }

    }

    private void contactUsEntry() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonContactUs = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonContactUs = new JSONObject(gson.toJson(contactUsModel));
            Log.e("json contact us request", jsonContactUs.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_CONTACT_US= CONTACT_US_URL;
        JsonObjectRequest contactUsRequest = new JsonObjectRequest(Request.Method.POST, URL_CONTACT_US, jsonContactUs, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_contact_us), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, getResources().getString(R.string.nwk_error_contact_us), getResources().getString(R.string.nwk_error_contact_us), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        ((BaseActivity) currentActivity).toast(getResources().getString(R.string.message_admin_contacting), true);
                        ((BaseActivity) currentActivity).finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.nwk_error_edit_person), true);
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_edit_person), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(contactUsRequest);
    }


}
