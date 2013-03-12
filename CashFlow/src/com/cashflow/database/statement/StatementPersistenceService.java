package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

import java.math.BigDecimal;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.domain.Statement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create statement for DAO.
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
     *             when DAO is null.
     */
    @Inject
    public StatementPersistenceService(StatementDao dao) {
        validateInput(dao);
        this.dao = dao;
    }

    /**
     * Creates the statement from data and then saves it to database.
     * @param statement statement's data.
     * @return <code>true</code> if saving was successful and the amount wasn't zero, <code>false</code> otherwise.
     */
    public boolean saveStatement(Statement statement) {
        validateInput(statement.getType(), statement.getAmount(), statement.getDate());

        boolean result = false;
        BigDecimal amount = parseAmount(statement.getAmount());

        if (checkIfNotZero(amount)) {
            ContentValues values = createContentValue(amount, statement);
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
        if (type.isIncome()) {
            result = dao.getIncomes();
        } else {
            result = dao.getExpenses();
        }

        return result;
    }

    /**
     * Updates statement with the specified id.
     * @param statement statement which is hold the data.
     * @return true if successful.
     */
    public boolean updateStatement(Statement statement) {
        validateInput(statement.getType(), statement.getAmount(), statement.getDate(), statement.getId());

        boolean result = true;
        BigDecimal amount = parseAmount(statement.getAmount());

        ContentValues value = createContentValue(amount, statement);
        dao.update(value, statement.getId());

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

        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return amount;
    }

    private ContentValues createContentValue(BigDecimal amount, Statement statement) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_AMOUNT, amount.toString());
        values.put(COLUMN_NAME_DATE, statement.getDate());
        values.put(COLUMN_NAME_IS_INCOME, statement.getType().isIncome() ? TRUE : FALSE);
        values.put(COLUMN_NAME_NOTE, statement.getNote());
        values.put(COLUMN_NAME_INTERVAL, statement.getRecurringInterval().toString());

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
