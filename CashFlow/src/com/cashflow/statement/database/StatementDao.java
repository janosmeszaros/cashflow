package com.cashflow.statement.database;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;

import org.apache.commons.lang.Validate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.DaoParent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the statements.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class StatementDao extends DaoParent {
    private final SQLiteDbProvider provider;

    /**
     * Default constructor which gets a Provider. Can't be null.
     * @throws IllegalArgumentException when provider is null.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public StatementDao(final SQLiteDbProvider provider) {
        super(provider, AbstractStatement.class);
        nullCheck(provider);
        this.provider = provider;
    }

    /**
     * Returns all of the expenses.
     * @return Cursor which contains the data.
     */
    public Cursor getExpenses() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, EXPENSE_SELECTION, null, null, null, null);
    }

    /**
     * Returns all of the incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null);
    }

    /**
     * Returns recurring incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getRecurringIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
    }

    /**
     * Get Statement by id.
     * @param statementId of statement
     * @return statement
     */
    public Cursor getStatementById(final String statementId) {
        idCheck(statementId);

        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{statementId});
    }

    private void idCheck(final String statementId) {
        Validate.notEmpty(statementId);
    }

    private void nullCheck(final Object object) {
        Validate.notNull(object);
    }

}
