package com.cashflow.database.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;

import com.cashflow.database.DatabaseContracts;
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
        long newRowId;
        // Insert the new row, returning the primary key value of the new row

        newRowId = provider.getWritableDb().insert(DatabaseContracts.AbstractStatement.TABLE_NAME,
                DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
    }
}
