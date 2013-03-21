package com.cashflow.database;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;

import com.google.inject.Inject;

/**
 * a
 * @author Janos_Gyula_Meszaros
 *
 * @param <E>
 */
public class ParentDao<E extends Tables> {
    private static final Logger LOG = LoggerFactory.getLogger(ParentDao.class);
    private final SQLiteDbProvider provider;
    private E table;

    /**
     * Default constructor which gets a Provider. Can't be null.
     * @throws IllegalArgumentException when provider is null.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public ParentDao(SQLiteDbProvider provider) {
        nullCheck(provider);
        this.provider = provider;
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

        long newRowId = provider.getWritableDb().insert(table.getName(), table.getNullableColumnName(), values);

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
