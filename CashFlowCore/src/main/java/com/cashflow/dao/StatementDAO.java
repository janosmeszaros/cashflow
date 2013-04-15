package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Statement;

public interface StatementDAO {

    /**
     * Persists values to the database.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(Statement statement);

    /**
     * Updates a row with specified id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(Statement statement, String id);

    /**
     * Returns all {@link Statement}.
     * @return Cursor which contains the data.
     */
    public List<Statement> getAllStatements();

    /**
     * Returns all of the expenses.
     * @return Cursor which contains the data.
     */
    public List<Statement> getExpenses();

    /**
     * Returns all of the incomes.
     * @return Cursor which contains the data.
     */
    public List<Statement> getIncomes();

    /**
     * Returns recurring incomes.
     * @return Cursor which contains the data.
     */
    public List<Statement> getRecurringIncomes();

    /**
     * Get Statement by id.
     * @param statementId of statement
     * @return statement
     */
    public Statement getStatementById(String statementId);

}