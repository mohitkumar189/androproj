package in.visheshagya.visheshagya.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.expertsListFragments.CaListFragment;
import in.visheshagya.visheshagya.expertsListFragments.CsListFragment;
import in.visheshagya.visheshagya.expertsListFragments.LawyersListFragment;

public class ExpertsListWithCategory extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int expertCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        expertCategory = intent.getIntExtra("categoryName", 100);
        //System.out.println("expert category is "+expertCategory);
        setContentView(R.layout.activity_experts_list_with_category);
        viewPager = (ViewPager) findViewById(R.id.expertsListViewPager);
        tabLayout = (TabLayout) findViewById(R.id.expertsListTabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CaListFragment(), "CA");
        viewPagerAdapter.addFragment(new CsListFragment(), "CS");
        viewPagerAdapter.addFragment(new LawyersListFragment(), "LAWYER");
        // viewPagerAdapter.addFragment(new CmaListFragment(), "CMA");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(expertCategory - 1);
    }

    // View Pager Class////
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
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
    }
}
