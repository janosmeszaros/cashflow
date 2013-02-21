package com.cashflow.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Basic interface for db provider.
 * @author Janos_Gyula_Meszaros
 */
public interface SQLiteDbProvider {

    /**
     * Returns a readable database.
     * @return SQLiteDatabase instance.
     */
    SQLiteDatabase getReadableDb();

    /**
     * Returns a writable database.
     * @return SQLiteDatabase instance.
     */
    SQLiteDatabase getWritableDb();

}
