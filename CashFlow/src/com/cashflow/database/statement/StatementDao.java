package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts;
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

    private SQLiteDbProvider provider;

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

    private void nullCheck(SQLiteDbProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Persists values to the database.
     * @param values
     *            Values to save.
     */
    public void save(ContentValues values) {
        long newRowId = provider.getWritableDb().insert(DatabaseContracts.AbstractStatement.TABLE_NAME,
                DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
    }

    /**
     * Updates statement row with specified id.
     * @param values
     *            data needs to be updated.
     * @param id
     *            row id.
     */
    public void update(ContentValues values, String id) {
        int update = provider.getWritableDb().update(TABLE_NAME, values, _ID + " = " + id, null);
        LOG.debug("Num of rows updated: " + update);
    }

    /**
     * Returns all of the expenses.
     * @return Cursor which contains the data.
     */
    public Cursor getExpenses() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor =
                db.query(TABLE_NAME, PROJECTION, EXPENSE_SELECTION,
                        null,
                        null, null, null);
        return cursor;
    }

    /**
     * Returns all of the incomes.
     * @return Cursor which contains the data.
     */
    public Cursor getIncomes() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor =
                db.query(TABLE_NAME, PROJECTION, INCOME_SELECTION,
                        null,
                        null, null, null);
        return cursor;
    }
}
