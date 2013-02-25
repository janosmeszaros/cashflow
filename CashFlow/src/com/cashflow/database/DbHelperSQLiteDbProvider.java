package com.cashflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;

/**
 * DbHelper based {@link SQLiteDbProvider} implementation.
 * @author Janos_Gyula_Meszaros
 */
public class DbHelperSQLiteDbProvider implements SQLiteDbProvider {

    private final Application application;
    private static final Logger LOG = LoggerFactory.getLogger(DbHelperSQLiteDbProvider.class);

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
        LOG.debug("anyád");
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
