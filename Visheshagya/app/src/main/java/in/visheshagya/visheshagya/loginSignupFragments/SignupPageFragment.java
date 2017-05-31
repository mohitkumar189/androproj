/*
    @Auther MOHIT KUMAR
    Created on 08/08/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.loginSignupFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.HomeActivity;
import in.visheshagya.visheshagya.activities.LoginSignupActivity;
import in.visheshagya.visheshagya.networking.ClassForUserAccount;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.resourses.StringResources;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;
import in.visheshagya.visheshagya.webViews.WebViewActivity;

public class SignupPageFragment extends Fragment implements View.OnClickListener {

    private int clientType;
    private EditText emailInput, nameInput, mobileInput;
    private Spinner spinner;
    private TextView loginLink, termsAndCond;
    private Button signupBUTTON;
    private String name, mobileNumber, userEmail;
    private CheckBox checkBox;

    public SignupPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        termsAndCond = (TextView) getActivity().findViewById(R.id.termsAndCondTV);
        checkBox = (CheckBox) getActivity().findViewById(R.id.termsAcceptCheck);
        emailInput = (EditText) getActivity().findViewById(R.id.sEmailId);
        nameInput = (EditText) getActivity().findViewById(R.id.sName);
        mobileInput = (EditText) getActivity().findViewById(R.id.sMobileNumber);
        signupBUTTON = (Button) getActivity().findViewById(R.id.signupButton);
        loginLink = (TextView) getActivity().findViewById(R.id.loginLink);
        loginLink.setOnClickListener(this);
        signupBUTTON.setOnClickListener(this);
        termsAndCond.setOnClickListener(this);

    }

    public boolean validate() {
        name = nameInput.getText().toString().trim();
        userEmail = emailInput.getText().toString().toLowerCase().trim();
        mobileNumber = mobileInput.getText().toString().trim();
        if (name.isEmpty() || name.length() < 3) {
            nameInput.setError("Name too short");
            return false;
        } else if (userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            emailInput.setError("Enter a valid email");
            return false;
        } else if (Patterns.PHONE.matcher(mobileNumber).matches()) {
            mobileInput.setError("Enter a valid number");
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        boolean checked = checkBox.isChecked();
        switch (view.getId()) {
            case R.id.signupButton:

                if (checked) {
                    name = nameInput.getText().toString().trim();
                    userEmail = emailInput.getText().toString().toLowerCase().trim();
                    mobileNumber = mobileInput.getText().toString().trim();
                    if (name.isEmpty() || name.length() < 3) {
                        nameInput.setError("Name too short");
                        nameInput.requestFocus();

                    } else {
                        if (userEmail.isEmpty() || !userEmail.matches(StringResources.emailPattern)) {
                            emailInput.setError("Enter a valid email");
                            emailInput.requestFocus();

                        } else {
                            if (mobileNumber.length() < 10) {
                                mobileInput.setError("Enter a valid number");
                                mobileInput.requestFocus();
                            } else {
                                ClassForUserAccount classForUserAccount = new ClassForUserAccount(getActivity());
                                boolean i = classForUserAccount.signupClient(name, userEmail, mobileNumber, 2);
                                if (i) {
                                    ((LoginSignupActivity) getActivity()).setLoginFragment();
                                } else {
                                    Toast.makeText(getActivity(), "Not Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Accept Terms", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginLink:
                ((LoginSignupActivity) getActivity()).setLoginFragment();
                break;
            case R.id.termsAndCondTV:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("action", "tmc");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
