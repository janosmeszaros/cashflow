package com.cashflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public abstract class IncomeContract {
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String REAL_TYPE = " REAL";
	private static final String COMMA_SEP = ",";

    public static abstract class Income implements BaseColumns {
    	//if you instead set this to "null", then the framework will not insert a row when there are no values
    	public static final String COLUMN_NAME_NULLABLE = null;
    	public static final String TABLE_NAME = "income";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
        
        static final String SQL_CREATE_ENTRIES =
        		"CREATE TABLE " + IncomeContract.Income.TABLE_NAME + " (" +
        				IncomeContract.Income._ID + " INTEGER PRIMARY KEY," +
        				IncomeContract.Income.COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
        				IncomeContract.Income.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
        				IncomeContract.Income.COLUMN_NAME_NOTE + TEXT_TYPE +  " )";
        
        static final String SQL_DELETE_ENTRIES =
        		"DROP TABLE IF EXISTS " + IncomeContract.Income.TABLE_NAME;  

        private Income() {
    	}
    }
    
    
    
}
