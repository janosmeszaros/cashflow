package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY;
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

import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.domain.Category;
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
    private static final int INCOME_TYPE = 1;
    private static final Logger LOG = LoggerFactory.getLogger(StatementPersistenceService.class);
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private final StatementDao dao;

    /**
     * Default constructor which gets a DAO.
     * @param dao
     *            {@link StatementDao} to use to save data. Can't be <code>null</code>.
     * @throws IllegalArgumentException
     *             when DAO is <code>null</code>.
     */
    @Inject
    public StatementPersistenceService(StatementDao dao) {
        validateObjectsNotNull(dao);
        this.dao = dao;
    }

    /**
     * Creates the statement from data and then saves it to database.
     * @param statement statement's data.
     * @return <code>true</code> if saving was successful and the amount wasn't zero, <code>false</code> otherwise.
     */
    public boolean saveStatement(Statement statement) {
        validateStatement(statement);

        boolean result = false;
        BigDecimal amount = parseAmount(statement.getAmount());

        if (checkIfNotZero(amount)) {
            ContentValues values = createContentValue(amount, statement);
            dao.save(values);
            result = true;
        }
        return result;
    }

    private void validateStatement(Statement statement) {
        validateObjectsNotNull(statement.getType(), statement.getCategory());
        validateStringsNotEmpty(statement.getAmount(), statement.getDate(), statement.getId());
    }

    /**
     * Get all statement from a statement type.
     * @param type
     *            specify the statement type which has to be returned.
     * @return a {@link Cursor} which contains the values.
     */
    public Cursor getStatement(StatementType type) {
        validateObjectsNotNull(type);

        Cursor result = null;
        if (type.isIncome()) {
            result = dao.getIncomes();
        } else {
            result = dao.getExpenses();
        }

        return result;
    }

    /**
     * Returns recurring incomes.
     * @return cursor for the recurring incomes.
     */
    public Cursor getRecurringIncomes() {
        return dao.getRecurringIncomes();

    }

    /**
     * Updates statement with the specified id.
     * @param statement statement which is hold the data.
     * @return <code>true</code> if successful, otherwise <code>false</code>.
     */
    public boolean updateStatement(Statement statement) {
        validateStatement(statement);

        boolean result = false;
        BigDecimal amount = parseAmount(statement.getAmount());

        ContentValues value = createContentValue(amount, statement);
        if (dao.update(value, statement.getId())) {
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
        values.put(COLUMN_NAME_CATEGORY, statement.getCategory().getId());
        values.put(COLUMN_NAME_DATE, statement.getDate());
        values.put(COLUMN_NAME_IS_INCOME, statement.getType().isIncome() ? TRUE : FALSE);
        values.put(COLUMN_NAME_NOTE, statement.getNote());
        values.put(COLUMN_NAME_INTERVAL, statement.getRecurringInterval().toString());

        LOG.debug("Content created: " + values);

        return values;
    }

    private void validateObjectsNotNull(Object... objects) {
        for (Object object : objects) {
            Validate.notNull(object);
        }
    }

    private void validateStringsNotEmpty(String... params) {
        for (String string : params) {
            Validate.notEmpty(string);
        }
    }

    /**
     * Get {@link Statement} by id.
     * @param id of {@link Statement}
     * @return {@link Statement} or <code>null</code> when the specified id doesn't exist
     */
    public Statement getStatementById(String id) {
        validateStringsNotEmpty(id);
        Cursor cursor = dao.getStatementById(id);
        Statement statement = null;

        if (cursor.moveToNext()) {
            statement = buildStatement(id, cursor);
        }

        return statement;
    }

    private Statement buildStatement(String id, Cursor cursor) {
        Statement statement;
        int categoryIdIndex = cursor.getColumnIndexOrThrow("categoryId");
        int categoryNameIndex = cursor.getColumnIndex(AbstractCategory.COLUMN_NAME_CATEGORY_NAME);
        int dateIndex = cursor.getColumnIndex(COLUMN_NAME_DATE);
        int amountIndex = cursor.getColumnIndex(COLUMN_NAME_AMOUNT);
        int intervalIndex = cursor.getColumnIndex(COLUMN_NAME_INTERVAL);
        int noteIndex = cursor.getColumnIndex(COLUMN_NAME_NOTE);
        int typeIndex = cursor.getColumnIndex(COLUMN_NAME_IS_INCOME);

        String amount = cursor.getString(amountIndex);
        String date = cursor.getString(dateIndex);
        String note = cursor.getString(noteIndex);
        RecurringInterval interval = RecurringInterval.valueOf(cursor.getString(intervalIndex));
        StatementType type = INCOME_TYPE == cursor.getInt(typeIndex) ? StatementType.Income : StatementType.Expense;

        String categoryId = cursor.getString(categoryIdIndex);
        String categoryName = cursor.getString(categoryNameIndex);

        Category category = new Category(categoryId, categoryName);
        statement = new Statement.Builder(amount, date).setId(id).setNote(note).setRecurringInterval(interval).setType(type).setCategory(category)
                .build();
        return statement;
    }
}
