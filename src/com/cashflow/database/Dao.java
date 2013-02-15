package com.cashflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * .
 * @author Kornel_Refi
 */
public class Dao {
    private static final Logger LOG = LoggerFactory.getLogger(Dao.class);
    private final SQLiteDatabase writableDb;

    /**
     * Default constructor which get an activity.
     * @param activity
     *            Required for DbHelper.
     */
    public Dao(Activity activity) {
        // TODO what if activity is null?
        this.writableDb = createWritableDatabase(activity);
    }

    private SQLiteDatabase createWritableDatabase(Activity activity) {
        DbHelper mDbHelper = new DbHelper(activity);
        return mDbHelper.getWritableDatabase();
    }

    /**
     * Persists values to the database.
     * @param values
     *            Values to save.
     */
    public void save(ContentValues values) {
        long newRowId;
        // Insert the new row, returning the primary key value of the new row
        newRowId = writableDb.insert(DatabaseContracts.Statement.TABLE_NAME, DatabaseContracts.Statement.COLUMN_NAME_NULLABLE, values);
        LOG.debug("New row created with row ID: " + newRowId);
    }
}
