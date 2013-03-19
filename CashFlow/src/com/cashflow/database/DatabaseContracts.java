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
    public static final int DATABASE_VERSION = 4;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String OPEN_PARENTHESIS = " (";
    private static final String CLOSE_PARENTHESIS = " )";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String EQUALS_INCOME = " = 1";
    private static final String EQUALS_EXPENSE = " = 0";
    private static final String AND = " AND ";
    private static final String DOT = ".";
    private static final String PRIMARY_KEY = " PRIMARY KEY";

    /**
     * Category table.
     * @author Kornel_Refi
     */
    public abstract static class AbstractCategory implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY_NAME = "name";
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_CATEGORY_NAME};

        static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPEN_PARENTHESIS + _ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP
                + COLUMN_NAME_CATEGORY_NAME + REAL_TYPE + CLOSE_PARENTHESIS;

        static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        private AbstractCategory() {
        }
    }

    /**
      * Statement table.
      * @author Kornel_Refi
      */
    public abstract static class AbstractStatement implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "statement";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORY = "category_id";
        public static final String COLUMN_NAME_IS_INCOME = "is_income";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_INTERVAL = "interval";

        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_AMOUNT, AbstractCategory.COLUMN_NAME_CATEGORY_NAME, COLUMN_NAME_DATE,
            COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};

        public static final String[] PROJECTION_WITH_CATEGORY_ID = new String[]{TABLE_NAME + DOT + _ID, AbstractCategory.TABLE_NAME + DOT + _ID,
            COLUMN_NAME_AMOUNT, AbstractCategory.COLUMN_NAME_CATEGORY_NAME, COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};

        public static final int[] TO_VIEWS = {R.id.row_id, R.id.row_amount, R.id.row_category, R.id.row_date, R.id.row_note, R.id.row_interval};

        public static final String STATEMENT_INNER_JOINED_CATEGORY = TABLE_NAME + " LEFT JOIN " + AbstractCategory.TABLE_NAME + " ON " + TABLE_NAME
                + DOT + COLUMN_NAME_CATEGORY + "=" + AbstractCategory.TABLE_NAME + DOT + AbstractCategory._ID;

        public static final String EXPENSE_SELECTION = OPEN_PARENTHESIS + COLUMN_NAME_IS_INCOME + EQUALS_EXPENSE + CLOSE_PARENTHESIS;
        public static final String SELECTION_BY_ID = OPEN_PARENTHESIS + TABLE_NAME + DOT + COLUMN_NAME_CATEGORY + " = " + AbstractCategory.TABLE_NAME
                + DOT + AbstractCategory._ID + AND + TABLE_NAME + DOT + _ID + " = ?" + CLOSE_PARENTHESIS;
        public static final String INCOME_SELECTION = OPEN_PARENTHESIS + COLUMN_NAME_IS_INCOME + EQUALS_INCOME + CLOSE_PARENTHESIS;
        public static final String RECURRING_INCOME_SELECTION = OPEN_PARENTHESIS + COLUMN_NAME_IS_INCOME + EQUALS_INCOME + AND + COLUMN_NAME_INTERVAL
                + " != 'none'" + CLOSE_PARENTHESIS;

        static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPEN_PARENTHESIS + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP + COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP + COLUMN_NAME_IS_INCOME + INTEGER_TYPE
                + COMMA_SEP + COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_INTERVAL + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_NOTE + TEXT_TYPE
                + COMMA_SEP + "FOREIGN KEY" + OPEN_PARENTHESIS + COLUMN_NAME_CATEGORY + CLOSE_PARENTHESIS + "REFERENCES "
                + AbstractCategory.TABLE_NAME + OPEN_PARENTHESIS + AbstractCategory._ID + CLOSE_PARENTHESIS + CLOSE_PARENTHESIS;

        static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        private AbstractStatement() {
        }
    }

}
