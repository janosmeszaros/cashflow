package com.cashflow.activity.testutil;

import com.cashflow.activity.statement.ListStatementActivity;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class ListStatementActivityProvider implements Provider<ListStatementActivity> {

    @Override
    public ListStatementActivity get() {
        return new ListStatementActivity();
    }
}
