package com.cashflow.database;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;

/**
 * a
 * @author Janos_Gyula_Meszaros
 */
public class ParentDao {
    private static final Logger LOG = LoggerFactory.getLogger(ParentDao.class);
    private final SQLiteDbProvider provider;
    private String table;

    /**
     * Default constructor which gets a Provider. Can't be null.
     * @throws IllegalArgumentException when provider is null.
     * @param provider Provider to get database.
     * @param tableName table
     */
    public ParentDao(SQLiteDbProvider provider, String tableName) {
        nullCheck(provider);
        this.provider = provider;
        this.table = tableName;
    }

    /**
     * Persists values to the database.
     * @param values Values to save. Can not be null.
     * @throws IllegalArgumentException when <code>values</code> is null.
     * @return true if save was successful, false otherwise.
     */
    public boolean save(ContentValues values) {
        nullCheck(values);
        boolean result = false;

        long newRowId = provider.getWritableDb().insert(table, null, values);

        if (newRowId >= 0) {
            result = true;
            LOG.debug("New row created with row ID: " + newRowId);
        }

        return result;
    }

    private void nullCheck(Object object) {
        Validate.notNull(object);
    }
}
