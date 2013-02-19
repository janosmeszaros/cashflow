package com.cashflow.database.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.content.Context;

import com.cashflow.database.DatabaseContracts;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * .
 * @author Kornel_Refi
 */
@Singleton
public class StatementDao {
    private static final Logger LOG = LoggerFactory.getLogger(StatementDao.class);

    private SQLiteDbProvider provider;

    /**
     * Default constructor which get an activity.
     * @param context
     *            Required for DbHelper.
     */
    //TODO javadoc refresh
    @Inject
    public StatementDao(SQLiteDbProvider provider) {
        this.provider = provider;
    }

    private void nullCheck(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Persists values to the database.
     * @param values
     *            Values to save.
     */
    public void save(ContentValues values) {
        long newRowId;
        // Insert the new row, returning the primary key value of the new row

        newRowId = provider.getWritableDb().insert(DatabaseContracts.AbstractStatement.TABLE_NAME,
                DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
    }
}
