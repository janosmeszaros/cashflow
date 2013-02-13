package com.cashflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public abstract class IncomeContract implements BaseColumns {
	

    public static abstract class Income implements BaseColumns {
    	//if you instead set this to "null", then the framework will not insert a row when there are no values
    	public static final String COLUMN_NAME_NULLABLE = null;
    	public static final String TABLE_NAME = "income";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
        
        private Income() {
    	}
    }
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + IncomeContract.Income.TABLE_NAME + " (" +
    	    		IncomeContract.Income._ID + " INTEGER PRIMARY KEY," +
    	    		IncomeContract.Income.COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
    	    IncomeContract.Income.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
    	    IncomeContract.Income.COLUMN_NAME_NOTE + TEXT_TYPE +  " )";
    
    private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + IncomeContract.Income.TABLE_NAME;
    
    
    public static class IncomeDbHelper extends SQLiteOpenHelper {

    	// If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Income.db";
        
        public IncomeDbHelper(Context context) {
        	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}
        
    	@Override
    	public void onCreate(SQLiteDatabase db) {
    		db.execSQL(SQL_CREATE_ENTRIES);

    	}

    	@Override
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    		// This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
    	}
    	
    	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    	
    }
    
    
}
