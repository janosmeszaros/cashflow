package com.cashflow.database.statement;

/**
 * Enumerate the statement types.
 * @author Janos_Gyula_Meszaros
 */
public enum StatementType {
    Expense, Income;

    /**
     * Decides that this {@link StatementType} is income.
     * @return true if in this {@link StatementType} is <code>Income</code>. False otherwise.
     */
    public boolean isIncome() {
        return equals(Income);
    }

}
