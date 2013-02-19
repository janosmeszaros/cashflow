package com.cashflow.database.statement;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DbHelper;
import com.google.inject.Inject;

public class DbHelperSQLiteDbProvider implements SQLiteDbProvider {

    private final Application application;

    @Inject
    public DbHelperSQLiteDbProvider(Application application) {
        //TODO nullcheck
        this.application = application;
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
