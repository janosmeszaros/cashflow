package com.cashflow.activity.testutil;

import com.cashflow.statement.activity.list.ListStatementFragment;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class ListStatementActivityProvider implements Provider<ListStatementFragment> {

    private final ListStatementFragment fragment;

    /**
     * Constructor.
     * @param fragment which will be returned be get();
     */
    public ListStatementActivityProvider(final ListStatementFragment fragment) {
        super();
        this.fragment = fragment;
    }

    @Override
    public ListStatementFragment get() {
        return fragment;
    }
}
