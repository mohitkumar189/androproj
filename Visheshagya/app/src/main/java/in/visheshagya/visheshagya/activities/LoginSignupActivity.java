package in.visheshagya.visheshagya.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.FacebookSdk;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.loginSignupFragments.ForgotPasswordPageFragment;
import in.visheshagya.visheshagya.loginSignupFragments.LoginPageFragment;
import in.visheshagya.visheshagya.loginSignupFragments.SignupPageFragment;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class LoginSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this); // initialize facebook sdk
        setContentView(R.layout.activity_login_signup);
        setLoginFragment();

    }

    public void setLoginFragment() {
        LoginPageFragment loginFragment = new LoginPageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.LSFragmentcontainer, loginFragment, "login fragment").addToBackStack("loginFragment");
        fragmentTransaction.commit();
    }

    public void setSignupFragment() {
        SignupPageFragment signUpFragment = new SignupPageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.LSFragmentcontainer, signUpFragment, "signup fragment");
        fragmentTransaction.addToBackStack("signupFragment");
        fragmentTransaction.commit();
    }

    public void setResetPasswordFragment() {
        ForgotPasswordPageFragment forgotPasswordFragment = new ForgotPasswordPageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.LSFragmentcontainer, forgotPasswordFragment, "reset fragment");
        fragmentTransaction.addToBackStack("resetFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
