package in.visheshagya.visheshagya.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edName, edEmail, edMobile, eDob, ePanNumber,
            edAdLine1, edAdLine2, edCity, edCountry, edPinCode;
    private Button resetPswdBtn, updateInfoBtn;
    private TextView tvEmail;
    private ProgressDialog pDialog;
    private RadioButton radioMale, radioFemale;
    private String userName, userEmail, userGender, userMobile, userDob, userAdLine1, userAdLine2, userCity, userCountry, userPincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialize();
    }

    private void initialize() {
        SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(UserProfileActivity.this);
        String clientId = sharedPrefsClass.getClientId();
        edName = (EditText) findViewById(R.id.pUserName);
        tvEmail = (TextView) findViewById(R.id.pUserEmail);
        edMobile = (EditText) findViewById(R.id.pUserMobile);
        eDob = (EditText) findViewById(R.id.pUserDob);
        ePanNumber = (EditText) findViewById(R.id.pUserPanNo);
        edAdLine1 = (EditText) findViewById(R.id.pUserAddLine1);
        edAdLine2 = (EditText) findViewById(R.id.pUserAddLine2);
        edCity = (EditText) findViewById(R.id.pUserCity);
        edCountry = (EditText) findViewById(R.id.pUserCountry);
        edPinCode = (EditText) findViewById(R.id.pUserPincode);
        resetPswdBtn = (Button) findViewById(R.id.pUserResetPswd);
        updateInfoBtn = (Button) findViewById(R.id.pUserUpdateProfile);
        radioMale = (RadioButton) findViewById(R.id.pUserGenderMale);
        radioFemale = (RadioButton) findViewById(R.id.pUserGenderFemale);

        // set onclick listener
        resetPswdBtn.setOnClickListener(UserProfileActivity.this);
        updateInfoBtn.setOnClickListener(UserProfileActivity.this);
        String url = APIUrls.VISHESHAGYA_URL + APIUrls.CLIENT_PROFILE + clientId; //url for data downloading
        try {
            new DowloadUserData().execute(url).get();// hit url for downloading data
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void setUserDetails() {

        edName.setText(userName.trim());
        tvEmail.setText(userEmail.trim());
        eDob.setText(userDob.trim());
        edMobile.setText(userMobile.trim());
        edAdLine1.setText(userAdLine1.trim());
        edAdLine2.setText(userAdLine2.trim());
        edCity.setText(userCity.trim());
        edCountry.setText(userCountry.trim());
        edPinCode.setText(userPincode.trim());

        if (userGender.equals("1")) {
            radioMale.setChecked(true);
            radioFemale.setChecked(false);
        } else {
            radioMale.setChecked(false);
            radioFemale.setChecked(true);
        }
    }

    private void extractJson(String json) {
        System.out.println("user data is " + json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            userName = jsonObject.getString("clientName");
            userEmail = jsonObject.getString("clientEmailId");
            userMobile = jsonObject.getString("clientMobileNumber");
            userDob = jsonObject.getString("clientDOB");
            userAdLine1 = jsonObject.getString("clientAddressLine1");
            userAdLine2 = jsonObject.getString("clientAddressLine2");
            userCity = jsonObject.getString("clientCity");
            userCountry = jsonObject.getString("clientCountry");
            userPincode = jsonObject.getString("clientPincode");
            userGender = jsonObject.getString("clientGender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setUserDetails();
    }

    @Override
    public void onClick(View view) {
        int res_id = view.getId();
        switch (res_id) {
            case R.id.pUserResetPswd:
                createDialog();
                break;
            case R.id.pUserUpdateProfile:
                break;
        }
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Change password");   // Set the title
        dialog.setContentView(R.layout.dialog_change_password); // inflate the layout
        dialog.show();
        final EditText edOldPass, edNewPass, edNewConPass;
        Button btnUpdatePass;

        edOldPass = (EditText) dialog.findViewById(R.id.oldPasswordInput);
        edNewPass = (EditText) dialog.findViewById(R.id.newPasswordInput);
        edNewConPass = (EditText) dialog.findViewById(R.id.newConfirmPasswordInput);
        btnUpdatePass = (Button) dialog.findViewById(R.id.changePassBtn);

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String oldPass, newPass, newConPass;
                oldPass = edOldPass.getText().toString().trim();
                newPass = edNewPass.getText().toString().trim();
                newConPass = edNewConPass.getText().toString().trim();

                if (newPass.length() >= 5 && newConPass.length() >= 5 && newPass.equals(newConPass)) {
                    Toast.makeText(UserProfileActivity.this, "right", Toast.LENGTH_SHORT).show();
                    String url = APIUrls.UPDATE_PASSWORD + "userId=" + new SharedPrefsClass(UserProfileActivity.this).getClientId()
                            + "&currentPassword=" + oldPass + "&newPassword=" + newPass + "&role=" + 2;
                    System.out.println("url : "+url);
                    new UpdatePassword().execute(url);
                } else {
                    Toast.makeText(UserProfileActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class DowloadUserData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("client profile url is " + strings[0]);
            String s = new JSONDownloader().jsonData(strings[0]);
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                pDialog.cancel();
                extractJson(s);
            } else {
                pDialog.cancel();
                // Toast.makeText(UserProfileActivity.this,"No response...",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class UpdatePassword extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            System.out.println("pass change status : " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String success = jsonObject.getString("success");
                String message = jsonObject.getString("message");
                if (success.equals("1")) {
                    Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            return new JSONDownloader().jsonData(strings[0]);
        }
    }
}
