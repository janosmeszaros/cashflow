package com.cashflow.activity.testutil;

import com.cashflow.activity.statement.AddStatementActivity;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class AddStatementActivityProvider implements Provider<AddStatementActivity> {

    @Override
    public AddStatementActivity get() {
        return new AddStatementActivity();
    }
}
