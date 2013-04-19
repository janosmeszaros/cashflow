package com.cashflow.activity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.cashflow.R;
import com.cashflow.activity.ActionsActivity.SectionsPagerAdapter;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.activity.testutil.shadows.ActionBarSherlockRobolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ActionsActivityTest {

    private ActionsActivity underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ActionBarSherlock.registerImplementation(ActionBarSherlockRobolectric.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockCompat.class);

        final ActivityModule module = new ActivityModule(new ActivityProvider());

        ActivityModule.setUp(this, module);
        underTest = new ActionsActivity();

    }

    @After
    public void tearDown() {
        ActionBarSherlock.registerImplementation(ActionBarSherlockCompat.class);
        ActionBarSherlock.registerImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockRobolectric.class);
        TestGuiceModule.tearDown();
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetNavigationModeToTabs() {
        underTest.onCreate(null);

        final ActionBar actionBar = underTest.getSupportActionBar();
        assertThat(actionBar.getNavigationMode(), equalTo(ActionBar.NAVIGATION_MODE_TABS));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetPagersAdapterToSectionPagerAdapter() {
        underTest.onCreate(null);

        final ViewPager mViewPager = (ViewPager) underTest.findViewById(R.id.pager);
        final ActionBar actionBar = underTest.getSupportActionBar();
        assertThat(actionBar.getNavigationMode(), equalTo(ActionBar.NAVIGATION_MODE_TABS));
        assertThat(mViewPager.getAdapter(), instanceOf(SectionsPagerAdapter.class));
        assertThat(actionBar.getTabCount(), equalTo(mViewPager.getAdapter().getCount()));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTabsToActionBar() {
        underTest.onCreate(null);

        final ViewPager mViewPager = (ViewPager) underTest.findViewById(R.id.pager);
        final ActionBar actionBar = underTest.getSupportActionBar();
        assertThat(actionBar.getTabCount(), equalTo(mViewPager.getAdapter().getCount()));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetCorrectNamesToTheTabs() {
        underTest.onCreate(null);

        final ViewPager mViewPager = (ViewPager) underTest.findViewById(R.id.pager);
        final ActionBar actionBar = underTest.getSupportActionBar();
        final PagerAdapter adapter = mViewPager.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            assertThat(actionBar.getTabAt(i).getText(), equalTo(adapter.getPageTitle(i)));
        }
    }

    @Test
    public void testOnTabSelectedWhenCalledThenShouldSetViewPagersItemToTheSelectedTabsNumber() {
        underTest.onCreate(null);

        final ViewPager viewPager = (ViewPager) underTest.findViewById(R.id.pager);
        final ActionBar actionBar = underTest.getSupportActionBar();
        for (int i = 0; i < actionBar.getTabCount(); i++) {
            underTest.onTabSelected(actionBar.getTabAt(i), null);

            assertThat(viewPager.getCurrentItem(), equalTo(i));
        }
    }

    @Test
    public void testOnPageSelectedWhenCalledThenShouldSetActionBarsNavigationItemToCurrentPos() {
        underTest.onCreate(null);

        final ActionBar actionBar = underTest.getSupportActionBar();
        for (int i = 0; i < actionBar.getTabCount(); i++) {
            underTest.onPageSelected(i);

            assertThat(actionBar.getSelectedNavigationIndex(), equalTo(i));
        }
    }

}
