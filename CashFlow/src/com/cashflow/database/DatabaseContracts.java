package com.cashflow.database;

import android.provider.BaseColumns;

/**
 * Database tables.
 * @author Kornel_Refi
 */
public abstract class DatabaseContracts {
    public static final String DATABASE_NAME = "CashFlow.db";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /**
     * Category table.
     * @author Kornel_Refi
     */
    public abstract static class AbstractCategory implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY_NAME = "name";
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_CATEGORY_NAME};

        static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DatabaseContracts.AbstractCategory.TABLE_NAME + " ("
                + DatabaseContracts.AbstractCategory._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME + REAL_TYPE + " )";

        static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContracts.AbstractCategory.TABLE_NAME;

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
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_CATEGORY, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};

        public static final String EXPENSE_SELECTION = "(" + COLUMN_NAME_IS_INCOME + " == 0)";
        public static final String INCOME_SELECTION = "(" + COLUMN_NAME_IS_INCOME + " == 1)";

        static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DatabaseContracts.AbstractStatement.TABLE_NAME + " ("
                + DatabaseContracts.AbstractStatement._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY + " INTEGER " + COMMA_SEP
                + DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP
                + DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME + INTEGER_TYPE + COMMA_SEP
                + DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP + DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE
                + TEXT_TYPE + COMMA_SEP + "FOREIGN KEY(" + DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY + ") " + "REFERENCES "
                + DatabaseContracts.AbstractCategory.TABLE_NAME + "(" + DatabaseContracts.AbstractCategory._ID + ")" + " )";

        static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContracts.AbstractStatement.TABLE_NAME;

        private AbstractStatement() {
        }
    }

}
