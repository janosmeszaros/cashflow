package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.util.Locale;

import roboguice.activity.RoboFragmentActivity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.cashflow.R;
import com.cashflow.bill.activity.AddBillFragment;
import com.cashflow.statement.activity.AddStatementFragment;
import com.cashflow.statement.database.StatementType;

/**
 * List the actions in tabbed form.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ActionsActivity extends RoboFragmentActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiped_add_statement);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.swiped_add_statement, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Constructor
         * @param fm fm
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AddStatementFragment();
            Bundle args = new Bundle();
            switch (position) {
            case 0:
                args.putString(STATEMENT_TYPE_EXTRA, StatementType.Income.toString());
                break;
            case 1:
                args.putString(STATEMENT_TYPE_EXTRA, StatementType.Expense.toString());
                break;
            case 2:
                fragment = new AddBillFragment();
                break;
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            CharSequence name = null;

            switch (position) {
            case 0:
                name = getString(R.string.title_activity_add_income).toUpperCase(l);
                break;
            case 1:
                name = getString(R.string.title_activity_add_expense).toUpperCase(l);
                break;
            case 2:
                name = getString(R.string.title_activity_add_bill).toUpperCase(l);
                break;
            }
            return name;
        }
    }

}
