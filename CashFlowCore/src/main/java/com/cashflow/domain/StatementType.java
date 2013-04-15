package com.cashflow.domain;

/**
 * Enumerate the statement types. <code>RecurringIncome</code> is a subtype of <code>Income</code>
 * @author Janos_Gyula_Meszaros
 */
public enum StatementType {
    Expense, Income;

    /**
     * Decides that this {@link StatementType} is income.
     * @return true if in this {@link StatementType} is <code>Income</code> or <code>RecurringIncome</code>. False otherwise.
     */
    public boolean isIncome() {
        return equals(Income);
    }

}
