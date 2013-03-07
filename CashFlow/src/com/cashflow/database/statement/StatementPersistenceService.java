package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

import java.math.BigDecimal;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create statement for dao.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class StatementPersistenceService {
    private static final Logger LOG = LoggerFactory.getLogger(StatementPersistenceService.class);
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private final StatementDao dao;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param dao
     *            {@link StatementDao} to use to save data. Can't be null.
     * @throws IllegalArgumentException
     *             when dao is null.
     */
    @Inject
    public StatementPersistenceService(StatementDao dao) {
        validateInput(dao);
        this.dao = dao;
    }

    /**
     * Creates the statement from data and then saves it to database.
     * @param amountStr
     *            amount of the statement.
     * @param date
     *            Date of the statement.
     * @param note
     *            Note for the statement.
     * @param type
     *            Statement's type
     * @return true if saving was successful and the amount wasn't zero, false otherwise.
     */
    public boolean saveStatement(String amountStr, String date, String note, StatementType type) {
        validateInput(type, amountStr, date);

        boolean result = false;
        BigDecimal amount = parseAmount(amountStr);

        if (checkIfNotZero(amount)) {
            ContentValues values = createContentValue(amount, date, note, type);
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
        validateInput(type);

        Cursor result = null;
        if (isIncome(type)) {
            result = dao.getIncomes();
        } else {
            result = dao.getExpenses();
        }

        return result;
    }

    /**
     * Updates statement with the specified id.
     * @param id
     *            id.
     * @param amountStr
     *            new amount for the statement.
     * @param date
     *            new date for the statement.
     * @param note
     *            new note for the statement.
     * @param type
     *            type of the statement.
     * @return true if successful.
     */
    public boolean updateStatement(String id, String amountStr, String date, String note, StatementType type) {
        validateInput(type, amountStr, date, id);

        boolean result = true;
        BigDecimal amount = parseAmount(amountStr);

        ContentValues value = createContentValue(amount, date, note, type);
        dao.update(value, id);

        return result;
    }

    private boolean isIncome(StatementType type) {
        return type.equals(StatementType.Income);
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

        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return amount;
    }

    private ContentValues createContentValue(BigDecimal amount, String date, String note, StatementType type) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_AMOUNT, amount.toString());
        values.put(COLUMN_NAME_DATE, date);
        values.put(COLUMN_NAME_IS_INCOME, isIncome(type) ? TRUE : FALSE);
        values.put(COLUMN_NAME_NOTE, note);

        LOG.debug("Content created: " + values);

        return values;
    }

    private void validateInput(Object obj, String... params) {
        for (String string : params) {
            Validate.notEmpty(string);
        }
        Validate.notNull(obj);
    }
}
