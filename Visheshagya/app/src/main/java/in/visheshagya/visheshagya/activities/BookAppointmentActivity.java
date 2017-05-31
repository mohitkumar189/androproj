package in.visheshagya.visheshagya.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import in.visheshagya.visheshagya.JSONDownloaderPackage.JSONDownloader;
import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.networking.ClassForUserAccount;
import in.visheshagya.visheshagya.resourses.APIUrls;
import in.visheshagya.visheshagya.resourses.DataTAGS;
import in.visheshagya.visheshagya.resourses.StringResources;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;
import in.visheshagya.visheshagya.webViews.WebViewActivity;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPrefsClass sharedPrefsClass;
    private Boolean loginStatus = false;
    private Button btnBooking, btnGoBack;
    private EditText queryTextBox;
    private TextView consType, consAmt;
    private String consultType = "";
    private int consultAmount;
    private String consultantName = "";
    private String consultTime = "";
    private String consultDate = null;
    private String appointmentId = null;
    private Button dateChooseBtn;
    private int year;
    private int day;
    private int month;
    private StringBuilder appointmentDate;
    private StringBuilder timeSlot = new StringBuilder();
    private String[] availTimeSlots = {};
    private int clientType = -1;
    private Dialog dialog;   // Created a new Dialog

    //parameters for appointment assigning
    private String appointmentNotes;
    private String expertId = null;
    private String clientId;
    private String appointmentStartTime;
    private String categoryId;
    private double amountPaid;

    private Spinner spinnerDate, spinnerTime;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3
            setDate(arg1, arg2 + 1, arg3);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        dialog = new Dialog(BookAppointmentActivity.this);
        Intent intent = getIntent();
        consultType = intent.getStringExtra("consultType");// get consultation type
        consultAmount = intent.getIntExtra("consultAmt", 0); // get consultation amount
        consultantName = intent.getStringExtra("consultantName"); // get consultant name
        expertId = intent.getStringExtra("expertId");
        sharedPrefsClass = new SharedPrefsClass(this);
        consType = (TextView) findViewById(R.id.consultType);
        consAmt = (TextView) findViewById(R.id.consultAmount);
        dateChooseBtn = (Button) findViewById(R.id.bookAppointmentDateBtn);
        spinnerDate = (Spinner) findViewById(R.id.bookAppointmentDate);
        queryTextBox = (EditText) findViewById(R.id.bookQueryToExpert);
        spinnerTime = (Spinner) findViewById(R.id.bookAppointmentTime);// Spinner for time slots
        btnBooking = (Button) findViewById(R.id.bookAppointmentButtonF);
        btnGoBack = (Button) findViewById(R.id.bookGoBackBtn);
        setCategoryId(consultType);// set category id
        setAmountPaid(consultAmount);// set amount paid
        btnBooking.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
        dateChooseBtn.setOnClickListener(this);
        // System.out.println("Book con id is " + consultantName);

        consType.setText(consultType); // Set consultation type
        consAmt.setText("" + consultAmount);// Set consultation amount
    }

    private void setSpinners() {
        //Setting time slots for spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this,
                android.R.layout.simple_dropdown_item_1line, availTimeSlots);
        // spinnerDate.setAdapter(arrayAdapter);
        spinnerTime.setAdapter(arrayAdapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (availTimeSlots.length > 1) {
                    appointmentStartTime = availTimeSlots[i].substring(0, 5);
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(BookAppointmentActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCategoryId(String consultationType) {
        switch (consultationType) {
            case "Video":
                categoryId = "1";
                break;
            case "Audio":
                categoryId = "2";
                break;
            case "Meet":
                categoryId = "3";
                break;
            default:
                break;
        }
    }

    private void setAmountPaid(int amt) {
        if (amt != 0) {
            amountPaid = amt + amt / 10;
        } else {
            return;
        }

    }

    @Override
    public void onClick(View view) {
        int res_id = view.getId();
        switch (res_id) {
            case R.id.bookAppointmentDateBtn:

                showDateDialog();
                break;
            case R.id.bookGoBackBtn:
                // KILL ACTIVITY IF USER PRESSED BACK BUTTON
                finish();
                break;
            case R.id.bookAppointmentButtonF:
                appointmentNotes = queryTextBox.getText().toString().trim();// get appointment notes
                if (appointmentNotes.length() > 2) { // set appointment notes size
                    if (availTimeSlots.length > 1) {
                        if (sharedPrefsClass.getLoginStatus()) {  // Check login status
                            // check user login status
                            // Do BOOKIG IF USER IS LOGGED IN
                            // Toast.makeText(this, "Yor are Logged in", Toast.LENGTH_SHORT).show();
                            continueBooking(); // call for booking method
                        } else {
                            // SHOW POPUP TO USER TO LOG IN
                            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                            dialog.setCancelable(false);
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
                    } else {
                        Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Please type query to expert", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void showPopupMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, btnBooking);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // ((DetailActivity)getActivity()).addBookingAppointmentFragment();

                Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                // OPEN BOOKING ACTIVITY HERE///
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    public void createDialog(String type) {
        final String[] uLname = {null};//login details
        final String[] uLpswd = {null};// login details
        final String[] uSname = {null};//signup details
        final String[] uSEmail = {null};//signup details
        final String[] uSphn = {null};//signup details
        final String[] uFEmail = {null};//signup details
        final String[] uFphn = {null};//signup details
        Button lBtn, cancelBtn;
        Button sBtn;

        switch (type) {
            case "login":
                dialog.setTitle("Login to Visheshagya");   // Set the title
                dialog.setContentView(R.layout.login_dialog); // inflate the layout
                final EditText uName = (EditText) dialog.findViewById(R.id.loginEmailId);
                final EditText uPassword = (EditText) dialog.findViewById(R.id.loginPassword);
                final TextView uForgotLink = (TextView) dialog.findViewById(R.id.forgotLink);
                uForgotLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        createDialog("forgot");
                    }
                });
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
                TextView sForgotLink = (TextView) dialog.findViewById(R.id.forgotLink);
                sForgotLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        createDialog("forgot");
                    }
                });
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
                            Toast.makeText(BookAppointmentActivity.this, "First Accept terms", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                TextView termsAndCon = (TextView) dialog.findViewById(R.id.termsAndCondTV);
                termsAndCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BookAppointmentActivity.this, WebViewActivity.class);
                        intent.putExtra("action", "tmc");
                        startActivity(intent);
                    }
                });
                break;
            case "forgot":
                dialog.setTitle("Login to Visheshagya");   // Set the title
                dialog.setContentView(R.layout.forgot_dialog); // inflate the layout
                final EditText uFFEmail = (EditText) dialog.findViewById(R.id.fLoginEmailId);
                final EditText uFMobile = (EditText) dialog.findViewById(R.id.fMobileNumber);
                lBtn = (Button) dialog.findViewById(R.id.submit);
                cancelBtn = (Button) dialog.findViewById(R.id.cancel);
                lBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uFEmail[0] = uFFEmail.getText().toString().trim().toLowerCase();
                        uFphn[0] = uFMobile.getText().toString().trim();
                        if (uFEmail[0].length() > 7 && uFEmail[0].matches(StringResources.emailPattern)) {

                        } else {
                            uFFEmail.setError("Enter a valid Email");
                            uFFEmail.requestFocus();
                        }
                        if (uFphn[0].length() == 10 && uFphn[0].matches(StringResources.MobilePattern)) {
                            continueForgotPassword(uFEmail[0], uFphn[0]);
                        } else {
                            uFMobile.setError("Enter a valid mobile number");
                            uFMobile.requestFocus();
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

    private void continueForgotPassword(String userEmail, String userMobile) {
        ClassForUserAccount classForUserAccount = new ClassForUserAccount(BookAppointmentActivity.this);
        boolean a = classForUserAccount.fogotPassword(userEmail, userMobile, "2");
        if (a) {
            Toast.makeText(this, "Password sent on registered number", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            createDialog("login");
        } else {
            Toast.makeText(this, "Details are not matched", Toast.LENGTH_SHORT).show();
        }
    }

    private void continueLogin(String userName, String userPassword) {
        String u = userName;
        String p = userPassword;

        ClassForUserAccount classForUserAccount = new ClassForUserAccount(BookAppointmentActivity.this);
        boolean s = classForUserAccount.loginClient(u, p, 2);
        //Toast.makeText(getApplicationContext(), "username " + userName + " password " + userPassword, Toast.LENGTH_SHORT).show();
        System.out.println("username " + userName + " password " + userPassword);
        if (s) {
            Toast.makeText(getApplicationContext(), "Details matched", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            continueBooking();
        } else {
            Toast.makeText(getApplicationContext(), "Details not matched", Toast.LENGTH_SHORT).show();
        }
    }

    // Signup method
    private void continueSignup(String name, String email, String contactNo) {
        String u = name;
        String e = email;
        String n = contactNo;
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.termsAcceptCheck);
        boolean checked = checkBox.isChecked();
        if (checked) {
            ClassForUserAccount classForUserAccount = new ClassForUserAccount(BookAppointmentActivity.this);
            boolean s = classForUserAccount.signupClient(u, e, n, 2);
            if (s) {
                Toast.makeText(getApplicationContext(), "signed up", Toast.LENGTH_SHORT).show();
                //open login dialog here
                dialog.cancel();
                createDialog("login");
            } else {
                Toast.makeText(getApplicationContext(), "signed up failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Accept terms", Toast.LENGTH_SHORT).show();
        }
    }

    public void continueBooking() {
        // System.out.println("Continue Booking method is called");
        //System.out.println("consultant name is " + consultantName);
        clientId = sharedPrefsClass.getClientId();
        System.out.println("app date= " + appointmentDate + " app notes= " + appointmentNotes + " expert id= " + expertId
                + " client id= " + clientId + " app start time= " + appointmentStartTime + " category id= " + categoryId + " amount paid= " + amountPaid);

        String bookingUrl = APIUrls.VISHESHAGYA_URL + APIUrls.SAVE_APPOINTMENT + "appointmentDate=" + appointmentDate + "&appointmentNotes=" + appointmentNotes + "&expertId=" + expertId + "&clientId="
                + clientId + "&appointmentStartTime=" + appointmentStartTime + "&categoryId=" + categoryId + "&amountPaid=" + amountPaid;
        System.out.println("booking url is " + bookingUrl);
        if (appointmentNotes.length() > 0) {
            new GetAppointmentId().execute(bookingUrl);// hit web service for appointment assignment
        } else {
            Toast.makeText(BookAppointmentActivity.this, "Please write query", Toast.LENGTH_SHORT).show();
        }
    }

    private void continueToPayment(String jsonData) {
        try {
            System.out.println("book app json data is " + jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            int response = jsonObject.getInt("success");
            if (response == 1) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                System.out.println("amount is " + consultAmount + " consultant name is " + consultantName);

                String message = jsonObject.getString("message");
                JSONObject jsonObject1 = new JSONObject(message);
                System.out.println("appointment id is " + jsonObject1.getString("appointmentId"));
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId", jsonObject1.getString("appointmentId"));
                bundle.putString("appointmentDate", jsonObject1.getString("appointmentDate"));
                bundle.putString("appointmentStartTime", jsonObject1.getString("appointmentStartTime"));
                bundle.putString("appointmentEndTime", jsonObject1.getString("appointmentEndTime"));
                bundle.putString("appointmentAmount", String.valueOf(amountPaid)); // set amount to be paid after 10%
                bundle.putString("consultantName", consultantName);
                bundle.putString("consultantType", categoryId);
                System.out.println("app id to be passed " + jsonObject1.getString("appointmentStartTime"));
                Intent intent = new Intent(BookAppointmentActivity.this, BookingConfirmationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                String message = jsonObject.getString("message");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
         /*     Intent intent = new Intent(getApplicationContext(), BookingConfirmationActivity.class);
            String[] passData = {consultantName, ""+amountPaid, consultTime, consultDate, consultType};
            intent.putExtra("passData", passData);
            startActivity(intent);*/


    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, myDateListener, year, month, day).show();

    }

    private void setDate(int year1, int month1, int day1) {
        appointmentDate = new StringBuilder().append(year1).append("-").append(month1).append("-").append(day1);
        System.out.println("date is " + appointmentDate);
        //System.out.println("date is: "+day1+" month is: "+month1+" year is: "+year1);
        if (year1 >= year && month1 >= month && day1 >= day) {
            dateChooseBtn.setText(new StringBuilder().append(day1).append(".")
                    .append(month1).append(".").append(year1));
        } else {
            Toast.makeText(getApplicationContext(), "Plesae select a valid date", Toast.LENGTH_SHORT).show();
            dateChooseBtn.setText("Select Date");
        }
        loadTimeSlots();//load time slots using url
    }

    private void loadTimeSlots() {
        String url = APIUrls.VISHESHAGYA_URL + APIUrls.AVAIL_TIME_SLOT + "appointmentDate=" + appointmentDate + "&expertId=" + expertId;
        System.out.println("time slot url is " + url);
        new LoadTimeSlots().execute(url);
    }

    private void setTimeSlots(String jsonData) {
        System.out.println("time slots are " + jsonData);
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            availTimeSlots = new String[jsonArray.length() + 1];
            availTimeSlots[0] = "--Select Time--";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonExpertObject = jsonArray.getJSONObject(i);
                String timeStart = jsonExpertObject.getString(DataTAGS.TIME_SLOT_START);
                String timeEnd = jsonExpertObject.getString(DataTAGS.TIME_SLOT_END);
                String timeToAdd = timeStart + "-" + timeEnd;
                System.out.println("start time is: " + timeStart + " end time is " + timeEnd);
                timeSlot.append(timeStart).append("-").append(timeEnd);
                availTimeSlots[i + 1] = timeToAdd;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("time slots string is " + timeSlot);
        System.out.println("time slots string is " + availTimeSlots);
        setSpinners();
    }

    private class LoadTimeSlots extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BookAppointmentActivity.this);
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
            setTimeSlots(jsonData);
        }
    }

    private class GetAppointmentId extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BookAppointmentActivity.this);
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
            System.out.println("app id json is " + jsonData);
            continueToPayment(jsonData);
        }
    }
}
