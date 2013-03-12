package com.cashflow.database;

import android.provider.BaseColumns;

import com.cashflow.R;

/**
 * Database tables.
 * @author Kornel_Refi
 */
public abstract class DatabaseContracts {
    public static final String DATABASE_NAME = "CashFlow.db";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String OPENNING_BRACKET = " (";
    private static final String CLOSSING_BRACKET = " )";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    /**
     * Statement table.
     * @author Kornel_Refi
     */
    public abstract static class AbstractStatement implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "statement";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_IS_INCOME = "is_income";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_INTERVAL = "interval";
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};
        public static final int[] TO_VIEWS = {R.id.row_id, R.id.row_amount, R.id.row_date, R.id.row_note, R.id.row_interval};

        public static final String EXPENSE_SELECTION = OPENNING_BRACKET + COLUMN_NAME_IS_INCOME + " == 0)";
        public static final String INCOME_SELECTION = OPENNING_BRACKET + COLUMN_NAME_IS_INCOME + " == 1)";

        static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPENNING_BRACKET + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP + COLUMN_NAME_IS_INCOME + INTEGER_TYPE + COMMA_SEP + COLUMN_NAME_DATE + TEXT_TYPE
                + COMMA_SEP + COLUMN_NAME_NOTE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_INTERVAL + TEXT_TYPE + CLOSSING_BRACKET;

        static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        private AbstractStatement() {
        }
    }

    /**
     * Category table.
     * @author Kornel_Refi
     */
    public abstract static class AbstractCategory implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY_NAME = "name";
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_CATEGORY_NAME};

        static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPENNING_BRACKET + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_CATEGORY_NAME + REAL_TYPE + CLOSSING_BRACKET;

        static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        private AbstractCategory() {
        }
    }

}
