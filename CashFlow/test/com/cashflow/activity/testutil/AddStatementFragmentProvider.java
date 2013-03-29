package com.cashflow.activity.testutil;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cashflow.activity.ActionsActivity;
import com.cashflow.statement.activity.AddStatementFragment;
import com.google.inject.Provider;

/**
 * {@link ListExpensesActivity} {@link Provider}.
 * @author Kornel_Refi
 */
public class AddStatementFragmentProvider implements Provider<AddStatementFragment> {

    @Override
    public AddStatementFragment get() {
        AddStatementFragment fragment = new AddStatementFragment();

        FragmentManager fragmentManager = new ActionsActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }
}
