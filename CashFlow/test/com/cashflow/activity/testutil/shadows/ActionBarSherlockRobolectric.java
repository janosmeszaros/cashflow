package com.cashflow.activity.testutil.shadows;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.internal.ActionBarSherlockNative;

/**
 * ActionBarSherlockRobolectric.
 * @author Kornel_Refi
 *
 */
@ActionBarSherlock.Implementation(api = 0)
public class ActionBarSherlockRobolectric extends ActionBarSherlockNative {

    /**
     * Constructor
     * @param activity {@link Activity}
     * @param flags flags
     */
    public ActionBarSherlockRobolectric(final Activity activity, final int flags) {
        super(activity, flags);
    }

    @Override
    public void setContentView(final int layoutResId) {
        final LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        final View contentView = layoutInflater.inflate(layoutResId, null);

        shadowOf(mActivity).setContentView(contentView);
    }

    @Override
    public void setContentView(final View view) {
        shadowOf(mActivity).setContentView(view);
    }
}