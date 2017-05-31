package com.mediax.mediaxapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.AddNewsActivity;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.ForgotPasswordActivity;
import com.mediax.mediaxapp.adapter.NewsAdapter;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.NewsModel;
import com.mediax.mediaxapp.model.SignInUserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 28/04/2016.
 */
public class NewsFargment extends BaseFragment implements AppConstants {

    SearchView searchNews;
    RecyclerView recyclerNews;

    NewsAdapter newsAdapter;
    LinearLayoutManager managerNews;

    List<NewsModel> newsModelList;
    List<NewsModel> newsModelListComplete;

    Context context;

    Bundle bundle;

    String searchText;

    TextView textNoNews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.from(currentActivity).inflate(R.layout.fragment_news, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initViews() {

        context = getActivity();
        currentActivity = getActivity();


        searchNews = (SearchView) view.findViewById(R.id.searchNews);
        recyclerNews = (RecyclerView) view.findViewById(R.id.recyclerNews);
        textNoNews = (TextView) view.findViewById(R.id.textNoNews);

        managerNews = new LinearLayoutManager(currentActivity);

        newsModelList = new ArrayList<>();
        newsModelListComplete = new ArrayList<>();

        newsAdapter = new NewsAdapter(currentActivity, newsModelListComplete);

        recyclerNews.setLayoutManager(managerNews);
        recyclerNews.setAdapter(newsAdapter);
        searchText = "";
        settingNewsSearchBarListner();
        getNews();

    }


    private void settingNewsSearchBarListner() {
        //   Toast.makeText(currentActivity, "hide ", Toast.LENGTH_SHORT).show();
        searchNews.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchText = newText;
                    newsModelListComplete.clear();
                    getNews();
                } else {
                    newsModelListComplete.clear();
                    searchText = "";
                    getNews();
                }

                return true;
            }
        });
    }


    private void getNews() {

        //     ((BaseActivity) currentActivity).progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonNews = null;

        String URL_GET_NEWS = GET_NEWS_URL;
        if (!searchText.equals("")) {
            URL_GET_NEWS = URL_GET_NEWS + "?search_string=" + searchText + "&zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);
        }
        else
        {
            URL_GET_NEWS = URL_GET_NEWS + "?zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);

        }
        Log.e("url get news",URL_GET_NEWS);
        JsonObjectRequest getNewsRequest = new JsonObjectRequest(Request.Method.GET, URL_GET_NEWS, jsonNews, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //     ((BaseActivity) currentActivity).cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_news), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, getResources().getString(R.string.msg_news_error), getResources().getString(R.string.msg_news_error), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {
                        Log.e("no news called", "no");
                        if (message.equals(NO_NEWS_FOUND)) {
                            Log.e("no news called", "yes");
                            recyclerNewsInVisible();
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            recyclerNewsVisible();
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            List<NewsModel> newsModelList = Arrays.asList(gson.fromJson(message.toString(), NewsModel[].class));
                            newsModelListComplete.clear();
                            newsModelListComplete.addAll(newsModelList);
                            newsAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   ((BaseActivity)currentActivity). cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_fetching_news), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_fetching_news), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(getNewsRequest);
    }

    private void recyclerNewsInVisible() {
        recyclerNews.setVisibility(View.GONE);
        textNoNews.setVisibility(View.VISIBLE);
    }

    private void recyclerNewsVisible() {
        recyclerNews.setVisibility(View.VISIBLE);
        textNoNews.setVisibility(View.GONE);
    }

    @Override
    protected void initListners() {

    }


    @Override
    public void alertOkClicked() {

    }


    @Override
    protected void initContext() {

    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_news, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            ((BaseActivity) currentActivity).startActivity(currentActivity, AddNewsActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == currentActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAG_ADD_NEWS_ACTIVITY) {
                newsModelListComplete.clear();
                getNews();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
