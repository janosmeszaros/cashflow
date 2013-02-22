package com.cashflow.activity.util;

import com.cashflow.activity.ListExpensesActivity;
import com.google.inject.Provider;

public class ListExpensesActivityProvider implements Provider<ListExpensesActivity> {

    @Override
    public ListExpensesActivity get() {
        return new ListExpensesActivity();
    }
}
