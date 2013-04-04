package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.cashflow.R;
import com.cashflow.statement.activity.ListStatementFragment;
import com.cashflow.statement.database.StatementType;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Tabbed lists for statements.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ListActivity extends RoboSherlockFragmentActivity implements TabListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_actions);

        final ActionBar actionBar = getSupportActionBar();
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
         * FragmentPagerAdapter.
         * @param manager {@link FragmentManager}.
         */
        public SectionsPagerAdapter(final FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(final int position) {
            final Fragment fragment = new ListStatementFragment();
            final Bundle args = new Bundle();
            if (position == 0) {
                args.putString(STATEMENT_TYPE_EXTRA, StatementType.Income.toString());
            } else {
                args.putString(STATEMENT_TYPE_EXTRA, StatementType.Expense.toString());
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            final Locale locale = Locale.getDefault();
            CharSequence name = null;

            switch (position) {
            case 0:
                name = getString(R.string.title_activity_list_incomes).toUpperCase(locale);
                break;
            case 1:
                name = getString(R.string.title_activity_list_expenses).toUpperCase(locale);
                break;
            default:
                break;
            }
            return name;
        }
    }

}
