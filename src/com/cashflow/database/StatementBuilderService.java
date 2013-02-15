package com.cashflow.database;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.ContentValues;

public class StatementBuilderService {
    private static final int TRUE = 1;
    private Activity activity;

    public StatementBuilderService(Activity activity) {
        this.activity = activity;
    }

    public boolean saveStatement(String amountStr, String date, String note) {
        boolean result = false;
        BigDecimal amount = parseAmount(amountStr);

        if (checkIfNotZero(amount)) {
            ContentValues values = createContentValue(amount, date, note);

            Dao dao = new Dao(activity);
            dao.save(values);

            result = true;
        }
        return result;
    }

    private boolean checkIfNotZero(BigDecimal amount) {
        boolean result = true;
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
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

    private ContentValues createContentValue(BigDecimal amount, String date, String note) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.Statement.COLUMN_NAME_AMOUNT, amount.toString());
        values.put(DatabaseContracts.Statement.COLUMN_NAME_DATE, date);
        values.put(DatabaseContracts.Statement.COLUMN_NAME_IS_INCOME, TRUE);
        values.put(DatabaseContracts.Statement.COLUMN_NAME_NOTE, note);

        return values;
    }
}
