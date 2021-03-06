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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.AddJobsModel;
import com.mediax.mediaxapp.model.AddNewsModel;
import com.mediax.mediaxapp.model.SignInUserModel;
import com.mediax.mediaxapp.network.BaseVolleyRequest;
import com.mediax.mediaxapp.utils.Helper;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;
import com.mediax.mediaxapp.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddJobsActivity extends BaseActivity {

    ImageView imageJobs;

    EditText editTitle;
    EditText editDescription;

    Button buttonSave;

    TextView textChoosePhoto;
    TextView textTakePhoto;

    Bitmap bitmapNewsImage;


    int bytesAvailable;
    int bytesRead;
    String mimeType;

    private boolean isImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);
    }

    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_add_job));

        imageJobs = (ImageView) findViewById(R.id.imageJobs);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        textChoosePhoto = (TextView) findViewById(R.id.textChoosePhoto);
        textTakePhoto = (TextView) findViewById(R.id.textTakePhoto);
        buttonSave = (Button) findViewById(R.id.buttonSave);
    }

    @Override
    protected void initContext() {
        context = AddJobsActivity.this;
        currentActivity = AddJobsActivity.this;
    }

    @Override
    protected void initListners() {

        buttonSave.setOnClickListener(this);
        imageJobs.setOnClickListener(this);
        textChoosePhoto.setOnClickListener(this);
        textTakePhoto.setOnClickListener(this);
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

    }

    private boolean isMandatoryFields() {
        if (editTitle.getText().toString().isEmpty()) {
            editTitle.requestFocus();
            editTitle.setError(getResources().getString(R.string.empty_news_title));
            return false;
        } else if (editDescription.getText().toString().isEmpty()) {
            editDescription.requestFocus();
            editDescription.setError(getResources().getString(R.string.empty_news_description));
            return false;
        }

        initAddJobsModel();
        return true;
    }

    private void initAddJobsModel() {
        AddJobsModel.getInstance().setDesc(editDescription.getText().toString());
        AddJobsModel.getInstance().setTitle(editTitle.getText().toString());
        AddJobsModel.getInstance().setCreatedBy(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_NAME));


        if (AddJobsModel.getInstance().getImage() == null) {
            AddJobsModel.getInstance().setImage("noImage");
            AddJobsModel.getInstance().setImage(false);
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSave: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        if (isImage) {
                            uploadImageByVolley();

                        } else {
                            progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

                            addJobs();
                        }
                    }
                } else {

                }

                break;
            }

            case R.id.imageNews: {


                break;
            }

            case R.id.textChoosePhoto: {
                showFileChooser();
                break;
            }
            case R.id.textTakePhoto: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, AppConstants.REQUEST_TAG_Image_Capture);
                break;
            }
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstants.REQUEST_TAG_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_TAG_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmapNewsImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageJobs.setImageBitmap(bitmapNewsImage);

                isImage = true;
                AddJobsModel.getInstance().setImage(true);
                AddJobsModel.getInstance().setImage("img_jobs" + String.valueOf(Helper.generateRandomNoTimeMillis()) + ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == AppConstants.REQUEST_TAG_Image_Capture && resultCode == RESULT_OK) {
            bitmapNewsImage = (Bitmap) data.getExtras().get("data");
            imageJobs.setImageBitmap(bitmapNewsImage);
            AddJobsModel.getInstance().setImage(true);
            AddJobsModel.getInstance().setImage("img_jobs" + String.valueOf(Helper.generateRandomNoTimeMillis()) + ".jpg");
            AddJobsModel.getInstance().setZone(SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION));

        }
    }

    private void addJobs() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddJobsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddJobsRequest = new JSONObject(gson.toJson(AddJobsModel.getInstance()));
            Log.e("json add job request", jsonAddJobsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_ADD_JOBS = ADD_JOBS_URL;
        JsonObjectRequest addJobsRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_JOBS, jsonAddJobsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                AddJobsModel.getInstance().clear();
                try {
                    logTesting(getResources().getString(R.string.ntwk_response_add_job), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, message, message, getResources().getString(R.string.add_news_error), getResources().getString(R.string.add_news_error), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {
                        toast("Request submitted for approval", true);
                        setResult(RESULT_OK);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.error_add_job), true);
                logTesting(getResources().getString(R.string.error_add_job), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(addJobsRequest);
    }


    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";


        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] meetupImageBytesArray = Helper.getImageBytes(((BitmapDrawable) imageJobs.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        //    final String fileName = "image_" + timeMillis.toString() + ".jpg";


        final String fileName = AddJobsModel.getInstance().getImage();

        String imageUploadUrl = AppConstants.UPLOAD_NEWS_IMAGE;
        BaseVolleyRequest uploadMeetupImagerequest = new BaseVolleyRequest(1, imageUploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    Log.e("image response  is", response.toString() + jsonString);
                    if (bundle == null) {
                        bundle = new Bundle();

                    }

                    addJobs();

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

        MediaXApplication.getInstance().addToRequestQueue(uploadMeetupImagerequest);


    }


}
