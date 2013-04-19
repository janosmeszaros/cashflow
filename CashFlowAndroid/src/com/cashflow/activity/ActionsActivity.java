package com.cashflow.activity;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.cashflow.R;
import com.cashflow.bill.activity.AddBillFragment;
import com.cashflow.statement.activity.AddIncomeFragment;
import com.cashflow.statement.activity.AddStatementFragment;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * List the actions in tabbed form.
 * @author Janos_Gyula_Meszaros
 */
public class ActionsActivity extends RoboSherlockFragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_actions);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(this);

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            final CharSequence title = mSectionsPagerAdapter.getPageTitle(i);
            final Tab tab = actionBar.newTab();
            tab.setText(title);
            tab.setTabListener(this);
            actionBar.addTab(tab);
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

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(final int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Constructor
         * @param manager
         *            {@link FragmentManager}
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
                break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            final Locale locale = Locale.getDefault();
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
