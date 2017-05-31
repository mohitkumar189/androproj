package in.visheshagya.visheshagya.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.adapters.AppointmentsListAdapter;
import in.visheshagya.visheshagya.getterSetter.AppointmentsData;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class AppointmentDetailsActivity extends AppCompatActivity {
    ListView appointmentsList;
    LinearLayout appointTitles;
    Context ctx;
    ArrayList<AppointmentsData> appointmentData;
    AppointmentsListAdapter appointmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        ctx = this;
        init();
    }

    private void init() {
        SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(this);
        String clientId = sharedPrefsClass.getClientId();
        appointmentData = new ArrayList<AppointmentsData>();
        appointmentsList = (ListView) findViewById(R.id.appointmentsList);
        appointTitles = (LinearLayout) findViewById(R.id.appLin);
        appointTitles.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
        String url = APIUrls.VISHESHAGYA_URL + APIUrls.APPOINTMENTS_LIST + "clientId=" + clientId;
        new DownloadAppointments().execute(url);
    }

    private void extractJson(String s) {
        //System.out.println("json to be processed "+s);

        String expertName, appointmentDate, consultationTypeName, consultationStatusName;
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                // System.out.println("data length is "+jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                expertName = jsonObject.getString("expertName");
                appointmentDate = jsonObject.getString("appointmentDate");
                consultationTypeName = jsonObject.getString("consultationTypeName");
                consultationStatusName = jsonObject.getString("consultationStatusName");
                appointmentData.add(new AppointmentsData(expertName, appointmentDate, consultationTypeName, consultationStatusName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //appointmentData.add(new AppointmentsData("","","","",""));
        setupList();
    }

    private void setupList() {
        appointmentsAdapter = new AppointmentsListAdapter(appointmentData, this);
        appointmentsList.setAdapter(appointmentsAdapter);
    }

    private class DownloadAppointments extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = new JSONDownloader().jsonData(strings[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
            if (s != null) {
                extractJson(s);
            } else {
                Toast.makeText(AppointmentDetailsActivity.this, "No appointment found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
