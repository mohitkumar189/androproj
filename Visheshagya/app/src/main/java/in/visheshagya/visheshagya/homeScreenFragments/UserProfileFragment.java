package in.visheshagya.visheshagya.homeScreenFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.AppointmentDetailsActivity;
import in.visheshagya.visheshagya.activities.ELockersActivity;
import in.visheshagya.visheshagya.activities.LoginSignupActivity;
import in.visheshagya.visheshagya.activities.UserProfileActivity;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;
import in.visheshagya.visheshagya.webViews.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    LinearLayout eLockerLayout;
    LinearLayout profileViewLayout;
    LinearLayout myAppointmentLayout;
    SharedPrefsClass sharedPrefsClass;
    TextView profileName;
    String getProfileName;


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPrefsClass = new SharedPrefsClass(getActivity().getApplicationContext());

        profileName = (TextView) getActivity().findViewById(R.id.clientProfileNameTop);
        // Get the Elocker layout and set on click listener
        eLockerLayout = (LinearLayout) getActivity().findViewById(R.id.eLockerTabLayout);
        profileViewLayout = (LinearLayout) getActivity().findViewById(R.id.clientProfileViewProfile);
        myAppointmentLayout = (LinearLayout) getActivity().findViewById(R.id.clientProfileMyAppointment);

        //listener for view profile
        profileViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefsClass.getLoginStatus()) {
                    startActivity(new Intent(getActivity(), UserProfileActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Please login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //listener for My appointments
        myAppointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefsClass.getLoginStatus()) {
                    startActivity(new Intent(getActivity(), AppointmentDetailsActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Please login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // listener for elocker activity
        eLockerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefsClass.getLoginStatus()) {
                    startActivity(new Intent(getActivity(), ELockersActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Please login", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // get the terms and services layout
        LinearLayout termsAndCondLay = (LinearLayout) getActivity().findViewById(R.id.termsAndCond);
        termsAndCondLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("action", "tmc");
                startActivity(intent);
            }
        });

        setLogBtn(); // set logout btn
    }

    private void setLogBtn() {
        // Shared prefs class for setting up login/logout button
        final Button logOutBtn = (Button) getActivity().findViewById(R.id.logoutUser);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Btn pressed");
                Boolean loginStats = sharedPrefsClass.getLoginStatus();
                if (loginStats) {
                    sharedPrefsClass.setLoginStatus(false);
                    logOutBtn.setText("LOGIN");
                } else {
                    startActivity(new Intent(getActivity().getApplicationContext(), LoginSignupActivity.class));
                }
            }
        });

        // set profile name text and btn text
        if (sharedPrefsClass.getLoginStatus()) {
            getProfileName = sharedPrefsClass.getUserName();
            profileName.setText(getProfileName); // set Profilename
            logOutBtn.setText("LOGOUT");
        } else {
            profileName.setText("Name"); // set Default name
            sharedPrefsClass.setLoginStatus(false);
            logOutBtn.setText("LOGIN");
        }
    }

}
