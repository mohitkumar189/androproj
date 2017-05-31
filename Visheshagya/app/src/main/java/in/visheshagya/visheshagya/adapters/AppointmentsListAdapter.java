/*
    @Auther MOHIT KUMAR
    Created on 24/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.getterSetter.AppointmentsData;

public class AppointmentsListAdapter extends BaseAdapter {

    ////variables for data
    public ArrayList<AppointmentsData> appointmentData;
    LayoutInflater inflater = null;
    private Context mcontext;

    public AppointmentsListAdapter(ArrayList<AppointmentsData> appointData, Context mcontext) {
        this.appointmentData = appointData;
        this.mcontext = mcontext;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //System.out.println("length of received data is "+appointmentData.size());
        return appointmentData.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.appointment_detail_top_view, null);
            Log.d("adapter", "first time view created");
        }

        // get views
        TextView expertName = (TextView) view.findViewById(R.id.expertName);
        TextView appointmentTiming = (TextView) view.findViewById(R.id.appointTiming);
        TextView consultationType = (TextView) view.findViewById(R.id.consType);
        TextView appointmentStatus = (TextView) view.findViewById(R.id.appointStatus);
        //  TextView appointmentAction=(TextView) view.findViewById(R.id.appointAction);

        // set the data on views
        expertName.setText(appointmentData.get(i).getExpertName());
        appointmentTiming.setText(appointmentData.get(i).getAppointmentTiming());
        consultationType.setText(appointmentData.get(i).getConsultationType());
        appointmentStatus.setText(appointmentData.get(i).getAppointmentStatus());
        // appointmentAction.setText(appointmentData.get(i).getAppointmentAction());
        return view;
    }
}
