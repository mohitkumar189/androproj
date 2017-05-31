package in.visheshagya.visheshagya.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.paymentGateways.PayuPaymentGateway;
import in.visheshagya.visheshagya.storagePackage.SharedPrefsClass;

public class BookingConfirmationActivity extends AppCompatActivity {
    private String expertName, consultationAmt, consultDate, consultTime, consultationType, appointmentId, clientNumber;
    private Button goToPayuBtn;
    private String txnId, firstName, emailId, phoneNumber;
    private float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        Bundle bundle = getIntent().getExtras();
        appointmentId = bundle.getString("appointmentId");
        expertName = bundle.getString("consultantName");
        consultationAmt = bundle.getString("appointmentAmount");
        consultDate = bundle.getString("appointmentDate");
        String coType = bundle.getString("consultantType");
        String consultStartTime = bundle.getString("appointmentStartTime");
        String consultEndTime = bundle.getString("appointmentEndTime");
        // System.out.println("appointment start time is "+consultStartTime);
        consultTime = consultStartTime + "-" + consultEndTime;
        switch (coType) {
            case "1":
                consultationType = "Video";
                break;
            case "2":
                consultationType = "Audio";
                break;
            case "3":
                consultationType = "In person";
                break;
            default:
                break;
        }
        setupData();
    }

    private void setupData() {
        SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(this);
        clientNumber = sharedPrefsClass.getMobileNumber();
        TextView appId = (TextView) findViewById(R.id.consultID);
        TextView consName = (TextView) findViewById(R.id.consultantName);
        final TextView consAmt = (TextView) findViewById(R.id.consultAmount);
        TextView consDate = (TextView) findViewById(R.id.bookAppointmentDate);
        final TextView consTime = (TextView) findViewById(R.id.bookAppointmentTime);
        TextView consType = (TextView) findViewById(R.id.consultType);
        final EditText clientNo = (EditText) findViewById(R.id.clientContactNo);
        goToPayuBtn = (Button) findViewById(R.id.goToPayment);
        appId.setText(appointmentId);
        consName.setText(expertName);
        consAmt.setText(consultationAmt);
        consDate.setText(consultDate);
        consTime.setText(consultTime);
        consType.setText(consultationType);
        clientNo.setText(clientNumber);
        goToPayuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsClass sharedPrefsClass = new SharedPrefsClass(BookingConfirmationActivity.this);
                phoneNumber = clientNo.getText().toString().trim();
                amount = Float.parseFloat(consultationAmt);
                // System.out.println("amount in float is "+amount);
                Bundle bundle = new Bundle();
                bundle.putString("firstName", "mohit");
                bundle.putString("phn", phoneNumber);
                bundle.putString("txnId", appointmentId);
                bundle.putString("consType", consultationType);
                bundle.putString("consTime", consultTime);
                bundle.putString("consDate", consultDate);
                bundle.putString("expertName", expertName);
                bundle.putString("emailId", sharedPrefsClass.getEmailAddress());
                bundle.putFloat("consAmt", amount);
                System.out.println("consultation amount is " + amount);
                Intent intent = new Intent(BookingConfirmationActivity.this, PayuPaymentGateway.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
