/*
    @Auther MOHIT KUMAR
    Created on 21/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.resourses.APIUrls;

public class AfterBookingStatus extends AppCompatActivity implements View.OnClickListener {
    String status = "", appointId = "";
    private LinearLayout failedLauyout, succesLayout;
    private TextView expertName, consType, consAmount, consTime, consDate, tranxId, appoId;
    private Button goToHome, goToAppointment;
    private String experttName, conssType, conssAmount, conssTime, conssDate, tranxxId;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_booking_status);
        // System.out.println("After booking status class is called");
        Intent intent = getIntent();
        status = intent.getStringExtra("result");
        appointId = intent.getStringExtra("appoId");
        conssAmount = intent.getStringExtra("amount");
        experttName = intent.getStringExtra("expertName");
        conssType = intent.getStringExtra("consType");
        conssTime = intent.getStringExtra("consTime");
        conssDate = intent.getStringExtra("consDate");
        tranxxId = intent.getStringExtra("paymentId");
        System.out.println("parameters received " + status + " " + appointId + " " + conssAmount + " " + experttName + " " + conssType + " " + conssDate + " " + tranxxId);
        initializeComponents();
    }

    private void initializeComponents() {
        failedLauyout = (LinearLayout) findViewById(R.id.failedLayout);
        succesLayout = (LinearLayout) findViewById(R.id.succesLayout);
        goToHome = (Button) findViewById(R.id.goBackToHome);
        goToAppointment = (Button) findViewById(R.id.goToAppointment);
        goToHome.setOnClickListener(this);
        goToAppointment.setOnClickListener(this);
        expertName = (TextView) findViewById(R.id.expertName);
        consType = (TextView) findViewById(R.id.consultType);
        consDate = (TextView) findViewById(R.id.appointDate);
        consTime = (TextView) findViewById(R.id.appointTime);
        consAmount = (TextView) findViewById(R.id.consultAmount);
        tranxId = (TextView) findViewById(R.id.tranxId);
        appoId = (TextView) findViewById(R.id.consultId);
        failedLauyout.setVisibility(View.GONE);
        succesLayout.setVisibility(View.GONE);

        // url for success appointment
        String url = APIUrls.VISHESHAGYA_URL + APIUrls.SUCCESS_APPOINTMENT + "appointmentId=" + appointId + "&txdId=" + tranxxId + "&status=" + status;
        switch (status) {
            case "success":
                new SetBookingStatus().execute(url);
                break;
            case "failed":
                if (succesLayout.getVisibility() == View.VISIBLE) {
                    succesLayout.setVisibility(View.GONE);
                    failedLauyout.setVisibility(View.VISIBLE);
                } else {
                    failedLauyout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                succesLayout.setVisibility(View.GONE);
                failedLauyout.setVisibility(View.GONE);
                break;
        }
    }

    // set text view
    private void setupData() {
        // set success layout
        if (failedLauyout.getVisibility() == View.VISIBLE) {
            failedLauyout.setVisibility(View.GONE);
            succesLayout.setVisibility(View.VISIBLE);
        } else {
            succesLayout.setVisibility(View.VISIBLE);
        }

        // set values
        expertName.setText(experttName);
        consType.setText(conssType);
        consAmount.setText(conssAmount);
        consTime.setText(conssTime);
        consDate.setText(conssDate);
        tranxId.setText(tranxxId);
        appoId.setText(appointId);
    }

    private void extractJson(String jsonData) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            int success = (int) jsonObject.get("success");
            if (success == 1) {
                Toast.makeText(AfterBookingStatus.this, "Successfully booked", Toast.LENGTH_SHORT).show();
                setupData();
            } else {
                Toast.makeText(AfterBookingStatus.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int res_id = view.getId();
        switch (res_id) {
            case R.id.goBackToHome:
                startActivity(new Intent(AfterBookingStatus.this, HomeActivity.class));
                break;
            case R.id.goToAppointment:
                break;
        }
    }

    private class SetBookingStatus extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String response = new JSONDownloader().jsonData(url);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                // System.out.println("output by async task " + s);
                progressDialog.dismiss();
                extractJson(s);
            }
        }
    }
}
