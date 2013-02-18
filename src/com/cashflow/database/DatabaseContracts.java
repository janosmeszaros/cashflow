package com.cashflow.database;

import android.provider.BaseColumns;

/**
 * Database tables.
 * @author Kornel_Refi
 *
 */
public abstract class DatabaseContracts {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /**
     * Statement table.
     * @author Kornel_Refi
     *
     */
    public abstract static class Statement implements BaseColumns {
<<<<<<< .mine        //if you instead set this to "null", then the framework will not insert a row when there are no values
=======        // if you instead set this to "null", then the framework will not insert a row when there are no values
>>>>>>> .theirs        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "income";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_IS_INCOME = "is_income";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";

        static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DatabaseContracts.Statement.TABLE_NAME + " (" + DatabaseContracts.Statement._ID + " INTEGER PRIMARY KEY,"
                + DatabaseContracts.Statement.COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP + DatabaseContracts.Statement.COLUMN_NAME_IS_INCOME + INTEGER_TYPE + COMMA_SEP
                + DatabaseContracts.Statement.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP + DatabaseContracts.Statement.COLUMN_NAME_NOTE + TEXT_TYPE + " )";

        static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContracts.Statement.TABLE_NAME;

        private Statement() {
        }
    }

}
