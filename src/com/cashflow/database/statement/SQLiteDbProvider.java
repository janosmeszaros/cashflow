package com.cashflow.database.statement;

import android.database.sqlite.SQLiteDatabase;

public interface SQLiteDbProvider {

    SQLiteDatabase getReadableDb();

    SQLiteDatabase getWritableDb();

}
