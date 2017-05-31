package in.visheshagya.visheshagya.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class SplashActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //set shared preferences
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(SplashActivity.this);
                if (!sharedPrefsClass.getClientIdExistance()) {
                    sharedPrefsClass.setClientId("");
                } else if (!sharedPrefsClass.getLoginExistance()) {
                    sharedPrefsClass.setLoginStatus(false);
                } else if (!sharedPrefsClass.getEmailExistance()) {
                    sharedPrefsClass.setEmailAddress("");
                } else if (!sharedPrefsClass.getMobileExistance()) {
                    sharedPrefsClass.setMobileNumber("");
                } else if (!sharedPrefsClass.getNameExistance()) {
                    sharedPrefsClass.setUserName("");
                }
            }
        });
        thread.run();
        checkCompatibility();
    }

    private void checkCompatibility() {

        if (checkPlayServices()) {
            // Start Application FLOW
            if (checkNetworkStatus()) {
                setDelayTimer();
            } else {
                LinearLayout layout = (LinearLayout) findViewById(R.id.splashLayout);
                Snackbar snackbar = Snackbar.make(layout, "No internet connection", Snackbar.LENGTH_INDEFINITE).setAction("CONNECT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show dialog box for internet connectivity
                        showNoConnectionDialog();
                    }
                });
                View view = snackbar.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.show();

            }
        }
    }

    // to check network status
    private boolean checkNetworkStatus() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;        // always true for testing
    }

    //Opening dialog for network settings
    public void showNoConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_connection);
        builder.setTitle(R.string.no_connection_title);

        builder.setPositiveButton(R.string.settings_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 1);
            }
        });

        builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public void setDelayTimer() {
        new CountDownTimer(1000, 10000) {
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
}
