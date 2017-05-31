package com.mediax.mediaxapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.model.AddNewsModel;
import com.mediax.mediaxapp.model.ProfileDetailsModel;
import com.mediax.mediaxapp.model.SignUpUserModel;
import com.mediax.mediaxapp.network.BaseVolleyRequest;
import com.mediax.mediaxapp.utils.Helper;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;
import com.mediax.mediaxapp.widgets.CircularImageView;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    EditText editFullName;
    EditText editOfficialEmailId;
    EditText editMobileNo;
    EditText editStatusMessage;

    Button buttonSaveChanges;

    String userId;

    ProfileDetailsModel profileDetailsModel;

    Bitmap bitmapProfileImage;

    int bytesAvailable;
    int bytesRead;
    String mimeType;


    CircularImageView circularProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = SharedPreferenceUtils.getInstance(currentActivity).getString(USER_ID);

        getProfileDetails();
    }


    @Override
    protected void initViews() {
        editFullName = (EditText) findViewById(R.id.editFullName);
        editOfficialEmailId = (EditText) findViewById(R.id.editOfficialEmailId);
        editMobileNo = (EditText) findViewById(R.id.editMobileNo);
        circularProfileImage = (CircularImageView) findViewById(R.id.circularProfileImage);
        editStatusMessage = (EditText) findViewById(R.id.editStatusMessage);
        buttonSaveChanges = (Button) findViewById(R.id.buttonSaveChanges);
        buttonSaveChanges.setOnClickListener(this);
        circularProfileImage.setOnClickListener(this);
    }

    @Override
    protected void initContext() {
        context = ProfileActivity.this;
        currentActivity = ProfileActivity.this;
    }

    @Override
    protected void initListners() {

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
    public void onAlertClicked(int alertType) {
        switch (alertType) {
            case ALERT_TYPE_IMAGE_UPLOAD: {
                uploadImageByVolley();
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSaveChanges: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        updateProfile();
                    } else {
                        alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    }
                }


                break;
            }
            case R.id.circularProfileImage: {
                showFileChooser();

                break;
            }
        }

    }


    private void getProfileDetails() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        final String URL_SIGN_UP = GET_USER_DETAILS + "?user_id=" + userId;
        Log.e("url profile details", URL_SIGN_UP);
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.GET, URL_SIGN_UP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();

                Log.e("profile details json", response.toString());
                try {
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getResources().getString(R.string.alert_title_oops), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        Gson gson = new Gson();
                        profileDetailsModel = gson.fromJson(message, ProfileDetailsModel.class);

                        profileDetailsModel.setUserId(userId);
                        toSetProfileDetails();
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
                Map<String, String> map = new HashMap<>();
                //  map.put("user_id", userId);
                return map;
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(signUpRequest);
    }


    private void updateProfile() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonUpdateUser = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonUpdateUser = new JSONObject(gson.toJson(profileDetailsModel));
            Log.e("json profUpdate request", jsonUpdateUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_UPDATE_USER = UPDATE_USER_URL;
        JsonObjectRequest profileUpdateRequest = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_USER, jsonUpdateUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_sign_up), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        toast(getResources().getString(R.string.message_profile_updated), true);

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

        MediaXApplication.getInstance().addToRequestQueue(profileUpdateRequest);
    }

    private void toSetProfileDetails() {

        editFullName.setText(profileDetailsModel.getName());
        editOfficialEmailId.setText(profileDetailsModel.getEmail());
        editMobileNo.setText(profileDetailsModel.getMobile_no());
        editStatusMessage.setText(profileDetailsModel.getStatusMessage());
        // editStatusMessage.setText(profileDetailsModel.get);

        if (profileDetailsModel.getUserImage() != null && !profileDetailsModel.getUserImage().equals("")) {
            Picasso.with(currentActivity).load(BASE_URL_IMAGES + profileDetailsModel.getUserImage()).into(circularProfileImage);
        }


    }

    private void toUpdateProfileModel() {
        profileDetailsModel.setName(editFullName.getText().toString());
        profileDetailsModel.setMobile_no(editMobileNo.getText().toString());
        profileDetailsModel.setStatusMessage(editStatusMessage.getText().toString());
    }

    private boolean isMandatoryFields() {
        editFullName.setError(null);
        editOfficialEmailId.setError(null);
        editMobileNo.setError(null);

        if (editFullName.getText().toString().isEmpty()) {
            editFullName.setError(getResources().getString(R.string.error_fullname_empty));
            editFullName.requestFocus();
            return false;

        } else if (editOfficialEmailId.getText().toString().isEmpty()) {
            editOfficialEmailId.requestFocus();
            return false;
        } else if (!Validator.getInstance().validateNumber(context, editMobileNo.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, editMobileNo.getText().toString());
            editMobileNo.setError(numberError);
            editMobileNo.requestFocus();
            return false;

        } else if (editStatusMessage.getText().toString().isEmpty()) {
            editStatusMessage.setError(getResources().getString(R.string.error_status_message));
            editStatusMessage.requestFocus();

        }
        toUpdateProfileModel();
        return true;
    }


    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";


        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] meetupImageBytesArray = Helper.getImageBytes(((BitmapDrawable) circularProfileImage.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "profileImage_" + timeMillis.toString() + ".jpg";


        String imageUploadUrl = com.mediax.mediaxapp.constant.AppConstants.UPLOAD_NEWS_IMAGE;
        BaseVolleyRequest uploadProfileImageRequest = new BaseVolleyRequest(1, imageUploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    Log.e("image response  is", response.toString() + jsonString);
                    if (bundle == null) {
                        bundle = new Bundle();

                    }
                    updateImageUrl(fileName);
                    //addNews();

                } catch (UnsupportedEncodingException e) {
                    cancelProgressDialog();
                    e.printStackTrace();
                }
                Log.e("image response  is", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("error uploading image", true);
                Log.e("image upload error is", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                mimeType = "multipart/form-data;boundary=" + boundary;
                return mimeType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);


                try {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    ByteArrayInputStream fileInputStream = new ByteArrayInputStream(meetupImageBytesArray);
                    bytesAvailable = fileInputStream.available();

                    int bufferSize = Math.min(meetupImageBytesArray.length, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }


                    //    dos.write(meetupImageBytesArray, 0, bufferSize);
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return meetupImageBytesArray;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Long timeMillis = System.currentTimeMillis();
                Map<String, String> params = new HashMap<String, String>();
                params.put("Connection", "Keep-Alive");
                params.put("ENCTYPE", "multipart/form-data");
                params.put("accept", "application/json");
                params.put("uploaded_file", fileName);
                params.put("Content-Type", "multipart/form-data;boundary=" + boundary);
                return params;

            }

        };

        MediaXApplication.getInstance().addToRequestQueue(uploadProfileImageRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.mediax.mediaxapp.constant.AppConstants.REQUEST_TAG_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmapProfileImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                circularProfileImage.setImageBitmap(bitmapProfileImage);
                alert(currentActivity, getResources().getString(R.string.alert_message_upload_image), getResources().getString(R.string.alert_message_upload_image), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_IMAGE_UPLOAD);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == com.mediax.mediaxapp.constant.AppConstants.REQUEST_TAG_Image_Capture && resultCode == RESULT_OK) {
            bitmapProfileImage = (Bitmap) data.getExtras().get("data");
            circularProfileImage.setImageBitmap(bitmapProfileImage);


        }
    }


    private void updateImageUrl(final String imageName) {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonUpdateUser = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonUpdateUser = new JSONObject();
            jsonUpdateUser.put("email", SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));
            jsonUpdateUser.put("image_name", imageName);


            Log.e("json profUpdate request", jsonUpdateUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_PROFILE_PIC = UPLOAD_PROFILE_PIC;
        JsonObjectRequest profileUpdateRequest = new JsonObjectRequest(Request.Method.POST, URL_PROFILE_PIC, jsonUpdateUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_sign_up), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_IMAGE, imageName);
                        toast(getResources().getString(R.string.message_image_updated), true);

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

        MediaXApplication.getInstance().addToRequestQueue(profileUpdateRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), com.mediax.mediaxapp.constant.AppConstants.REQUEST_TAG_PICK_IMAGE);
    }


}
