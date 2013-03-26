package com.cashflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database handler class.
 * @author Kornel_Refi
 */
public final class DbHelper extends SQLiteOpenHelper {
    private static DbHelper reader;
    private static DbHelper writer;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param context
     *            Required for SQLiteOpenHelper.
     */
    private DbHelper(final Context context) {
        super(context, DatabaseContracts.DATABASE_NAME, null, DatabaseContracts.DATABASE_VERSION);
    }

    /**
     * Returns a read-only {@link SQLiteDatabase} instance.
     * @param context
     *            context for the databaseHelper.
     * @return readable database.
     */
    public static synchronized SQLiteDatabase getReadableDatabase(final Context context) {
        if (reader == null) {
            reader = new DbHelper(context.getApplicationContext());
        }
        return reader.getReadableDatabase();
    }

    /**
     * Returns a writable {@link SQLiteDatabase} instance.
     * @param context
     *            context for the databasehelper.
     * @return writable database.
     */
    public static synchronized SQLiteDatabase getWritableDatabase(final Context context) {
        if (writer == null) {
            writer = new DbHelper(context.getApplicationContext());
        }
        return writer.getWritableDatabase();
    }

    /**
     * This method is invoked when the database doesn't exists.
     * @param dataBase {@link SQLiteDatabase}
     */
    @Override
    public void onCreate(final SQLiteDatabase dataBase) {
        dataBase.execSQL(DatabaseContracts.AbstractStatement.SQL_CREATE_ENTRIES);
        dataBase.execSQL(DatabaseContracts.AbstractCategory.SQL_CREATE_ENTRIES);
        dataBase.execSQL(DatabaseContracts.AbstractBill.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(final SQLiteDatabase dataBase, final int oldVersion, final int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        dataBase.execSQL(DatabaseContracts.AbstractStatement.SQL_DELETE_ENTRIES);
        dataBase.execSQL(DatabaseContracts.AbstractCategory.SQL_DELETE_ENTRIES);
        dataBase.execSQL(DatabaseContracts.AbstractBill.SQL_DELETE_ENTRIES);
        onCreate(dataBase);
    }

    // @Override
    // public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // onUpgrade(db, oldVersion, newVersion);
    // }

}
