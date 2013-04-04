package com.cashflow.activity;

import java.util.Locale;

import roboguice.activity.RoboFragmentActivity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cashflow.R;
import com.cashflow.bill.activity.AddBillFragment;
import com.cashflow.statement.activity.AddIncomeFragment;
import com.cashflow.statement.activity.AddStatementFragment;

/**
 * List the actions in tabbed form.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ActionsActivity extends RoboFragmentActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_actions);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(final ActionBar.Tab tab, final FragmentTransaction transaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(final ActionBar.Tab tab, final FragmentTransaction transaction) {
    }

    @Override
    public void onTabReselected(final ActionBar.Tab tab, final FragmentTransaction transaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Constructor
         * @param manager {@link FragmentManager}
         */
        public SectionsPagerAdapter(final FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(final int position) {
            Fragment fragment = null;
            switch (position) {
            case 0:
                fragment = new AddIncomeFragment();
                break;
            case 1:
                fragment = new AddStatementFragment();
                break;
            case 2:
                fragment = new AddBillFragment();
                break;
            default:
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            Locale locale = Locale.getDefault();
            CharSequence name = null;

            switch (position) {
            case 0:
                name = getString(R.string.title_activity_add_income).toUpperCase(locale);
                break;
            case 1:
                name = getString(R.string.title_activity_add_expense).toUpperCase(locale);
                break;
            case 2:
                name = getString(R.string.title_activity_add_bill).toUpperCase(locale);
                break;
            default:
            }
            return name;
        }
    }

}
