package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.util.ArrayList;
import java.util.List;

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
import com.cashflow.domain.StatementType;
import com.cashflow.statement.activity.ListStatementFragment;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Tabbed lists for statements.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ListActivity extends RoboSherlockFragmentActivity {

    private TabHost mTabHost;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_fragments);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);

        final TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        Bundle bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, StatementType.Income.toString());
        mTabsAdapter.addTab(mTabHost.newTabSpec("income").setIndicator(getString(R.string.title_activity_list_incomes)), ListStatementFragment.class,
                bundle);
        bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, StatementType.Expense.toString());
        mTabsAdapter.addTab(mTabHost.newTabSpec("expense").setIndicator(getString(R.string.title_activity_list_expenses)),
                ListStatementFragment.class, bundle);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * Helper class.
     * @author Janos_Gyula_Meszaros
     *
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final List<TabInfo> mTabs = new ArrayList<TabInfo>();

        /**
         * Constructor.
         * @param activity activity.
         * @param tabHost tabHost.
         * @param pager pager.
         */
        public TabsAdapter(final FragmentActivity activity, final TabHost tabHost, final ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        /**
         * add tab.
         * @param tabSpec tabSpec.
         * @param clss class
         * @param args args
         */
        public void addTab(final TabHost.TabSpec tabSpec, final Class<?> clss, final Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            final String tag = tabSpec.getTag();

            final TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(final int position) {
            final TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(final String tabId) {
            final int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            final TabWidget widget = mTabHost.getTabWidget();
            final int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
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
             * @param tag tag
             * @param clazz class
             * @param args arg
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
