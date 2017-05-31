package com.mediax.mediaxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.utils.QuickbloxLoginUtils;

public class AppAccessActivity extends BaseActivity {

    Button btnSignUp;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_access);


    }


    @Override
    protected void initContext() {

        currentActivity = AppAccessActivity.this;
        context = AppAccessActivity.this;

    }

    @Override
    protected void initViews() {

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
    }


    @Override
    protected void initListners() {
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

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
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSignUp: {
                startActivity(currentActivity, SignUpActivity.class, bundle, true, REQUEST_TAG_SIGN_UP_ACTIVITY, true, ANIMATION_SLIDE_UP);

                break;
            }
            case R.id.btnSignIn: {
                startActivity(currentActivity, SignInActivity.class, bundle, true, REQUEST_TAG_SIGN_IN_ACTIVITY, true, ANIMATION_SLIDE_UP);

                break;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_TAG_SIGN_IN_ACTIVITY: {
                  //  progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

                 //   QuickbloxLoginUtils.getInstance(currentActivity).quickbloxLogin();

                 /*   startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
                    finish();*/

                    break;
                }

                case REQUEST_TAG_SIGN_UP_ACTIVITY: {
                    progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

                    QuickbloxLoginUtils.getInstance(currentActivity).quickbloxLogin();
                 /*   startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
                    finish();
*/
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
