package com.cashflow.activity.testutil.shadows;

import com.actionbarsherlock.app.SherlockActivity;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

/**
 * {@link SherlockActivity} shadow.
 * @author Kornel_Refi
 *
 */
@Implements(SherlockActivity.class)
public class SherlockActivityShadow extends ShadowActivity {

}
