package com.mediax.mediaxapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.mediax.mediaxapp.R;

/**
 * Created by Mayank on 28/04/2016.
 */
public class ReviewFragment extends BaseFragment implements AppCompatRadioButton.OnCheckedChangeListener {


    AppCompatRadioButton appCombatRadioBug;
    AppCompatRadioButton appCombatRadioImprovement;
    AppCompatRadioButton appCombatRadioSuggestion;

    EditText editMessage;

    Button buttonSend;

    String subject = "Bug Report";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.from(currentActivity).inflate(R.layout.fragment_review, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        appCombatRadioBug = (AppCompatRadioButton) view.findViewById(R.id.appCombatRadioBug);
        appCombatRadioImprovement = (AppCompatRadioButton) view.findViewById(R.id.appCombatRadioImprovement);
        appCombatRadioSuggestion = (AppCompatRadioButton) view.findViewById(R.id.appCombatRadioSuggestion);
        editMessage = (EditText) view.findViewById(R.id.editMessage);

        buttonSend = (Button) view.findViewById(R.id.buttonSend);
    }

    @Override
    protected void initContext() {

    }

    @Override
    protected void initListners() {
        appCombatRadioBug.setOnCheckedChangeListener(this);
        appCombatRadioImprovement.setOnCheckedChangeListener(this);
        appCombatRadioSuggestion.setOnCheckedChangeListener(this);

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSend: {
                toSendMail();
                break;
            }
        }
    }

    private void toSendMail() {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "mediaxassist@gmail.com", null));


        i.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        i.putExtra(android.content.Intent.EXTRA_TEXT, "" + editMessage.getText().toString());
        startActivity(Intent.createChooser(i, "Send email"));

/*        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:emailaddress@emailaddress.com"));
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");

      //  intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        startActivity(Intent.createChooser(intent, "Send Email"));*/
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {
            case R.id.appCombatRadioBug: {
                if (b) {
                    appCombatRadioImprovement.setChecked(false);
                    appCombatRadioSuggestion.setChecked(false);
                    appCombatRadioBug.setChecked(true);

                    subject = "Bug Report";
                }

                break;
            }

            case R.id.appCombatRadioImprovement: {
                if (b) {
                    appCombatRadioBug.setChecked(false);
                    appCombatRadioSuggestion.setChecked(false);
                    appCombatRadioImprovement.setChecked(true);
                    subject = "App Improvement";
                }
                break;
            }
            case R.id.appCombatRadioSuggestion: {
                if (b) {
                    appCombatRadioBug.setChecked(false);
                    appCombatRadioImprovement.setChecked(false);
                    appCombatRadioSuggestion.setChecked(true);
                    subject = "App Suggestion";
                }
                break;
            }
        }

    }
}
