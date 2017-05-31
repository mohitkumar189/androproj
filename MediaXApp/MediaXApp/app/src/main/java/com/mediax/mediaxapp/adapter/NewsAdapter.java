package com.mediax.mediaxapp.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.JobsDetailActivity;
import com.mediax.mediaxapp.activity.NewsDetailActivity;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.NewsModel;
import com.mediax.mediaxapp.widgets.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mayank on 28/04/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements AppConstants {

    View view;
    Activity currentActivity;
    List<NewsModel> newsModelList;
    Bundle bundle;

    public NewsAdapter(Activity currentActivity, List<NewsModel> newsModelList) {
        this.currentActivity = currentActivity;
        this.newsModelList = newsModelList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(currentActivity).inflate(R.layout.item_news, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        if (newsModelList.get(pos).getNewsImage() != null && !newsModelList.get(pos).getNewsImage().equals(NO_IMAGE)) {

            String imagUrl = BASE_URL_IMAGES + newsModelList.get(pos).getNewsImage();

            ((BaseActivity) currentActivity).logTesting("news image url", imagUrl, Log.ERROR);

            Picasso.with(currentActivity).load(imagUrl).into(holder.imageNews);


        } else {
            Picasso.with(currentActivity).load(R.drawable.icon_news).into(holder.imageNews);
            //   holder.imageNews.setBackgroundResource(R.drawable.icon_news);
        }


        holder.textNews.setText(newsModelList.get(pos).getNewsTitle());
        holder.textNewsDescription.setText(newsModelList.get(pos).getNewsDesc());

        holder.containerNewsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putParcelable(NewsModel, newsModelList.get(pos));
                ((BaseActivity) currentActivity).startActivity(currentActivity, NewsDetailActivity.class, bundle, false, AppConstants.REQUEST_TAG_NO_RESULT, false, AppConstants.ANIMATION_SLIDE_UP);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNews;
        TextView textNewsDescription;
        CircularImageView imageNews;
        LinearLayout containerNewsDetails;

        public ViewHolder(View itemView) {
            super(itemView);

            textNews = (TextView) itemView.findViewById(R.id.textNews);
            textNewsDescription = (TextView) itemView.findViewById(R.id.textNewsDescription);
            imageNews = (CircularImageView) itemView.findViewById(R.id.imageNews);
            containerNewsDetails = (LinearLayout) itemView.findViewById(R.id.containerNewsDetails);
        }
    }
}