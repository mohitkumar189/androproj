/*
    @Auther MOHIT KUMAR
    Created on 13/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.expertsListFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.ExpertDetailsActivity;
import in.visheshagya.visheshagya.adapters.ExpertsDataAdapter;
import in.visheshagya.visheshagya.getterSetter.ExpertsData;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.resourses.DataTAGS;

public class CaListFragment extends Fragment {
    private String expertData = "";
    private ListView listViewExpert;
    private ArrayList<ExpertsData> expertDataList;
    private ExpertsDataAdapter expertDataAdapter;
    private int offset = 0;

    public CaListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("fragment", "onCreate() ca");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ca_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expertDataList = new ArrayList<ExpertsData>();
        listViewExpert = (ListView) getActivity().findViewById(R.id.caListView);
        // listViewExpert.setStackFromBottom(true);
        downloadExpertsData();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void downloadExpertsData() {
        String url = APIUrls.VISHESHAGYA_URL + APIUrls.SEARCH_EXPERTS + "expertCategoryId=" + 1 + "&offset=" + offset;
        //System.out.println("URL is " + url);
        new Downloader().execute(url);
    }

    private void extractJson() {
        try {
            JSONArray jsonArray = new JSONArray(expertData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonExpertObject = jsonArray.getJSONObject(i);
                //System.out.println("json Object "+jsonExpertObject);

                //////////////////////////////       Variables for expert data        ////////////////////////////////////

                String expertID = jsonExpertObject.getString(DataTAGS.EXPERT_ID);
                String expertName = jsonExpertObject.getString(DataTAGS.EXPERT_NAME);
                String expertProfileSmry = jsonExpertObject.getString(DataTAGS.EXPERT_PROFILE_SUMMARY);
                String expertCareerStart = jsonExpertObject.getString(DataTAGS.EXPERT_CAREER_START);
                String expertInistitute = jsonExpertObject.getString(DataTAGS.EXPERT_INSTITUTE);
                String expertCity = jsonExpertObject.getString(DataTAGS.EXPERT_CITY);
                String expertAudioFee = jsonExpertObject.getString(DataTAGS.EXPERT_AUDIO_FEE);
                String expertVideoFee = jsonExpertObject.getString(DataTAGS.EXPERT_VIDEO_FEE);
                String expertInPersonFee = jsonExpertObject.getString(DataTAGS.EXPERT_IN_PERSON_FEE);

                //////////////////////////////       Initialize list expert data        ////////////////////////////////////
                expertDataList.add(new ExpertsData(expertID, expertName, expertCareerStart, expertInistitute, expertCity, expertAudioFee, expertVideoFee, expertInPersonFee));
            }
            //JSONObject jsonObject = new JSONObject(expertData);
            updateAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter() {
        expertDataAdapter = new ExpertsDataAdapter(expertDataList, getActivity());
        expertDataAdapter.notifyDataSetChanged(); //notify adapter
        setupExperts();
    }

    private void setupExperts() {

        listViewExpert.setAdapter(expertDataAdapter);
        // expertDataAdapter.notifyDataSetChanged(); //notify adapter
        listViewExpert.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int lastCount = listViewExpert.getLastVisiblePosition();
                System.out.println("adapter size is " + expertDataAdapter.getCount() + " last count is " + lastCount);

                if (listViewExpert.getLastVisiblePosition() == expertDataAdapter.getCount() - 1) {
                    //Update new data here
                    // and then, notify your adapter
                    offset = offset + 50; // change offset value
                    downloadExpertsData(); // download data again and update adapter
                    //Toast.makeText(getActivity(),"hello last reached",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listViewExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String expertId = expertDataList.get(position).getExpertId().trim();
                ArrayList<String> expertData = new ArrayList<String>();
                expertData.add(0, expertDataList.get(position).getExpertName());
                expertData.add(1, expertDataList.get(position).getExpertProfileImageName());
                expertData.add(2, expertDataList.get(position).getExpertProfileSummary());
                expertData.add(3, expertDataList.get(position).getExpertCareerStartYear());
                expertData.add(4, expertDataList.get(position).getExpertInstituteName());
                expertData.add(5, expertDataList.get(position).getExpertCategoryName());
                expertData.add(6, expertDataList.get(position).getExpertCityName());
                expertData.add(7, expertDataList.get(position).getExpertSkills());
                expertData.add(8, expertDataList.get(position).getExpertAudioFee());
                expertData.add(9, expertDataList.get(position).getExpertVideoFee());
                expertData.add(10, expertDataList.get(position).getExpertMeetFee());

                Intent intent = new Intent(getActivity(), ExpertDetailsActivity.class);
                intent.putExtra("expertId", expertId);
                intent.putExtra("expertData", expertData);
                startActivity(intent);
                // startActivity(new Intent(getActivity(), DetailActivity.class).putExtra());
            }
        });
    }

    class Downloader extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String u = strings[0];
            String s = new JSONDownloader().jsonData(u);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing()) {
                expertData = s.toString();
                //System.out.println("output by async task "+s);
                extractJson();
                pDialog.dismiss();
            }
        }
    }
}

