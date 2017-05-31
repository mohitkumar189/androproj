package practice.mohitattri.cacont;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class AppPassActivity extends MyBaseActivity {

    private EditText editPassDigitOne, editPassDigitTwo, editPassDigitThree, editPassDigitFour;

    @Override
    protected void initViews() {
        editPassDigitOne = (EditText) findViewById(R.id.editPassDigitOne);
        editPassDigitTwo = (EditText) findViewById(R.id.editPassDigitTwo);
        editPassDigitThree = (EditText) findViewById(R.id.editPassDigitThree);
        editPassDigitFour = (EditText) findViewById(R.id.editPassDigitFour);
    }

    @Override
    protected void initContext() {
        context = AppPassActivity.this;
        currentActivity = AppPassActivity.this;
    }

    @Override
    protected void initListners() {

        editPassDigitTwo.addTextChangedListener(new MyTextWatcher(editPassDigitTwo));
        editPassDigitThree.addTextChangedListener(new MyTextWatcher(editPassDigitThree));
        editPassDigitFour.addTextChangedListener(new MyTextWatcher(editPassDigitFour));
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_pass);

    }

    @Override
    public void onClick(View view) {

    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.editPassDigitOne:
                    editPassDigitTwo.requestFocus();
                    Log.d("tag", "editPassDigitOne");
                    break;
                case R.id.editPassDigitTwo:
                    editPassDigitThree.requestFocus();
                    Log.d("tag", "editPassDigitTwo");
                    break;
                case R.id.editPassDigitThree:
                    editPassDigitFour.requestFocus();
                    Log.d("tag", "editPassDigitThree");
                    break;
                case R.id.editPassDigitFour:
                    // Toast.makeText(UserPassActivity.this, "Password entered", Toast.LENGTH_SHORT).show();
                 /*   if (editPassDigitFour.getText().toString() != null || editPassDigitFour.getText().toString() !=""){
                    }*/
                    if(text !=null || text !=""){
                        startActivity(currentActivity,UserDashBoardActivity.class);

                    }
                    Log.d("tag", "editPassDigitFour");
                    break;
            }
        }
    }
}
