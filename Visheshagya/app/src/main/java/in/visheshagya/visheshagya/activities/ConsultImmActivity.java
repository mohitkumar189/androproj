package in.visheshagya.visheshagya.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.networking.ClassForUserAccount;
import in.visheshagya.visheshagya.paymentGateways.InstantPayuPayment;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.resourses.StringResources;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class ConsultImmActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPrefsClass sharedPrefsClass;
    private String expertType;
    private Button goBack, bookAppointment, dateButton;
    private Dialog dialog;   // Created a new Dialog
    private String clientId;
    private int clientType = -1;
    private float amount = (float) 1100.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_imm);
        dialog = new Dialog(ConsultImmActivity.this);
        sharedPrefsClass = new SharedPrefsClass(this);
        Intent intent = getIntent();
        expertType = intent.getStringExtra("expertType");
        //Toast.makeText(this,expertType.toString(),Toast.LENGTH_SHORT).show();
        initializeComponents();
    }

    private void initializeComponents() {
        goBack = (Button) findViewById(R.id.goBackConsImm);
        bookAppointment = (Button) findViewById(R.id.goToBookAppoint);
        dateButton = (Button) findViewById(R.id.immAppoDateBtn);
        goBack.setOnClickListener(this);
        bookAppointment.setOnClickListener(this);
        setDate();
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateButton.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();
        switch (resId) {
            case R.id.goBackConsImm:
                finish();
                break;
            case R.id.goToBookAppoint:
                checkLogin();
                //Intent intent=new Intent(ConsultImmActivity.this, PayuPaymentGateway.class);
                //Toast.makeText(this,"book clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLogin() {

        if (sharedPrefsClass.getLoginStatus()) {                           // check user login status
            // Do BOOKIG IF USER IS LOGGED IN
            //Toast.makeText(this, "Yor are Logged in", Toast.LENGTH_SHORT).show();
            continueBooking(); // call for booking method
        } else {
            // SHOW POPUP TO USER TO LOG IN
            // Toast.makeText(this,"Please Login",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(true);
            dialog.setTitle(R.string.no_login_title);
            dialog.setPositiveButton(R.string.positive_btn_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // SHOW LOGIN DIALOG HERE FOR LOGIN
                    createDialog("login");

                }
            });
            dialog.setNegativeButton(R.string.negative_btn_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // SHOW SIGNUP DIALOG HERE FOR SIGNUP
                    createDialog("signup");

                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    return;
                }
            });
            dialog.show();
        }

    }

    private void continueBooking() {
        EditText appoQuery = (EditText) findViewById(R.id.EditText02);
        String appointmentNotes = appoQuery.getText().toString().trim();
        clientId = sharedPrefsClass.getClientId();
        String bookingUrl = APIUrls.VISHESHAGYA_URL + APIUrls.SAVE_INSTANT_APPOINTMENT + "appointmentNotes=" + appointmentNotes + "&clientId="
                + clientId + "&categoryId=" + 2 + "&amountPaid=" + amount;
        System.out.println("booking url is " + bookingUrl);
        if (appointmentNotes.length() > 0) {
            //hit for app id
            new GetAppointmentId().execute(bookingUrl);
        } else {
            Toast.makeText(ConsultImmActivity.this, "Please write query", Toast.LENGTH_SHORT).show();
        }
    }

    public void createDialog(String type) {
        final String[] uLname = {null};//login details
        final String[] uLpswd = {null};// login details
        final String[] uSname = {null};//signup details
        final String[] uSEmail = {null};//signup details
        final String[] uSphn = {null};//signup details
        Button lBtn, cancelBtn;
        Button sBtn;

        switch (type) {
            case "login":
                dialog.setTitle("Login to Visheshagya");   // Set the title
                dialog.setContentView(R.layout.login_dialog); // inflate the layout
                final EditText uName = (EditText) dialog.findViewById(R.id.loginEmailId);
                final EditText uPassword = (EditText) dialog.findViewById(R.id.loginPassword);
                lBtn = (Button) dialog.findViewById(R.id.btnLogin);
                cancelBtn = (Button) dialog.findViewById(R.id.btnCancel);
                lBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uLname[0] = uName.getText().toString().trim().toLowerCase();
                        uLpswd[0] = uPassword.getText().toString().trim();
                        if (uLname[0].length() > 7 && uLname[0].matches(StringResources.emailPattern)) {

                        } else {
                            uName.setError("Enter valid Email");
                            uName.requestFocus();
                        }
                        if (uLpswd[0].length() < 6) {
                            uPassword.setError("Enter a valid password");
                            uName.requestFocus();
                        } else {
                            continueLogin(uLname[0], uLpswd[0]);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                break;
            case "signup":
                dialog.setTitle("Signup");
                dialog.setContentView(R.layout.signup_dialog);
                final EditText usName = (EditText) dialog.findViewById(R.id.sName);
                final EditText usEmail = (EditText) dialog.findViewById(R.id.sEmailId);
                final EditText usMobile = (EditText) dialog.findViewById(R.id.sMobileNumber);
                sBtn = (Button) dialog.findViewById(R.id.signupButton);
                cancelBtn = (Button) dialog.findViewById(R.id.btnCancel);
                sBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.termsAcceptCheck);
                        boolean checked = checkBox.isChecked();
                        uSname[0] = usName.getText().toString();
                        uSEmail[0] = usEmail.getText().toString();
                        uSphn[0] = usMobile.getText().toString();
                        if (checked) {
                            if (uSEmail[0].length() > 7 && uSEmail[0].matches(StringResources.emailPattern)) {

                            } else {
                                usEmail.setError("Enter a valid Email");
                                usEmail.requestFocus();
                            }
                            if (uSname[0].length() < 3) {
                                usName.setError("Enter a valid password");
                                usName.requestFocus();
                            } else {
                            }
                            if (uSphn[0].length() == 10 && uSphn[0].matches(StringResources.MobilePattern)) {
                                continueSignup(uSname[0], uSEmail[0], uSphn[0]);
                            } else {
                                usMobile.setError("Enter a valid mobile number");
                                usMobile.requestFocus();
                            }
                        } else {
                            Toast.makeText(ConsultImmActivity.this, "First Accept terms", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                break;
            default:
                break;
        }
        dialog.show();   // Display the dialog

    }

    private void continueLogin(String s, String s1) {
        String u = s;
        String p = s1;
        ClassForUserAccount classForUserAccount = new ClassForUserAccount(ConsultImmActivity.this);
        boolean ss = classForUserAccount.loginClient(u, p, clientType);
        //System.out.println("username " + s + " password " + s1);
        if (ss) {
            //Toast.makeText(getApplicationContext(), "Details matched", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            continueBooking();
        } else {
            //Toast.makeText(getApplicationContext(), "Details not matched", Toast.LENGTH_SHORT).show();
        }
    }

    private void continueSignup(String s, String s1, String s2) {
        String u = s;
        String e = s1;
        String n = s2;
        ClassForUserAccount classForUserAccount = new ClassForUserAccount(ConsultImmActivity.this);
        boolean ss = classForUserAccount.signupClient(u, e, n, clientType);
        if (ss) {
            Toast.makeText(getApplicationContext(), "successfully signed up", Toast.LENGTH_SHORT).show();
            //open login dialog here
            dialog.cancel();
            createDialog("login");
        } else {
            Toast.makeText(getApplicationContext(), "signup failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void continueToPayment(String jsonData) {
        String appointmentId;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                String message = jsonObject.getString("message");
                JSONObject jsonObject1 = new JSONObject(message);
                appointmentId = jsonObject1.getString("appointmentId");
                // System.out.println("appointment id: "+appointmentId+" message: "+message);
                Intent intent = new Intent(ConsultImmActivity.this, InstantPayuPayment.class);
                intent.putExtra("firstName", sharedPrefsClass.getUserName());
                intent.putExtra("phn", sharedPrefsClass.getMobileNumber());
                intent.putExtra("emailId", sharedPrefsClass.getEmailAddress());
                intent.putExtra("consAmt", amount);
                intent.putExtra("txnId", appointmentId);
                startActivity(intent);
            } else {
                // System.out.println("success failed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetAppointmentId extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConsultImmActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String data = new JSONDownloader().jsonData(strings[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            progressDialog.cancel();
            if (jsonData != null) {
                //System.out.println("app id json is " + jsonData);
                continueToPayment(jsonData);
            } else {
                LinearLayout layout = (LinearLayout) findViewById(R.id.consImmLay);
                Snackbar snackbar = Snackbar.make(layout, "Something went wrong...", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        continueBooking();
                    }
                });
                View view = snackbar.getView();
                //  snackbar.getView().setBackgroundColor(getResources().getColor(R.color.White));
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.show();
                //System.out.println("No response from server");
            }
        }
    }
}
