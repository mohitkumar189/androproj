package com.quickbloxchat.samplequickbloxchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.quickbloxchat.samplequickbloxchat.R;
import com.quickbloxchat.samplequickbloxchat.constant.AppConstants;
import com.quickbloxchat.samplequickbloxchat.utils.Helper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullImageViewActivity extends com.quickbloxchat.samplequickbloxchat.activity.BaseActivity implements AppConstants {

    Activity currentActivity;


    ImageView imageViewFull;

    Toolbar toolbar;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ful_image_view);

        String url = getIntent().getExtras().getString(IMAGE_URL);


        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        imageViewFull = (ImageView) findViewById(R.id.imageViewFull);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MediaX");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        currentActivity = FullImageViewActivity.this;

/*        resize(Helper.toPixels(currentActivity, 218), Helper.toPixels(currentActivity, 192)).centerCrop()*/

        Picasso.with(currentActivity).load(url).into(imageViewFull, new Callback() {
            @Override
            public void onSuccess() {

                progressBar.setVisibility(View.GONE);
                imageViewFull.setVisibility(View.VISIBLE);
                Log.e("image load success", "yes");
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                imageViewFull.setVisibility(View.VISIBLE);
                Log.e("image load failed", "yes");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
