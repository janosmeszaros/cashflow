package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_CATEGORY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECTION_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.SQLiteDbProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the statements.
 * @author Kornel_Refi
 */
@Singleton
public class StatementDao {
    private static final Logger LOG = LoggerFactory.getLogger(StatementDao.class);

    private final SQLiteDbProvider provider;

    /**
     * Default constructor which get an Provider.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public StatementDao(SQLiteDbProvider provider) {
        nullCheck(provider);
        this.provider = provider;
    }

    /**
     * Persists values to the database.
     * @param values
     *            Values to save.
     */
    public void save(ContentValues values) {
        long newRowId = provider.getWritableDb().insert(TABLE_NAME, COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
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
        boolean result = false;
        int update = provider.getWritableDb().update(TABLE_NAME, values, _ID + " = " + id, null);

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
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_CATEGORY_ID, EXPENSE_SELECTION, null, null, null, null);
        return cursor;
    }

    /**
     * Returns all of the incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getIncomes() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_CATEGORY_ID, INCOME_SELECTION, null, null, null, null);
        return cursor;
    }

    /**
     * Returns recurring incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getRecurringIncomes() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_CATEGORY_ID, RECURRING_INCOME_SELECTION, null, null, null, null);
        return cursor;
    }

    private void nullCheck(SQLiteDbProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get Statement by id.
     * @param id of statement
     * @return statement
     */
    public Cursor getStatementById(String id) {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.rawQuery("SELECT " + TABLE_NAME + "." + _ID + " AS statementId " + "," + AbstractCategory.TABLE_NAME + "." + _ID
                + " AS categoryId " + "," + COLUMN_NAME_AMOUNT + "," + AbstractCategory.COLUMN_NAME_CATEGORY_NAME + "," + COLUMN_NAME_DATE + ","
                + COLUMN_NAME_NOTE + "," + COLUMN_NAME_INTERVAL + "," + COLUMN_NAME_IS_INCOME + " FROM " + AbstractCategory.TABLE_NAME + ","
                + AbstractStatement.TABLE_NAME + " WHERE " + SELECTION_BY_ID, new String[]{id});
        return cursor;
    }
}
