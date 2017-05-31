package com.example.mohitattri.visheshagyaexpert.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.mohitattri.visheshagyaexpert.R;
import com.example.mohitattri.visheshagyaexpert.fragments.ConsultInfoFragment;
import com.example.mohitattri.visheshagyaexpert.fragments.ExpertAccountsFragment;
import com.example.mohitattri.visheshagyaexpert.fragments.ExpertCalenderFragment;
import com.example.mohitattri.visheshagyaexpert.fragments.PersonalInfoFragment;
import com.example.mohitattri.visheshagyaexpert.fragments.ProfessionalInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends BaseActivity {
    Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void initViews() {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.myProfileTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PersonalInfoFragment(), "Personal");
        viewPagerAdapter.addFragment(new ProfessionalInfoFragment(), "Professional");
        viewPagerAdapter.addFragment(new ConsultInfoFragment(), "Consulting");
        viewPagerAdapter.addFragment(new ExpertCalenderFragment(), "Calender");
        viewPagerAdapter.addFragment(new ExpertAccountsFragment(), "Account");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void initContext() {
        context=MyProfileActivity.this;
        currentActivity=MyProfileActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    /// View Pager Class////
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            // System.out.println("The size of fragment is " + mFragmentList.size());
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
