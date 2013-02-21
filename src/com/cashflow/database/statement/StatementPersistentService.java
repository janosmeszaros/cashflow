package com.cashflow.database.statement;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.database.DatabaseContracts;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create statement for dao.
 * @author Kornel_Refi
 */
@Singleton
public class StatementPersistentService {
    private static final Logger LOG = LoggerFactory.getLogger(StatementPersistentService.class);
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private final StatementDao dao;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param dao
     *            {@link StatementDao} to use to save data.
     */
    @Inject
    public StatementPersistentService(StatementDao dao) {
        this.dao = dao;
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
            dao.save(values);
            result = true;
        }
        return result;
    }

    /**
     * Get all statement from a statement type.
     * @param type
     *            specify the statement type which has to be returned.
     * @return a cursor which contains the values.
     */
    public Cursor getStatement(StatementType type) {
        Cursor result = null;
        if (isExpense(type)) {
            result = dao.getExpenses();
        } else if (isIncome(type)) {
            result = dao.getIncomes();
        }

        return result;
    }

    private boolean isIncome(StatementType type) {
        return type.equals(StatementType.Income);
    }

    private boolean isExpense(StatementType type) {
        return type.equals(StatementType.Expense);
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
        values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT, amount.toString());
        values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE, date);
        values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME, isIncome ? TRUE : FALSE);
        values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE, note);

        LOG.debug("Content created: " + values);

        return values;
    }
}
