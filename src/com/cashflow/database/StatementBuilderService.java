package com.cashflow.database;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.ContentValues;

/**
 * Class to create statement for dao.
 * @author Kornel_Refi
 */
public class StatementBuilderService {
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private Activity activity;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param activity
     *            Required for DbHelper.
     */
    public StatementBuilderService(Activity activity) {
        this.activity = activity;
    }

    /**
     * Creates the statement from data and then saves it to db.
     * @param amountStr
     *            amount of the statement.
     * @param date
     *            Date of the statement.
     * @param note
     *            Note for the statement.
     * @param isIncome
     *            True if the statement is income, false otherwise
     * @return true if saving was successful, false otherwise.
     */
    public boolean saveStatement(String amountStr, String date, String note, boolean isIncome) {
        boolean result = false;
        BigDecimal amount = parseAmount(amountStr);

        if (checkIfNotZero(amount)) {
            ContentValues values = createContentValue(amount, date, note, isIncome);

            Dao dao = new Dao(activity);
            dao.save(values);

            result = true;
        }
        return result;
    }

    private boolean checkIfNotZero(BigDecimal amount) {
        boolean result = true;
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            result = false;
        }
        return result;
    }

    private BigDecimal parseAmount(String amountStr) {
        BigDecimal amount = BigDecimal.ZERO;

        if (amountStr.length() != 0) {
            amount = new BigDecimal(amountStr);
        }

        return amount;
    }

    private ContentValues createContentValue(BigDecimal amount, String date, String note, boolean isIncome) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.Statement.COLUMN_NAME_AMOUNT, amount.toString());
        values.put(DatabaseContracts.Statement.COLUMN_NAME_DATE, date);
        values.put(DatabaseContracts.Statement.COLUMN_NAME_IS_INCOME, isIncome ? TRUE : FALSE);
        values.put(DatabaseContracts.Statement.COLUMN_NAME_NOTE, note);

        return values;
    }
}
