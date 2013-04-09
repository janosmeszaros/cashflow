package com.cashflow.activity.testutil;

import android.app.Activity;

import com.cashflow.R;
import com.google.inject.Provider;

/**
 * Provider for {@link Activity} class.
 * @author Kornel_Refi
 *
 */
public class AddIncomeStatementActivityProvider implements Provider<Activity> {

    @Override
    public Activity get() {
        final Activity actionsActivity = new Activity();
        actionsActivity.setContentView(R.layout.add_income_statement_fragment);
        return actionsActivity;
    }

}
