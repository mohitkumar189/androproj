package practice.mohitattri.cacont;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

public abstract class MyBaseActivity extends AppCompatActivity implements View.OnClickListener {

    Activity currentActivity;
    public Bundle bundle;
    Context context;
    ProgressDialog pdialog;
    Toolbar toolbar;


    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        ScrollView layout = (ScrollView) getLayoutInflater().inflate(R.layout.activity_my_base, null);
        ScrollView activityLayout = (ScrollView) layout.findViewById(R.id.activity_layout);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(layout);


        initContext();
        initViews();
        initListners();

    }


    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode) {
        Intent intent = new Intent(activity, newclass);
        if (bundle != null) {
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (!isResult) {
            startActivity(intent);

        } else if (isResult) {
            startActivityForResult(intent, requestCode);

        } else
            startActivityForResult(intent, requestCode);
    }

    public void startActivity(Activity activity,Class newClass){
        Intent intent = new Intent(activity, newClass);
        startActivity(intent);
    }

    public void toast(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

}
