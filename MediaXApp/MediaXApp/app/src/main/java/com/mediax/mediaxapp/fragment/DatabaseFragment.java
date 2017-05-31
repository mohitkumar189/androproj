package com.mediax.mediaxapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.activity.AddEntryActivity;
import com.mediax.mediaxapp.activity.BaseActivity;
import com.mediax.mediaxapp.activity.EditUserActivity;
import com.mediax.mediaxapp.adapter.CompanyAdapter;
import com.mediax.mediaxapp.adapter.UserAdapter;
import com.mediax.mediaxapp.application.MediaXApplication;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.model.CompanyModel;
import com.mediax.mediaxapp.model.DatabaseModel;
import com.mediax.mediaxapp.model.SignUpUserModel;
import com.mediax.mediaxapp.model.UserModel;
import com.mediax.mediaxapp.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 28/04/2016.
 */
public class DatabaseFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, AppConstants {

    ImageView imageDatabase;

    RadioGroup radioGroupSearchFor;

    AppCompatRadioButton radioPerson;
    AppCompatRadioButton radioCompany;

    SearchView searchDatabase;

    RelativeLayout containerByName;
    RelativeLayout containerByCompany;

    TextView textSearchInstructions;

    RecyclerView recyclerDatabase;

    LinearLayoutManager managerDatabase;

    List<DatabaseModel> databaseModelList;


    LinearLayout containerSearchResults;

    TextView textNoDatabase;

    UserAdapter userAdapter;
    CompanyAdapter companyAdapter;

    LinearLayoutManager manager;

    boolean isPerson;

    String searchText;

    List<UserModel> userModelList;
    List<UserModel> userModelListComplete;


    List<CompanyModel> companyModelList;
    List<CompanyModel> companyModelListComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.from(currentActivity).inflate(R.layout.fragment_database, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    protected void initViews() {
        // imageDatabase = (ImageView) view.findViewById(R.id.imageDatabase);

        radioGroupSearchFor = (RadioGroup) view.findViewById(R.id.radioGroupSearchFor);

        radioPerson = (AppCompatRadioButton) view.findViewById(R.id.radioPerson);
        radioCompany = (AppCompatRadioButton) view.findViewById(R.id.radioCompany);
        radioPerson.setChecked(true);
        searchDatabase = (SearchView) view.findViewById(R.id.searchDatabase);

        containerByName = (RelativeLayout) view.findViewById(R.id.containerByName);
        containerByCompany = (RelativeLayout) view.findViewById(R.id.containerByCompany);

        textSearchInstructions = (TextView) view.findViewById(R.id.textSearchInstructions);

        recyclerDatabase = (RecyclerView) view.findViewById(R.id.recyclerDatabase);


        managerDatabase = new LinearLayoutManager(currentActivity);

        databaseModelList = new ArrayList<>();
        userModelList = new ArrayList<>();

        recyclerDatabase.setLayoutManager(managerDatabase);

        containerSearchResults = (LinearLayout) view.findViewById(R.id.containerSearchResults);

        textNoDatabase = (TextView) view.findViewById(R.id.textNoDatabase);

        manager = new LinearLayoutManager(currentActivity);
        recyclerDatabase.setLayoutManager(manager);
        isPerson = true;


        userModelList = new ArrayList<>();
        userModelListComplete = new ArrayList<>();

        companyModelList = new ArrayList<>();
        companyModelListComplete = new ArrayList<>();

        userAdapter = new UserAdapter(currentActivity, userModelListComplete);
        companyAdapter = new CompanyAdapter(currentActivity, companyModelListComplete);

        settingJobSearchBarListner();

    }


    private void settingJobSearchBarListner() {
        //   Toast.makeText(currentActivity, "hide ", Toast.LENGTH_SHORT).show();
        searchDatabase.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchText = newText;
                    hidingCoverImage();
                    performSearch();
                } else {
                    userModelListComplete.clear();
                    companyModelListComplete.clear();
                    showingCoverImage();
                    showSearchText();

                }

                return true;
            }
        });
    }

    private void performSearch() {
        containerSearchResults.setVisibility(View.VISIBLE);
        textSearchInstructions.setVisibility(View.GONE);

  /*      recyclerDatabase.setVisibility(View.GONE);
        textNoDatabase.setVisibility(View.VISIBLE);*/

        if (isPerson) {
            userModelListComplete.clear();
            searchingPersons();
        } else

        {
            companyModelListComplete.clear();
            searchingCompany();
        }

    }


    private void searchingPersons() {


        //     progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String URL_GET_PERSONS = GET_PERSONS_URL + searchText + "&zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);
        JsonObjectRequest searchPersonsRequest = new JsonObjectRequest(Request.Method.GET, URL_GET_PERSONS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //       cancelProgressDialog();
                try {
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {
                        if (message.equals(NO_PERSONS_FOUND)) {
                            hideDatabase(getResources().getString(R.string.no_persons_found));
                        } else {
                            showDatabase();
                            Log.e("all user response", "i" + message);
                            Log.e("all user response jso", "i" + response.toString());

                            Gson gson = new Gson();
                            userModelList = Arrays.asList(gson.fromJson(message, UserModel[].class));
                            userModelListComplete.clear();
                            userModelListComplete.addAll(userModelList);
                            recyclerDatabase.setAdapter(userAdapter);
                            userAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.error_get_persons), true);
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.error_get_persons), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(searchPersonsRequest);
    }

    private void hideDatabase(String noDatabaseText) {
        recyclerDatabase.setVisibility(View.GONE);
        textNoDatabase.setVisibility(View.VISIBLE);

        textNoDatabase.setText(noDatabaseText);
    }

    private void showDatabase() {
        recyclerDatabase.setVisibility(View.VISIBLE);
        textNoDatabase.setVisibility(View.GONE);

    }


    private void searchingCompany() {


        //     progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String URL_SEARCH_COMPANY = GET_COMPANY_URL + searchText + "&zone=" + SharedPreferenceUtils.getInstance(currentActivity).getString(AppConstants.REGION);
        JsonObjectRequest searchCompanyRequest = new JsonObjectRequest(Request.Method.GET, URL_SEARCH_COMPANY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   cancelProgressDialog();
                try {
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        ((BaseActivity) currentActivity).alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);

                    } else {

                        if (message.equals(NO_COMPANY_FOUND)) {
                            hideDatabase(getResources().getString(R.string.no_company_found));
                        } else {
                            showDatabase();
                            Log.e("all company response", "i" + message);
                            Gson gson = new Gson();
                            companyModelList = Arrays.asList(gson.fromJson(message, CompanyModel[].class));
                            companyModelListComplete.clear();
                            companyModelListComplete.addAll(companyModelList);

                            recyclerDatabase.setAdapter(companyAdapter);
                            companyAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    cancelProgressDialog();
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.error_get_company), true);
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.error_get_company), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MediaXApplication.getInstance().addToRequestQueue(searchCompanyRequest);
    }

    private void showSearchText() {
        containerSearchResults.setVisibility(View.GONE);
        textSearchInstructions.setVisibility(View.VISIBLE);

    }

    private void hidingCoverImage() {
        //   Toast.makeText(currentActivity, "hide called", Toast.LENGTH_SHORT).show();
//        imageDatabase.setVisibility(View.GONE);
    }

    public void showingCoverImage() {
        //   Toast.makeText(currentActivity, "show called", Toast.LENGTH_SHORT).show();
//        imageDatabase.setVisibility(View.VISIBLE);
    }


    @Override
    public void alertOkClicked() {

    }


    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {
        radioGroupSearchFor.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.radioGroupSearchFor: {


                if (radioGroupSearchFor.getCheckedRadioButtonId() == R.id.radioCompany) {

                    showSearchText();
                    searchDatabase.setQuery("", false);

                    containerByName.setVisibility(View.GONE);
                    containerByCompany.setVisibility(View.VISIBLE);

                    isPerson = false;
                    changeRadioButtonBg();
                } else if (radioGroupSearchFor.getCheckedRadioButtonId() == R.id.radioPerson) {

                    showSearchText();
                    searchDatabase.setQuery("", false);

                    containerByName.setVisibility(View.VISIBLE);
                    containerByCompany.setVisibility(View.GONE);
                    showSearchText();
                    isPerson = true;
                    changeRadioButtonBg();
                }

                break;
            }
        }
    }


    private void changeRadioButtonBg() {

        if (isPerson) {
            radioPerson.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_database_radio_selected));
            radioCompany.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_database_radio_unselected));

            radioPerson.setTextColor(getResources().getColor(R.color.white));
            radioCompany.setTextColor(getResources().getColor(R.color.bg_radio_container));
        } else {

            radioPerson.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_database_radio_unselected));
            radioCompany.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_database_radio_selected));

            radioPerson.setTextColor(getResources().getColor(R.color.bg_radio_container));
            radioCompany.setTextColor(getResources().getColor(R.color.white));
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        currentActivity.getMenuInflater().inflate(R.menu.menu_database_details, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btnAdd) {
            ((BaseActivity) currentActivity).startActivity(currentActivity, AddEntryActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);

        }
        return super.onOptionsItemSelected(item);
    }
}
