package com.mediax.mediaxapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.JobsModel;
import com.squareup.picasso.Picasso;

public class JobsDetailActivity extends BaseActivity implements AppConstants {

    ImageView imageJobs;
    TextView textJobs;
    TextView textJobsDescription;

    JobsModel jobsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_detail);


    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_job_detail));
        imageJobs = (ImageView) findViewById(R.id.imageJobs);
        textJobs = (TextView) findViewById(R.id.textJobs);
        textJobsDescription = (TextView) findViewById(R.id.textJobsDescription);
        jobsModel = getIntent().getExtras().getParcelable(JobsModel);

        if (jobsModel.getJobImage() != null && !jobsModel.getJobImage().equals(NO_IMAGE)) {

            String imagUrl = BASE_URL_IMAGES + jobsModel.getJobImage();

            ((BaseActivity) currentActivity).logTesting("jobs image url", imagUrl, Log.ERROR);

            Picasso.with(currentActivity).load(imagUrl).into(imageJobs);


        } else {
            Picasso.with(currentActivity).load(R.drawable.icon_jobs).into(imageJobs);
            //     imageJobs.setBackgroundResource(R.drawable.icon_jobs);
            // imageJobs.setBackgroundDrawable(currentActivity.getResources().getDrawable(R.drawable.icon_jobs));
        }

        textJobs.setText(jobsModel.getJobTitle());
        textJobsDescription.setText(jobsModel.getJobDesc());
    }

    @Override
    protected void initContext() {
        context = JobsDetailActivity.this;
        currentActivity = JobsDetailActivity.this;
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

    }

    @Override
    public void onClick(View view) {

    }


    public void shareJobs(Activity activity, String newsTitle, String newsDescription) {
        // Intent to share info
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // Info
        String text = newsTitle + "." + System.getProperty("line.separator");
        text = text + newsDescription + ".";


        // Image
      /*  Uri pathImagenPortada = (Uri) new Uri(jobsModel.getJobImage().toString());*/


        final SpannableString out0 = new SpannableString(newsTitle + System.getProperty("line.separator") + newsDescription);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        out0.setSpan(boldSpan, 0, newsTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


      /*  shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(new StringBuilder()
                .append("<p><b>" + newsTitle + "</b></p>").append(System.getProperty("line.separator"))
                .append("<small><p>" + newsDescription + "</p></small>")
                .toString()));*/

        shareIntent.putExtra(Intent.EXTRA_TITLE, newsTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

/*        shareIntent.putExtra(Intent.EXTRA_STREAM, pathImagenPortada);
        shareIntent.setType("image*//*");*/


        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(Intent.createChooser(shareIntent, "Share using.."));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jobs_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == R.id.btnShare) {
            shareJobs(currentActivity, jobsModel.getJobTitle(), jobsModel.getJobDesc());
        }
        return super.onOptionsItemSelected(item);
    }
}
