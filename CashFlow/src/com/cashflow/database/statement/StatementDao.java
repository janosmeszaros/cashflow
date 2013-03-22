package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.ParentDao;
import com.cashflow.database.SQLiteDbProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the statements.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class StatementDao extends ParentDao {
    private static final String EQUALS = " = ?";

    private static final Logger LOG = LoggerFactory.getLogger(StatementDao.class);

    private final SQLiteDbProvider provider;

    /**
     * Default constructor which gets a Provider. Can't be null.
     * @throws IllegalArgumentException when provider is null.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public StatementDao(SQLiteDbProvider provider) {
        super(provider, AbstractStatement.TABLE_NAME);
        nullCheck(provider);
        this.provider = provider;
    }

    /**
     * Updates a statement row with specified id.
     * @param values
     *            data needs to be updated.
     * @param id
     *            row id.
     * @return <code>true</code> if 1 or more records updated, otherwise <code>false</code>
     */
    public boolean update(ContentValues values, String id) {
        validateUpdateParams(values, id);

        boolean result = false;
        int update = provider.getWritableDb().update(TABLE_NAME, values, _ID + EQUALS, new String[]{id});

        if (update > 0) {
            result = true;
        }

        LOG.debug("Num of rows updated: " + update);
        return result;
    }

    /**
     * Returns all of the expenses.
     * @return Cursor which contains the data.
     */
    public Cursor getExpenses() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, EXPENSE_SELECTION, null, null, null, null);
        return cursor;
    }

    /**
     * Returns all of the incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getIncomes() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null);
        return cursor;
    }

    /**
     * Returns recurring incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getRecurringIncomes() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
        return cursor;
    }

    /**
     * Get Statement by id.
     * @param id of statement
     * @return statement
     */
    public Cursor getStatementById(String id) {
        idCheck(id);

        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{id});
        return cursor;
    }

    private void validateUpdateParams(ContentValues values, String id) {
        nullCheck(values);
        idCheck(id);
    }

    private void idCheck(String id) {
        Validate.notEmpty(id);
    }

    private void nullCheck(Object object) {
        Validate.notNull(object);
    }

}
