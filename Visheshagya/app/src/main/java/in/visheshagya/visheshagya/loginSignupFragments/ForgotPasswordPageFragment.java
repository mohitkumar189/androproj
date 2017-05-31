/*
    @Auther MOHIT KUMAR
    Created on 09/08/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.loginSignupFragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.LoginSignupActivity;
import in.visheshagya.visheshagya.networking.ClassForUserAccount;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class ForgotPasswordPageFragment extends Fragment implements View.OnClickListener {

    private EditText emailInput;
    private EditText mobileInput;
    private TextView loginLINK;
    private String email, mobileNumber;

    public ForgotPasswordPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emailInput = (EditText) getActivity().findViewById(R.id.rEmailId);
        mobileInput = (EditText) getActivity().findViewById(R.id.rMobileNumber);
        loginLINK = (TextView) getActivity().findViewById(R.id.loginLink);
        loginLINK.setOnClickListener(this);

        emailInput = (EditText) getActivity().findViewById(R.id.rEmailId);
        mobileInput = (EditText) getActivity().findViewById(R.id.rMobileNumber);
        Button resetPasswordButton = (Button) getActivity().findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    ClassForUserAccount classForUserAccount = new ClassForUserAccount(getActivity().getApplicationContext());
                    boolean a = classForUserAccount.fogotPassword(email, mobileNumber, "2");
                    if (a) {
                        Toast.makeText(getActivity(), "Password sent on registered number", Toast.LENGTH_SHORT).show();
                        ((LoginSignupActivity) getActivity()).setLoginFragment();
                    } else {
                        Toast.makeText(getActivity(), "Details are not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        email = emailInput.getText().toString();
        mobileNumber = mobileInput.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("enter a valid email address");
            emailInput.requestFocus();
            valid = false;
        } else {
            emailInput.setError(null);
        }
        if (!TextUtils.isEmpty(mobileNumber)) {
            // uMobileNumber.setError(null);
            valid = Patterns.PHONE.matcher(mobileNumber).matches();
        } else {
            mobileInput.setError("enter a valid mobile number");
            mobileInput.requestFocus();
        }
        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginLink:
                ((LoginSignupActivity) getActivity()).setLoginFragment();
                break;
        }
    }
}

