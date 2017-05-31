package in.visheshagya.visheshagya.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.resourses.DataTAGS;

public class ExpertDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> mList;
    private ImageView profileImage;
    private Button bookingButton;
    private String expertId;
    private String expertDetails;
    private TextView expertName, expertCategory, expertCareerStart, expertVideoFee, expertAudioFee, expertMeetFee,
            expertSummary, expertProSummary, expertQualification, expertExperiance, expertAddress;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        expertId = intent.getStringExtra("expertId").trim();
        mList = intent.getStringArrayListExtra("expertData");
        final String url = APIUrls.VISHESHAGYA_URL + APIUrls.EXPERT_PROFILE + expertId;
        new DownloadExpertData().execute(url);
    }

    private void initializeComponents() {
        setContentView(R.layout.activity_expert_details);
        profileImage = (ImageView) findViewById(R.id.detailExpertPic);
        expertName = (TextView) findViewById(R.id.detailExpertName);
        expertCategory = (TextView) findViewById(R.id.detailExpertCategory);
        expertCareerStart = (TextView) findViewById(R.id.detailExpertCareerStart);
        expertVideoFee = (TextView) findViewById(R.id.detailExpertVideoFee);
        expertAudioFee = (TextView) findViewById(R.id.detailExpertAudioFee);
        expertMeetFee = (TextView) findViewById(R.id.detailExpertMeetFee);
        expertSummary = (TextView) findViewById(R.id.detailExpertSummary);
        expertProSummary = (TextView) findViewById(R.id.detailExpertProSummary);
        expertQualification = (TextView) findViewById(R.id.detailExpertQualification);
        expertExperiance = (TextView) findViewById(R.id.detailExpertExperience);
        expertAddress = (TextView) findViewById(R.id.detailExpertAddress);
        bookingButton = (Button) findViewById(R.id.detailExpertBookButton);
        bookingButton.setOnClickListener(this);
    }

    ////////////////////////// SET DATA FOR EXPERT DETAILVIEW///////////////////////
    private void setExpertDetails(String expertData) {

        try {
            JSONObject jsonObject = new JSONObject(expertDetails);
            String expertNameData = jsonObject.getString(DataTAGS.EXPERT_NAME).toString().trim();
            String expertCategoryData = jsonObject.getString(DataTAGS.EXPERT_CATEGORY).toString().trim();
            String expertCareerStartData = jsonObject.getString(DataTAGS.EXPERT_CAREER_START).toString().trim();
            String expertAudioFeeData = jsonObject.getString(DataTAGS.EXPERT_AUDIO_FEE).toString().trim();
            String expertVideoFeeData = jsonObject.getString(DataTAGS.EXPERT_VIDEO_FEE).toString().trim();
            String expertMeetFeeData = jsonObject.getString(DataTAGS.EXPERT_IN_PERSON_FEE).toString().trim();
            String expertSkillsData = jsonObject.getString(DataTAGS.EXPERT_SKILLS).toString().trim();
            String expertProSummaryData = jsonObject.getString(DataTAGS.EXPERT_PROFILE_SUMMARY).toString().trim();
            String expertExperianceData = jsonObject.getString(DataTAGS.EXPERT_EXPERIENCE).toString().trim();
            String expertCityData = jsonObject.getString(DataTAGS.EXPERT_CITY).toString().trim();
            String imu = jsonObject.getString(DataTAGS.EXPERT_PROFILE_IMAGE_NAME).toString().trim();

            ////////////////// setting expert data////////////////
            expertName.setText(expertNameData);
            expertCategory.setText(expertCategoryData);
            expertCareerStart.setText(expertCareerStartData);
            expertVideoFee.setText(expertVideoFeeData);
            expertAudioFee.setText(expertAudioFeeData);
            expertMeetFee.setText(expertMeetFeeData);
            expertSummary.setText(expertSkillsData);
            expertProSummary.setText(expertProSummaryData);
            expertExperiance.setText(expertExperianceData);
            expertAddress.setText(expertCityData);

            if (imu.length() > 10) {
                imageUrl = APIUrls.VISHESHAGYA + jsonObject.getString(DataTAGS.EXPERT_PROFILE_IMAGE_NAME).trim();
                new GetProfilePic(imageUrl, profileImage).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int res_id = view.getId();
        switch (res_id) {
            case R.id.detailExpertBookButton:
                showPopupMenu();
                break;
            default:
                break;
        }
        // startActivity(new Intent(this,BookAppointmentActivity.class));

    }

    private void showPopupMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, bookingButton);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                int menuPos;
                int res_id = item.getItemId();
                switch (res_id) {
                    case R.id.one:
                        menuPos = 8;
                        break;
                    case R.id.two:
                        menuPos = 9;
                        break;
                    case R.id.three:
                        menuPos = 10;
                        break;
                    default:
                        menuPos = 0;
                        break;
                }

                // OPEN BOOKING ACTIVITY HERE///
                Intent intent = new Intent(getApplicationContext(), BookAppointmentActivity.class);
                intent.putExtra("consultType", item.getTitle());
                intent.putExtra("consultAmt", Integer.parseInt(mList.get(menuPos)));
                intent.putExtra("consultantName", mList.get(0));
                intent.putExtra("expertId", expertId);
                startActivity(intent);
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private class DownloadExpertData extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        private String requestUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ExpertDetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            requestUrl = strings[0];
            String s = new JSONDownloader().jsonData(requestUrl);
            return s;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (pDialog.isShowing()) {
                    expertDetails = s.toString();
                    // System.out.println("experts details are "+s);
                    initializeComponents(); // Initialize components in layout
                    setExpertDetails(s);   // set updata on thecomponents
                    pDialog.dismiss();
                }
            } else {
                pDialog.dismiss();
            }

        }
    }

    private class GetProfilePic extends AsyncTask<Object, Object, Object> {
        private String requestUrl;
        private ImageView view;
        private Bitmap bitmap;

        private GetProfilePic(String requestUrl, ImageView view) {
            this.requestUrl = requestUrl;
            this.view = view;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (bitmap != null) {
                view.setImageBitmap(bitmap);
            }

        }
    }
}
