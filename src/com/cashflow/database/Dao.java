package com.cashflow.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	private SQLiteDatabase writableDb;
	
	public Dao(Activity activity) {
		DbHelper mDbHelper = new DbHelper(activity);
		// Gets the data repository in write mode
		this.writableDb = mDbHelper.getWritableDatabase();
	}
	
	public void save(ContentValues values){
		// Insert the new row, returning the primary key value of the new row
				long newRowId;
				newRowId = writableDb.insert(
						DatabaseContracts.Statement.TABLE_NAME,
						DatabaseContracts.Statement.COLUMN_NAME_NULLABLE,
				         values);
				
				System.out.println("newRowID: "+ newRowId);
	}
	
}
