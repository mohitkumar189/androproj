package com.mediax.mediaxapp.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.CategoriesModel;
import com.mediax.mediaxapp.model.SignUpUserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.core.request.QueryRule;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    EditText editFullName;
    EditText editOfficialEmailId;
    EditText editPassword;
    EditText editBrand;
    EditText editDesignation;
    EditText editLinkedinProfileLink;
    EditText editMobileNo;

    AutoCompleteTextView autoCompleteCompanyName;

    Spinner spinnerIndustryType;
    Spinner spinnerZone;

    ArrayAdapter adapterIndustryType;
    ArrayAdapter adapterZone;

    List<String> listIndustryType;
    List<String> listZone;

    List<CategoriesModel> categoriesModelList;

    Button buttonSignUp;

    String id;


    String userId;
    int[] userQuickbloxId = new int[1];


    @Override
    protected void initViews() {
        editFullName = (EditText) findViewById(R.id.editFullName);
        editOfficialEmailId = (EditText) findViewById(R.id.editOfficialEmailId);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editBrand = (EditText) findViewById(R.id.editBrand);
        editDesignation = (EditText) findViewById(R.id.editDesignation);
        editLinkedinProfileLink = (EditText) findViewById(R.id.editLinkedinProfileLink);
        editMobileNo = (EditText) findViewById(R.id.editMobileNo);

        autoCompleteCompanyName = (AutoCompleteTextView) findViewById(R.id.autoCompleteCompanyName);

        spinnerIndustryType = (Spinner) findViewById(R.id.spinnerIndustryType);
        spinnerZone = (Spinner) findViewById(R.id.spinnerZone);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
    }

    @Override
    protected void initContext() {

        currentActivity = SignUpActivity.this;
        context = SignUpActivity.this;
    }

    @Override
    protected void initListners() {


        listIndustryType = new ArrayList<>();
        listZone = Arrays.asList(getResources().getStringArray(R.array.arrayZone));


        adapterIndustryType = new ArrayAdapter(currentActivity, R.layout.support_simple_spinner_dropdown_item, listIndustryType);
        adapterZone = new ArrayAdapter(currentActivity, R.layout.support_simple_spinner_dropdown_item, listZone);

        spinnerIndustryType.setAdapter(adapterIndustryType);
        spinnerZone.setAdapter(adapterZone);

        spinnerIndustryType.setOnItemSelectedListener(this);
        spinnerZone.setOnItemSelectedListener(this);

        buttonSignUp.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getCategories();
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {
        toHideKeyboard();
        switch (view.getId()) {
            case R.id.buttonSignUp: {
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        signUp();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                }
                break;
            }
        }

    }

    private boolean isMandatoryFields() {
        editFullName.setError(null);
        editOfficialEmailId.setError(null);
        editPassword.setError(null);
        editDesignation.setError(null);
        editMobileNo.setError(null);

        if (editFullName.getText().toString().isEmpty()) {

            editFullName.setError(getResources().getString(R.string.error_fullname_empty));
            editFullName.requestFocus();
            return false;
        } else if (!Validator.getInstance().isValidEmail(context, editOfficialEmailId.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, editOfficialEmailId.getText().toString());

            editOfficialEmailId.setError(emailError);
            editOfficialEmailId.requestFocus();
            return false;

        } else if (editPassword.getText().toString().isEmpty()) {

            editPassword.setError(getResources().getString(R.string.error_password_empty));
            editPassword.requestFocus();
            return false;

        } else if (spinnerIndustryType.getSelectedItemPosition() == 0) {
            spinnerIndustryType.requestFocus();
            alert(currentActivity, getResources().getString(R.string.error_industry_type_empty), getResources().getString(R.string.error_industry_type_empty), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);

            return false;

        } else if (editDesignation.getText().toString().isEmpty()) {

            editDesignation.setError(getResources().getString(R.string.error_designation_empty));
            editDesignation.requestFocus();
            return false;

        } else if (!Validator.getInstance().validateNumber(context, editMobileNo.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, editMobileNo.getText().toString());
            editMobileNo.setError(numberError);
            editMobileNo.requestFocus();
            return false;

        } else if (spinnerZone.getSelectedItemPosition() == 0) {
            spinnerZone.requestFocus();
            alert(currentActivity, getResources().getString(R.string.error_Zone_empty), getResources().getString(R.string.error_Zone_empty), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);

            return false;
        }
        initSignUpModel();
        return true;

    }


    private void initSignUpModel() {

        SignUpUserModel.getInstance().setName(editFullName.getText().toString());
        SignUpUserModel.getInstance().setEmail(editOfficialEmailId.getText().toString());
        SignUpUserModel.getInstance().setPassword(editPassword.getText().toString());
        SignUpUserModel.getInstance().setMobileNo(editMobileNo.getText().toString());
        SignUpUserModel.getInstance().setBrand(editBrand.getText().toString());
        SignUpUserModel.getInstance().setCategoryId(id);
        SignUpUserModel.getInstance().setDesignation(editDesignation.getText().toString());
        SignUpUserModel.getInstance().setLinkedinProfile(editLinkedinProfileLink.getText().toString());
        SignUpUserModel.getInstance().setRegion(spinnerZone.getSelectedItem().toString());


    }

    private void getCategories() {
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonSignUpRequest = null;
        final String URL_GET_CATEGORIES = GET_CATEGORIES_URL;
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.GET, URL_GET_CATEGORIES, jsonSignUpRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                String arrayMessage = null;
                try {
                    arrayMessage = response.getJSONArray(RESPONCE_MESSAGE).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                categoriesModelList = Arrays.asList(new Gson().fromJson(arrayMessage, CategoriesModel[].class));
                initIndustryTypeList(categoriesModelList);
                logTesting(getResources().getString(R.string.nwk_response_categories), response.toString(), Log.ERROR);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_categories), true);
                logTesting(getResources().getString(R.string.nwk_error_categories), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(signUpRequest);
    }


    private void signUp() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonSignUpRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonSignUpRequest = new JSONObject(gson.toJson(SignUpUserModel.getInstance()));
            Log.e("json sign up request", jsonSignUpRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_SIGN_UP = SIGNUP_URL;
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGN_UP, jsonSignUpRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    logTesting(getResources().getString(R.string.nwk_response_sign_up), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    userId = response.getString(USER_ID);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        cancelProgressDialog();
                        alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        createQuickbloxSession();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_sign_up), true);
                logTesting(getResources().getString(R.string.nwk_error_sign_up), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(signUpRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerIndustryType: {
                id = categoriesModelList.get(adapterView.getSelectedItemPosition()).getId();
                break;
            }
            case R.id.spinnerZone: {
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void initIndustryTypeList(List<CategoriesModel> categoriesModelList) {
        if (listIndustryType == null) {
            listIndustryType = new ArrayList<>();
        } else {
            listIndustryType.clear();
        }

        listIndustryType.add(getResources().getString(R.string.label_select_industry_type));
        if (categoriesModelList != null) {
            for (int i = 0; i < categoriesModelList.size(); i++) {
                listIndustryType.add(categoriesModelList.get(i).getName());
            }
        }
        adapterIndustryType.notifyDataSetChanged();
    }


    private void createQuickbloxSession() {
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                quickbloxSignUp(editOfficialEmailId.getText().toString(), editOfficialEmailId.getText().toString());

            }

            @Override
            public void onError(QBResponseException error) {
                // errors
                log("error in creating quickblox ession", error.toString(), Log.ERROR);
            }
        });
    }


    private void quickbloxSignUp(String username, String password) {

        // Register new user
        final QBUser user = new QBUser(username, password);
        user.setFullName(editFullName.getText().toString());
        //  user.setEmail(editOfficialEmailId.getText().toString());

        QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {

                userQuickbloxId[0] = user.getId();

                loginSuperUser();
                log("new user id", "" + userQuickbloxId[0], Log.ERROR);

            }

            @Override
            public void onError(QBResponseException error) {
                // error

                cancelProgressDialog();

                log("error in new user sign up", error.toString(), Log.ERROR);
            }
        });


    }


    private void loginSuperUser() {
        final QBUser user = new QBUser(SUPER_USERNAME, SUPER_PASSWORD);
        QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                loginUserToChat();
            }

            @Override
            public void onError(QBResponseException e) {
                cancelProgressDialog();
                log("error in super user sign in", e.toString(), Log.ERROR);
                // error
            }
        });

    }


    public void loginUserToChat() {

        final QBUser superUser = new QBUser();
        superUser.setLogin(SUPER_USERNAME);
        superUser.setPassword(SUPER_PASSWORD);
        superUser.setId(SUPER_USER_ID);
        Log.e("come under", "login chat");


        class LoginChat extends AsyncTask<String, Boolean, Boolean> {

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    QBChatService.getInstance().login(superUser);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                } catch (SmackException e) {
                    e.printStackTrace();
                    Log.e("chat login", e.toString());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                //  ChatService.getInstance().addConnectionListener(chatConnectionListener);
                if (aBoolean) {
                    getParticularDialog(spinnerZone.getSelectedItem().toString());
                    Log.e("chat login", "successfull");

                } else {
                    cancelProgressDialog();
                    Log.e("chat login", "error");
                }

            }
        }


        if (QBChatService.getInstance().isLoggedIn()) {
            Log.e("chat alreay login", "yes");
            getParticularDialog(spinnerZone.getSelectedItem().toString());
        } else {
            new LoginChat().execute();

        }

    }

    private void getParticularDialog(String zone) {


        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();

        requestBuilder.addRule("name", QueryRule.EQ, zone);

    /*    QueryRule genericQueryRule = new QueryRule("name", zone);

        ArrayList<GenericQueryRule> genericQueryRules = new ArrayList<>();

        genericQueryRules.add(genericQueryRule);
        requestBuilder.setRules(genericQueryRules);*/

        QBChatService.getChatDialogs(null, requestBuilder, new QBEntityCallback<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                int totalEntries = args.getInt("total_entries");
                if (totalEntries > 0) {

                    log("total dialog", "" + "is" + totalEntries, Log.ERROR);
                    log("dialog id", "" + "is" + dialogs.get(0).getDialogId(), Log.ERROR);
                    log("dialog occupants", "" + "is" + dialogs.get(0).getOccupants(), Log.ERROR);
                    addingNewUserToGroup(dialogs.get(0));
                } else {
                    log("total dialog", "" + "is" + totalEntries, Log.ERROR);
                }

            }

            @Override
            public void onError(QBResponseException errors) {
                log("error in getting dialog", errors.toString(), Log.ERROR);
            }
        });


    }


    private void addingNewUserToGroup(QBDialog qbDialog) {
        QBRequestUpdateBuilder requestBuilder = new QBRequestUpdateBuilder();
        requestBuilder.push("occupants_ids", userQuickbloxId[0]); // add another users


        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.updateDialog(qbDialog, requestBuilder, new QBEntityCallback<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {

                log("user added to group ", " " + userQuickbloxId[0], Log.ERROR);
                SharedPreferenceUtils.getInstance(currentActivity).putString(USER_PROFILE_NAME, editFullName.getText().toString());


                SharedPreferenceUtils.getInstance(currentActivity).putString(USER_ID, userId);
                SharedPreferenceUtils.getInstance(currentActivity).putString(USER_NAME, editOfficialEmailId.getText().toString());
                SharedPreferenceUtils.getInstance(currentActivity).putBoolean(IS_LOGIN, true);
                SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.REGION, spinnerZone.getSelectedItem().toString());


                chatLogOut();
                signOut();
            }

            @Override
            public void onError(QBResponseException errors) {
                log("error in adding to group", errors.toString(), Log.ERROR);
            }
        });
    }

    private void chatLogOut() {
        QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                log("chat logout success", "yes", Log.ERROR);

            }

            @Override
            public void onError(QBResponseException e) {
                log("error in chat   logout", e.toString(), Log.ERROR);
            }
        });
    }


    private void signOut() {
        log("come under signout", "yes", Log.ERROR);
        QBUsers.signOut(new QBEntityCallback() {


            @Override
            public void onSuccess(Object o, Bundle bundle) {
                log("signout success", "yes", Log.ERROR);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException errors) {
                log("error in super user logout", errors.toString(), Log.ERROR);
            }
        });
    }


    private void loginNewUser() {
        final QBUser user = new QBUser(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME), SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
        QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                cancelProgressDialog();
                log("error in new user sign in", e.toString(), Log.ERROR);
                // error
            }
        });

    }

}
