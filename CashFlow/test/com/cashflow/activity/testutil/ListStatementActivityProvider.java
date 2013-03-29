package com.cashflow.activity.testutil;

import com.cashflow.statement.activity.ListStatementFragment;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class ListStatementActivityProvider implements Provider<ListStatementFragment> {

    @Override
    public ListStatementFragment get() {
        return new ListStatementFragment();
    }
}
