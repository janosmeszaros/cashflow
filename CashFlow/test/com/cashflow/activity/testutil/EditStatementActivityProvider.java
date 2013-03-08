package com.cashflow.activity.testutil;

import com.cashflow.activity.EditStatementActivity;
import com.google.inject.Provider;

/**
 * Provider for {@link EditStatementActivity}.
 * @author Janos_Gyula_Meszaros
 *
 */
public class EditStatementActivityProvider implements Provider<EditStatementActivity> {

    @Override
    public EditStatementActivity get() {
        return new EditStatementActivity();
    }

}
