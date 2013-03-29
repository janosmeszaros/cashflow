package com.cashflow;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.TabHost.OnTabChangeListener;

import com.cashflow.statement.activity.AddStatementActivity;
import com.cashflow.statement.database.StatementType;

/**
 * proba
 * @author Janos_Gyula_Meszaros
 *
 */
public class SwipedAddStatement extends RoboFragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_swiped_add_statement);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title_bar);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, StatementType.Income.toString());
        mTabHost.addTab(mTabHost.newTabSpec("Income").setIndicator("Income"), AddStatementActivity.class, bundle);

        Bundle expenseBundle = new Bundle();
        expenseBundle.putString(STATEMENT_TYPE_EXTRA, StatementType.Expense.toString());
        mTabHost.addTab(mTabHost.newTabSpec("Expense").setIndicator("Expense"), AddStatementActivity.class, expenseBundle);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tag) {
                //TabInfo newTab = this.mapTabInfo.get(tag);
                int pos = mTabHost.getCurrentTab();
                mPager.setCurrentItem(pos);
            }
        });

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(Fragment.instantiate(this, AddStatementActivity.class.getName(), bundle));
        fragments.add(Fragment.instantiate(this, AddStatementActivity.class.getName(), expenseBundle));

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }
        });

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        /**Constructor.
         * @param fm FragmentManager
         * @param fragments fragments
         */
        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);

        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
