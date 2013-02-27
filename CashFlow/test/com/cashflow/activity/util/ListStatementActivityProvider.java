package com.cashflow.activity.util;

import com.cashflow.activity.ListStatementActivity;
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
