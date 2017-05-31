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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.HomeActivity;
import in.visheshagya.visheshagya.activities.LoginSignupActivity;
import in.visheshagya.visheshagya.networking.ClassForUserAccount;

public class LoginPageFragment extends Fragment implements View.OnClickListener {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView forgotPSWD, noAccount;
    private String userEmail, userPassword;

    public LoginPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emailInput = (EditText) getActivity().findViewById(R.id.lEmailId);
        passwordInput = (EditText) getActivity().findViewById(R.id.lPassword);
        loginButton = (Button) getActivity().findViewById(R.id.loginButton);
        forgotPSWD = (TextView) getActivity().findViewById(R.id.forgotLink);
        noAccount = (TextView) getActivity().findViewById(R.id.signupLink);

        // setOnClickListener on components

        loginButton.setOnClickListener(this);
        forgotPSWD.setOnClickListener(this);
        noAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:

                if (validate()) {
                    ClassForUserAccount classForUserAccount = new ClassForUserAccount(getActivity().getApplicationContext());
                    boolean i = classForUserAccount.loginClient(userEmail, userPassword, 2);
                    if (i)//email, password, client type
                    {
                        startActivity(new Intent(getActivity(), HomeActivity.class)); // startActivity is started
                    } else {
                        Toast.makeText(getActivity(), "Login Deatails are not valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    return;
                }
                break;

            case R.id.forgotLink:
                ((LoginSignupActivity) getActivity()).setResetPasswordFragment();
                break;

            case R.id.signupLink:
                ((LoginSignupActivity) getActivity()).setSignupFragment();
                break;

            default:
                break;
        }
    }

    private boolean validate() {
        userEmail = emailInput.getText().toString();
        userPassword = passwordInput.getText().toString();

        if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            emailInput.setError("enter a valid email address");
            emailInput.requestFocus();
            return false;
        } else {
            emailInput.setError(null);
        }
        if (userPassword.isEmpty() || userPassword.length() < 4) {
            passwordInput.setError("Enter a validpassword");
            passwordInput.requestFocus();
            return false;
        } else {
            passwordInput.setError(null);
        }
        return true;
    }
}
