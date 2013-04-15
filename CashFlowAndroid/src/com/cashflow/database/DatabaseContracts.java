package com.cashflow.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import android.provider.BaseColumns;

import com.cashflow.R;
import com.cashflow.database.parentdao.ColumnPredicate;
import com.cashflow.database.parentdao.FieldToStringTransformer;
import com.cashflow.database.parentdao.Tables;

/**
 * Database tables.
 * @author Kornel_Refi
 */
public abstract class DatabaseContracts {
    public static final String DATABASE_NAME = "CashFlow.db";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
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
    private static final String AS = " AS ";
    private static final String DOT = ".";
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";

    @SuppressWarnings("unchecked")
    private static Set<String> collectColumnNames(final Class<? extends Tables> clazz) {
        final Field[] fields = clazz.getFields();
        final List<Field> listOfFields = new ArrayList<Field>(Arrays.asList(fields));
        CollectionUtils.filter(listOfFields, new ColumnPredicate());
        final Collection<String> stringListOfFields = CollectionUtils.collect(listOfFields, new FieldToStringTransformer());
        return new TreeSet<String>(stringListOfFields);
    }

    /**
     * Category table.
     * @author Kornel_Refi
     */
    public static final class AbstractCategory implements BaseColumns, Tables {
        public static final String NULLABLE = null;
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY_NAME = "name";
        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_CATEGORY_NAME};

        public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPEN_PARENTHESIS + _ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP
                + COLUMN_NAME_CATEGORY_NAME + REAL_TYPE + CLOSE_PARENTHESIS;

        public static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }

        @Override
        public String[] getProjection() {
            return PROJECTION;
        }

        @Override
        public Set<String> getColumns() {
            return collectColumnNames(this.getClass());
        }
    }

    /**
      * Statement table.
      * @author Kornel_Refi
      */
    public static final class AbstractStatement implements BaseColumns, Tables {
        public static final String NULLABLE = null;
        public static final String STATEMENT_ID_ALIAS = "statementId";
        public static final String CATEGORY_ID_ALIAS = "categoryId";
        public static final String TABLE_NAME = "statement";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_IS_INCOME = "is_income";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_INTERVAL = "interval";

        public static final String[] PROJECTION = new String[]{TABLE_NAME + DOT + _ID, COLUMN_NAME_AMOUNT,
            AbstractCategory.COLUMN_NAME_CATEGORY_NAME, COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};

        public static final String[] PROJECTION_WITH_ALIAS = new String[]{TABLE_NAME + DOT + _ID + AS + STATEMENT_ID_ALIAS,
            AbstractCategory.TABLE_NAME + DOT + _ID + AS + CATEGORY_ID_ALIAS, COLUMN_NAME_AMOUNT, AbstractCategory.COLUMN_NAME_CATEGORY_NAME,
            COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL, COLUMN_NAME_IS_INCOME};

        public static final int[] TO_VIEWS = {R.id.row_id, R.id.row_amount, R.id.row_category, R.id.row_date, R.id.row_note, R.id.row_interval};

        public static final String STATEMENT_INNER_JOINED_CATEGORY = TABLE_NAME + " LEFT JOIN " + AbstractCategory.TABLE_NAME + " ON " + TABLE_NAME
                + DOT + COLUMN_NAME_CATEGORY + "=" + AbstractCategory.TABLE_NAME + DOT + AbstractCategory._ID;

        public static final String EXPENSE_SELECTION = OPEN_PARENTHESIS + COLUMN_NAME_IS_INCOME + EQUALS_EXPENSE + CLOSE_PARENTHESIS;
        public static final String SELECTION_BY_ID = OPEN_PARENTHESIS + TABLE_NAME + DOT + COLUMN_NAME_CATEGORY + " = " + AbstractCategory.TABLE_NAME
                + DOT + AbstractCategory._ID + AND + TABLE_NAME + DOT + _ID + " = ?" + CLOSE_PARENTHESIS;
        public static final String INCOME_SELECTION = COLUMN_NAME_IS_INCOME + EQUALS_INCOME + AND + COLUMN_NAME_INTERVAL + " = 'none'";
        public static final String RECURRING_INCOME_SELECTION = OPEN_PARENTHESIS + COLUMN_NAME_IS_INCOME + EQUALS_INCOME + AND + COLUMN_NAME_INTERVAL
                + " != 'none'" + CLOSE_PARENTHESIS;

        public static final String SELECT_STATEMENT_BY_ID = SELECT + TABLE_NAME + DOT + _ID + AS + STATEMENT_ID_ALIAS + COMMA_SEP
                + AbstractCategory.TABLE_NAME + DOT + _ID + AS + CATEGORY_ID_ALIAS + COMMA_SEP + COLUMN_NAME_AMOUNT + COMMA_SEP
                + AbstractCategory.COLUMN_NAME_CATEGORY_NAME + COMMA_SEP + COLUMN_NAME_DATE + COMMA_SEP + COLUMN_NAME_NOTE + COMMA_SEP
                + COLUMN_NAME_INTERVAL + COMMA_SEP + COLUMN_NAME_IS_INCOME + FROM + AbstractCategory.TABLE_NAME + COMMA_SEP
                + AbstractStatement.TABLE_NAME + WHERE + SELECTION_BY_ID;

        public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPEN_PARENTHESIS + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP + COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP + COLUMN_NAME_IS_INCOME + INTEGER_TYPE
                + COMMA_SEP + COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_INTERVAL + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_NOTE + TEXT_TYPE
                + COMMA_SEP + "FOREIGN KEY" + OPEN_PARENTHESIS + COLUMN_NAME_CATEGORY + CLOSE_PARENTHESIS + "REFERENCES "
                + AbstractCategory.TABLE_NAME + OPEN_PARENTHESIS + AbstractCategory._ID + CLOSE_PARENTHESIS + CLOSE_PARENTHESIS;

        public static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }

        @Override
        public String[] getProjection() {
            return PROJECTION;
        }

        @Override
        public Set<String> getColumns() {
            return collectColumnNames(this.getClass());
        }
    }

    /**
     * Database Table for bills.
     * @author Janos_Gyula_Meszaros
     *
     */
    public static final class AbstractBill implements BaseColumns, Tables {
        public static final String NULLABLE = null;
        public static final String TABLE_NAME = "bill";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE_ADDED = "date";
        public static final String COLUMN_NAME_DATE_PAYED = "payedDate";
        public static final String COLUMN_NAME_DATE_DEADLINE = "deadlineDate";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_IS_PAYED = "payed";
        public static final String COLUMN_NAME_INTERVAL = "interval";

        public static final String[] PROJECTION = new String[]{_ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE_ADDED, COLUMN_NAME_DATE_PAYED,
            COLUMN_NAME_DATE_DEADLINE, COLUMN_NAME_NOTE, COLUMN_NAME_IS_PAYED};

        public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + TABLE_NAME + OPEN_PARENTHESIS + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_AMOUNT + INTEGER_TYPE + COMMA_SEP + COLUMN_NAME_DATE_ADDED + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_DATE_DEADLINE
                + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_DATE_PAYED + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP
                + COLUMN_NAME_IS_PAYED + INTEGER_TYPE + COMMA_SEP + COLUMN_NAME_NOTE + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_INTERVAL + TEXT_TYPE
                + CLOSE_PARENTHESIS;

        public static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }

        @Override
        public String[] getProjection() {
            return PROJECTION;
        }

        @Override
        public Set<String> getColumns() {
            return collectColumnNames(this.getClass());
        }
    }

}
