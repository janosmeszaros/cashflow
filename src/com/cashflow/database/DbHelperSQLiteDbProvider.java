package com.cashflow.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;

/**
 * DbHelper based {@link SQLiteDbProvider} implementation.
 * @author Janos_Gyula_Meszaros
 */
public class DbHelperSQLiteDbProvider implements SQLiteDbProvider {

    private final Application application;

    /**
     * Constructor.
     * @param application
     *            Context which is used to create database instances. Can't be <code>null</code>.
     * @throws IllegalArgumentException
     *             if argument is null.
     */
    @Inject
    public DbHelperSQLiteDbProvider(Application application) {
        nullCheck(application);
        this.application = application;
    }

    private void nullCheck(Application application) {
        if (application == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public SQLiteDatabase getReadableDb() {
        return DbHelper.getReadableDatabase(application);
    }

    @Override
    public SQLiteDatabase getWritableDb() {
        return DbHelper.getWritableDatabase(application);
    }

}
