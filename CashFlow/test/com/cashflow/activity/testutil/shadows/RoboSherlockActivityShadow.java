package com.cashflow.activity.testutil.shadows;

import android.app.Activity;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

/**
 * {@link RoboSherlockActivity} shadow.
 * @author Kornel_Refi
 *
 */
@Implements(RoboSherlockActivity.class)
public class RoboSherlockActivityShadow extends SherlockActivityShadow {

    private ActionBarSherlock mSherlock;

    protected final ActionBarSherlock getSherlock() {
        if (mSherlock == null) {
            mSherlock = ActionBarSherlock.wrap(new Activity(), ActionBarSherlock.FLAG_DELEGATE);
        }
        return mSherlock;
    }

    @Override
    @Implementation
    public void setContentView(final int layoutResId) {
        getSherlock().setContentView(layoutResId);
    }

    @Implementation
    public ActionBar getSupportActionBar() {
        return getSherlock().getActionBar();
    }
}
