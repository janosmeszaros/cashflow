package com.cashflow.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.actionbarsherlock.view.Window;
import com.cashflow.R;
import com.cashflow.bill.activity.ListBillFragment;
import com.cashflow.statement.activity.list.ListExpenseFragment;
import com.cashflow.statement.activity.list.ListIncomeFragment;
import com.cashflow.statement.activity.list.ListRecurringIncomeFragment;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Tabbed lists for statements.
 * @author Janos_Gyula_Meszaros
 */
public class ListActivity extends RoboSherlockFragmentActivity {

    @InjectView(android.R.id.tabhost)
    private TabHost tabHost;
    @InjectView(R.id.pager)
    private ViewPager viewPager;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_fragments);
        tabHost.setup();
        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);

        viewPager.setOffscreenPageLimit(3);

        addIncomeTab();
        addExpenseTab();
        addRecurringIncomeTab();
        addListBillTab();

        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    private void addIncomeTab() {
        tabsAdapter.addTab(tabHost.newTabSpec("income").setIndicator(getString(R.string.title_activity_list_incomes)),
                ListIncomeFragment.class, null);
    }

    private void addExpenseTab() {
        tabsAdapter.addTab(tabHost.newTabSpec("expense").setIndicator(getString(R.string.title_activity_list_expenses)),
                ListExpenseFragment.class, null);
    }

    private void addRecurringIncomeTab() {
        tabsAdapter.addTab(tabHost.newTabSpec("recurringIncome").setIndicator(getString(R.string.title_activity_list_recurringincomes)),
                ListRecurringIncomeFragment.class, null);
    }

    private void addListBillTab() {
        tabsAdapter.addTab(tabHost.newTabSpec("bill").setIndicator(getString(R.string.title_activity_list_bill)),
                ListBillFragment.class, null);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
    }

    /**
     * Helper class.
     * @author Janos_Gyula_Meszaros
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context context;
        private final TabHost tabHost;
        private final ViewPager viewPager;
        private final List<TabInfo> mTabs = new ArrayList<TabInfo>();

        /**
         * Constructor.
         * @param activity
         *            activity.
         * @param tabHost
         *            tabHost.
         * @param pager
         *            pager.
         */
        public TabsAdapter(final FragmentActivity activity, final TabHost tabHost, final ViewPager pager) {
            super(activity.getSupportFragmentManager());
            this.context = activity;
            this.tabHost = tabHost;
            this.viewPager = pager;

            tabHost.setOnTabChangedListener(this);
            viewPager.setAdapter(this);
            viewPager.setOnPageChangeListener(this);
        }

        /**
         * add tab.
         * @param tabSpec
         *            tabSpec.
         * @param clss
         *            class
         * @param args
         *            args
         */
        public void addTab(final TabHost.TabSpec tabSpec, final Class<?> clss, final Bundle args) {
            tabSpec.setContent(new DummyTabFactory(context));
            final String tag = tabSpec.getTag();

            final TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            tabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(final int position) {
            final TabInfo info = mTabs.get(position);
            return Fragment.instantiate(context, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(final String tabId) {
            final int position = tabHost.getCurrentTab();
            viewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final TabWidget widget = tabHost.getTabWidget();
            final int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            tabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
        }

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            /**
             * Constructor.
             * @param tag
             *            tag
             * @param clazz
             *            class
             * @param args
             *            arg
             */
            TabInfo(final String tag, final Class<?> clazz, final Bundle args) {
                this.tag = tag;
                clss = clazz;
                this.args = args;
            }

            public String getTag() {
                return tag;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(final Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(final String tag) {
                final View view = new View(mContext);
                view.setMinimumWidth(0);
                view.setMinimumHeight(0);
                return view;
            }
        }
    }
}
