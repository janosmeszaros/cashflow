package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Statement;

/**
 * DAO for {@link Statement}.
 * @author Kornel_Refi
 */
public interface StatementDAO {

    /**
     * Persists values to the database.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(Statement statement);

    /**
     * Updates a {@link Statement} with specified id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(Statement statement, String id);

    /**
     * Returns all the {@link Statement}.
     * @return {@link List} of {@link Statement}.
     */
    public List<Statement> getAllStatements();

    /**
     * Returns all of the expenses.
     * @return {@link List} of {@link Statement} with expense type.
     */
    public List<Statement> getExpenses();

    /**
     * Returns all of the incomes.
     * @return {@link List} of {@link Statement} with income type.
     */
    public List<Statement> getIncomes();

    /**
     * Returns recurring incomes.
     * @return {@link List} of {@link Statement} which contains the data.
     */
    public List<Statement> getRecurringIncomes();

    /**
     * Get Statement by id.
     * @param statementId of statement
     * @return {@link Statement} with the specified id.
     */
    public Statement getStatementById(String statementId);

}