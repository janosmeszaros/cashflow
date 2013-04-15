package com.cashflow.activity.testutil.shadows;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;

/**
 * ActionBarSherlockRobolectric.
 * @author Kornel_Refi
 *
 */
@ActionBarSherlock.Implementation(api = 0)
public class ActionBarSherlockRobolectric extends ActionBarSherlockCompat {
    private final ActionBar actionBar;

    /**
     * Constructor.
     * @param activity activity.
     * @param flags flags.
     */
    public ActionBarSherlockRobolectric(Activity activity, int flags) {
        super(activity, flags);
        actionBar = new MockActionBar(activity);
    }

    @Override
    public void setContentView(int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View contentView = layoutInflater.inflate(layoutResId, null);

        shadowOf(mActivity).setContentView(contentView);
    }

    @Override
    public void setContentView(View view) {
        shadowOf(mActivity).setContentView(view);
    }

    @Override
    public ActionBar getActionBar() {
        return actionBar;
    }

    @Override
    protected Context getThemedContext() {
        return mActivity;
    }
}