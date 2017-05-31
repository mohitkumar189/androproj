package com.example.mohitattri.visheshagyaexpert.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.utils.SharedPreferenceUtils;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends BaseActivity {
    Context context;
    ImageView imageView;

/*    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.splash_background); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.vishesh_logo_circle); //or any other drawable
        configSplash.setAnimLogoSplashDuration(5000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("TAX and LEGAL"+"\n Services Redefined");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(25f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
       // configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView myView = (ImageView) findViewById(R.id.splash_logo_iv);
        imageView = (ImageView) findViewById(R.id.v_branding);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(myView, "alpha", 1f, .3f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
        slideToAbove();
    }

    //from down to above animation
    public void slideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -3.3f);

        slide.setDuration(4000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        imageView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.clearAnimation();
                        if (SharedPreferenceUtils.getInstance(context).getBoolean(IS_LOGIN)) {
                            startActivity(currentActivity, AppAccessActivity.class, null, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        } else {
                            startActivity(currentActivity, UserLoginActivity.class, null, true, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                            toast("not logged in", true);
                        }
                    }
                }, SPLASH_TIME);

          /*      imageView.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        imageView.getWidth(), imageView.getHeight());
                // lp.setMargins(0, 0, 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                imageView.setLayoutParams(lp);*/

            }

        });

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initContext() {
        currentActivity = SplashActivity.this;
        context = SplashActivity.this;
    }

    @Override
    protected void initListners() {

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
    public void onClick(View view) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
