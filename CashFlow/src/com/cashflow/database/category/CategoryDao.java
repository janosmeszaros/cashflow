package com.cashflow.database.category;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_NULLABLE;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.TABLE_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.SQLiteDbProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the categories.
 * @author Kornel_Refi
 */
@Singleton
public class CategoryDao {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryDao.class);

    private final SQLiteDbProvider provider;

    /**
     * Default constructor which get an Provider.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public CategoryDao(SQLiteDbProvider provider) {
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
        long newRowId = provider.getWritableDb().insert(TABLE_NAME, COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
    }

    /**
     * Updates a category row with specified id.
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
     * Returns all of the categories.
     * @return Cursor which contains the data.
     */
    public Cursor getCategories() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(TABLE_NAME, PROJECTION, null, null, null, null, null);
        return cursor;
    }

}
