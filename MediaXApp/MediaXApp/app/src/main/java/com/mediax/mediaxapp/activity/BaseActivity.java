package com.mediax.mediaxapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mediax.mediaxapp.R;
import com.mediax.mediaxapp.constant.AppConstants;
import com.mediax.mediaxapp.interfaces.AlertClicked;
import com.mediax.mediaxapp.utils.DimensionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayank on 29/02/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, AlertClicked, AppConstants {

    Activity currentActivity;
    public Bundle bundle;
    Context context;
    ProgressDialog pdialog;

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    public Map<String, String> linkParamsMap;

    Dialog customDialog;
    Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_base_activity, null);
        LinearLayout activityLayout = (LinearLayout) layout.findViewById(R.id.activity_layout);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(layout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isActionBar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (isHomeButton()) {
            settingHomeButton();
        }

        initContext();
        initViews();
        initListners();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        linkParamsMap = new HashMap<>();
    }


    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode, boolean animationRequired, int animationType) {
        Intent intent = new Intent(activity, newclass);
        if (bundle != null)

            intent.putExtras(bundle);
        if (!isResult && !animationRequired)
            startActivity(intent);
        else if (!isResult && animationRequired) {
            startActivity(intent);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }

        } else if (isResult && animationRequired) {
            startActivityForResult(intent, requestCode);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }
        } else
            startActivityForResult(intent, requestCode);
    }

    public void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(context);
        }


        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }

        if (!pdialog.isShowing()) {
            pdialog.show();

        }

    }

    public void cancelProgressDialog() {
        pdialog.cancel();
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

    public void toastTesting(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }


    public static void log(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public void logTesting(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    protected void toHideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void settingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);
    }

    public void settingTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void removingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment, boolean addToBackStack,
                              boolean add, String tag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (!add) {

            ft.replace(R.id.fragmentContainer, fragment, tag);
        } else {
            ft.add(R.id.fragmentContainer, fragment, tag);
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public void popBackStack(String tag) {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }


    protected Dialog creatingDialog(Context context, boolean isCancelableBack, boolean isCancelableoutside, View view, int height, int width, boolean heightMatchParent) {
        customDialog = new Dialog(context, R.style.dialogTheme);
        //  dialog.setCancelable(isCancelableBack);
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
        customDialog.setCanceledOnTouchOutside(isCancelableoutside);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = customDialog.getWindow();
        window.setGravity(Gravity.RIGHT);
      /*  WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Helper.toPixels(context, 200);
        layoutParams.height = Helper.toPixels(context, 200);
        dialog.getWindow().setAttributes(layoutParams);*/
        customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        customDialog.setContentView(view);
        customDialog.show();
        if (heightMatchParent) {
            customDialog.getWindow().setLayout(DimensionUtils.toPixels(context, width), WindowManager.LayoutParams.MATCH_PARENT);
        } else {
            customDialog.getWindow().setLayout(DimensionUtils.toPixels(context, width), DimensionUtils.toPixels(context, height));

        }

        return customDialog;

    }

    protected void cancelCustomDialog() {
        if (customDialog != null) {
            customDialog.cancel();
        }

    }


    protected Dialog creatingDialog(Context context, boolean isCancelableBack, boolean isCancelableoutside, View view, int height, int width) {
        customDialog = new Dialog(context, R.style.dialogTheme);
        //  dialog.setCancelable(isCancelableBack);
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
        customDialog.setCanceledOnTouchOutside(isCancelableoutside);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


      /*  WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Helper.toPixels(context, 200);
        layoutParams.height = Helper.toPixels(context, 200);
        dialog.getWindow().setAttributes(layoutParams);*/
        customDialog.setContentView(view);
        WindowManager.LayoutParams wmlp = customDialog.getWindow().getAttributes();
        customDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wmlp.gravity = Gravity.CENTER;
      /*  wmlp.x = 50;   //x position
        wmlp.y = -100;*/
        customDialog.show();

        customDialog.getWindow().setLayout(DimensionUtils.toPixels(context, width), DimensionUtils.toPixels(context, height));
        customDialog.getWindow().setAttributes(wmlp);
        return customDialog;

    }


    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onAlertClicked(alertType);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();


    }


}