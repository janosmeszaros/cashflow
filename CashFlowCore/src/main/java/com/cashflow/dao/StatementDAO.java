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
     * @param statement
     *            {@link Statement} to save.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    boolean save(Statement statement);

    /**
     * Updates a {@link Statement} with specified id.
     * @param statement
     *            updated {@link Statement}
     * @param statementId
     *            Updatable {@link Statement}'s id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    boolean update(Statement statement, String statementId);

    /**
     * Deletes the given {@link Statement} from database.
     * @param statement
     *            {@link Statement} to delete.
     */
    void delete(Statement statement);

    /**
     * Returns all the {@link Statement}.
     * @return {@link List} of {@link Statement}.
     */
    List<Statement> getAllStatements();

    /**
     * Returns all of the expenses.
     * @return {@link List} of {@link Statement} with expense type.
     */
    List<Statement> getExpenses();

    /**
     * Returns all of the incomes.
     * @return {@link List} of {@link Statement} with income type.
     */
    List<Statement> getIncomes();

    /**
     * Returns recurring incomes.
     * @return {@link List} of {@link Statement} which contains the data.
     */
    List<Statement> getRecurringIncomes();

    /**
     * Get Statement by id.
     * @param statementId
     *            of statement
     * @return {@link Statement} with the specified id.
     */
    Statement getStatementById(String statementId);

}