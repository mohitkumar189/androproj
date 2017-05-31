package com.mediax.mediaxapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.model.NewsModel;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends BaseActivity {

    ImageView imageNews;
    TextView textNews;
    TextView textNewsDescription;

    NewsModel newsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);


    }


    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_news_detail));
        imageNews = (ImageView) findViewById(R.id.imageNews);
        textNews = (TextView) findViewById(R.id.textNews);
        textNewsDescription = (TextView) findViewById(R.id.textNewsDescription);
        newsModel = getIntent().getExtras().getParcelable(NewsModel);

        if (newsModel.getNewsImage() != null && !newsModel.getNewsImage().equals(NO_IMAGE)) {

            String imagUrl = BASE_URL_IMAGES + newsModel.getNewsImage();


            Picasso.with(currentActivity).load(imagUrl).into(imageNews);
            ((BaseActivity) currentActivity).logTesting("news image url", imagUrl, Log.ERROR);
        } else {
            Picasso.with(currentActivity).load(R.drawable.icon_news).into(imageNews);

            //    imageNews.setBackgroundResource(R.drawable.icon_news);
            //  imageNews.setBackgroundDrawable(currentActivity.getResources().getDrawable(R.drawable.icon_news));
        }

        textNews.setText(newsModel.getNewsTitle());
        textNewsDescription.setText(newsModel.getNewsDesc());
    }

    @Override
    protected void initContext() {
        context = NewsDetailActivity.this;
        currentActivity = NewsDetailActivity.this;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == R.id.btnShare) {
            shareNews(currentActivity, newsModel.getNewsTitle(), newsModel.getNewsDesc());
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareNews(Activity activity, String newsTitle, String newsDescription) {
        // Intent to share info
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        // Info
        String text = newsTitle + "." + System.getProperty("line.separator");
        text = text + newsDescription + "." + System.getProperty("line.separator");


        // Image
      /*  Uri pathImagenPortada = (Uri) new Uri(jobsModel.getJobImage().toString());*/

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
/*        shareIntent.putExtra(Intent.EXTRA_STREAM, pathImagenPortada);
        shareIntent.setType("image*//*");*/


        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(Intent.createChooser(shareIntent, "Share using.."));
    }


}
