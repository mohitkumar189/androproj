/*
    @Auther MOHIT KUMAR
    Created on 19/08/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.BookAppointmentActivity;
import in.visheshagya.visheshagya.getterSetter.ExpertsData;

public class ExpertsDataAdapter extends BaseAdapter {

    ////variables for data
    public ArrayList<ExpertsData> expertsData;
    LayoutInflater inflater = null;
    private Context mcontext;

    public ExpertsDataAdapter(ArrayList<ExpertsData> expertsData, Context mcontext) {
        this.expertsData = expertsData;
        this.mcontext = mcontext;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return expertsData.size();
    }

    @Override
    public Object getItem(int position) {
        return expertsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.top_view_expert_profile, null);
            Log.d("adapter", "first time view created");
        }
        // finding views for setting their values///
        //ImageView expertimage=(ImageView) view.findViewById(R.id.expertImage);
        //ImageView expertVerified=(ImageView) view.findViewById(R.id.expertVerified);
        TextView expertName = (TextView) view.findViewById(R.id.expertName);
        TextView expertInstitute = (TextView) view.findViewById(R.id.expertInstitute);
        TextView expertPracticing = (TextView) view.findViewById(R.id.expertPracticing);
        TextView expertLocation = (TextView) view.findViewById(R.id.expertLocation);
        TextView expertVideoFee = (TextView) view.findViewById(R.id.expertVideoFee);
        TextView expertAudioFee = (TextView) view.findViewById(R.id.expertAudioFee);
        TextView expertMeetFee = (TextView) view.findViewById(R.id.expertMeetFee);
        final Button expertBookAppointmentButton = (Button) view.findViewById(R.id.expertBookAppointmentButton);

        // setting OnClickListener on Button
        expertBookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mcontext, expertBookAppointmentButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int menuPos;
                        String appoFee = "";
                        String expertId = expertsData.get(position).getExpertId();
                        //appointmentData.get(position).getExpertAudioFee();
                        // ((DetailActivity)getActivity()).addBookingAppointmentFragment();
                        int res_id = item.getItemId();
                        switch (res_id) {
                            case R.id.one:
                                //menuPos=8;
                                appoFee = expertsData.get(position).getExpertAudioFee();
                                break;
                            case R.id.two:
                                // menuPos=9;
                                appoFee = expertsData.get(position).getExpertVideoFee();
                                break;
                            case R.id.three:
                                //menuPos=10;
                                appoFee = expertsData.get(position).getExpertMeetFee();
                                break;
                            default:
                                menuPos = 0;
                                break;
                        }

                        // OPEN BOOKING ACTIVITY HERE///
                        Intent intent = new Intent(mcontext, BookAppointmentActivity.class);
                        intent.putExtra("consultType", item.getTitle());
                        intent.putExtra("consultantName", expertsData.get(position).getExpertName());
                        intent.putExtra("consultAmt", Integer.parseInt(appoFee));
                        intent.putExtra("expertId", expertId);
                        mcontext.startActivity(intent);
                        return true;
                    }
                });
                popup.show();//showing popup menu
                // Toast.makeText(mcontext,"Button is pressed for id "+position+" position", Toast.LENGTH_SHORT).show();
            }
        });

        //Setting values in their resspective views
        // Log.d("flow",appointmentData.get(position).getSince().toString());
        expertName.setText(expertsData.get(position).getExpertName());
        expertInstitute.setText(expertsData.get(position).getExpertInstituteName());
        expertPracticing.setText(expertsData.get(position).getExpertCareerStartYear().toString());
        expertLocation.setText(expertsData.get(position).getExpertAddress1());
        expertVideoFee.setText(expertsData.get(position).getExpertVideoFee());
        expertAudioFee.setText(expertsData.get(position).getExpertAudioFee());
        expertMeetFee.setText(expertsData.get(position).getExpertMeetFee());

        return view;
    }
}