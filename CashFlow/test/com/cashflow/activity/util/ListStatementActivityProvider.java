package com.cashflow.activity.util;

import com.cashflow.activity.ListExpensesActivity;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 *
 */
public class ListExpensesActivityProvider implements Provider<ListExpensesActivity> {

    @Override
    public ListExpensesActivity get() {
        return new ListExpensesActivity();
    }
}
