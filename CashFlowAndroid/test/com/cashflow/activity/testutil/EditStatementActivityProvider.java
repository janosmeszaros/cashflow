package com.cashflow.activity.testutil;

import com.cashflow.statement.activity.edit.EditStatementActivity;
import com.google.inject.Provider;

/**
 * Provider for {@link EditStatementActivity}.
 * @author Janos_Gyula_Meszaros
 *
 */
public class EditStatementActivityProvider implements Provider<EditStatementActivity> {

    private final EditStatementActivity clazz;

    /**
     * Constructor
     * @param clazz which will be returned be get.
     */
    public EditStatementActivityProvider(final EditStatementActivity clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public EditStatementActivity get() {
        return clazz;
    }

}
