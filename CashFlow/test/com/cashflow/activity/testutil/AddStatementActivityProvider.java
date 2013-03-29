package com.cashflow.activity.testutil;

import com.cashflow.statement.activity.AddStatementFragment;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class AddStatementActivityProvider implements Provider<AddStatementFragment> {

    @Override
    public AddStatementFragment get() {
        return new AddStatementFragment();
    }
}
