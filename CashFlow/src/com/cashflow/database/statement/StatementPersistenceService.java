package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.CATEGORY_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_ID_ALIAS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.exceptions.IllegalStatementIdException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create statement for DAO.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class StatementPersistenceService {
    private static final String INCOME_TYPE = "1";
    private static final Logger LOG = LoggerFactory.getLogger(StatementPersistenceService.class);
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private final StatementDao dao;

    /**
     * Default constructor which gets a DAO.
     * @param dao {@link StatementDao} to use to save data. Can't be <code>null</code>.
     * @throws IllegalArgumentException when DAO is <code>null</code>.
     */
    @Inject
    public StatementPersistenceService(StatementDao dao) {
        validateObjectsNotNull(dao);
        this.dao = dao;
    }

    /**
     * Creates the statement from data and then saves it to database.
     * @param statement statement's data.  It's amount, date, type, and recurring interval have to be setted.
     * @throws IllegalArgumentException when one of the above is empty or null.
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

    /**
     * Get all statement from a statement type.
     * @param type specify the statement type which has to be returned. Can not be null
     * @throws IllegalArgumentException when parameter is null.
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
     * @return list of statements which are recurring incomes.
     */
    public List<Statement> getRecurringIncomes() {
        List<Statement> list = new ArrayList<Statement>();
        Cursor cursor = dao.getRecurringIncomes();

        while (cursor.moveToNext()) {
            list.add(buildStatement(cursor));
        }

        return list;
    }

    /**
     * Updates statement with the specified id.
     * @param statement statement which is hold the data. It's id, amount, date, type, and recurring interval have to be setted.
     * @throws IllegalArgumentException when one of them above is not setted.
     * @return <code>true</code> if successful, otherwise <code>false</code>.
     */
    public boolean updateStatement(Statement statement) {
        validateUpdateStatement(statement);

        boolean result = false;
        BigDecimal amount = parseAmount(statement.getAmount());

        ContentValues value = createContentValue(amount, statement);
        if (dao.update(value, statement.getId())) {
            result = true;
        }

        return result;
    }

    /**
     * Get {@link Statement} by id. 
     * @param id of {@link Statement} can't be empty or null.
     * @throws IllegalArgumentException when id is empty or null.
     * @throws IllegalStatementIdException when statement does not exist for the id.
     * @return {@link Statement} for the specified id.
     */
    public Statement getStatementById(String id) {
        validateStringsNotEmpty(id);
        Statement statement;
        Cursor cursor = dao.getStatementById(id);

        if (cursor.moveToNext()) {
            statement = buildStatement(cursor);
        } else {
            throw new IllegalStatementIdException(id);
        }

        return statement;
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

    private Map<String, String> getColumnValues(Cursor cursor) {
        Map<String, Integer> columnNumbers = getColumnNumbers(cursor);
        Map<String, String> columnValues = new HashMap<String, String>();

        columnValues.put(COLUMN_NAME_INTERVAL, cursor.getString(columnNumbers.get(COLUMN_NAME_INTERVAL)));
        columnValues.put(COLUMN_NAME_AMOUNT, cursor.getString(columnNumbers.get(COLUMN_NAME_AMOUNT)));
        columnValues.put(COLUMN_NAME_DATE, cursor.getString(columnNumbers.get(COLUMN_NAME_DATE)));
        columnValues.put(COLUMN_NAME_NOTE, cursor.getString(columnNumbers.get(COLUMN_NAME_NOTE)));
        columnValues.put(STATEMENT_ID_ALIAS, cursor.getString(columnNumbers.get(STATEMENT_ID_ALIAS)));
        columnValues.put(CATEGORY_ID_ALIAS, cursor.getString(columnNumbers.get(CATEGORY_ID_ALIAS)));
        columnValues.put(AbstractCategory.COLUMN_NAME_CATEGORY_NAME, cursor.getString(columnNumbers.get(AbstractCategory.COLUMN_NAME_CATEGORY_NAME)));
        columnValues.put(COLUMN_NAME_IS_INCOME, ((Integer) cursor.getInt(columnNumbers.get(COLUMN_NAME_IS_INCOME))).toString());

        return columnValues;
    }

    private Map<String, Integer> getColumnNumbers(Cursor cursor) {
        Map<String, Integer> columnNumbers = new HashMap<String, Integer>();

        columnNumbers.put(COLUMN_NAME_DATE, cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE));
        columnNumbers.put(COLUMN_NAME_AMOUNT, cursor.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT));
        columnNumbers.put(COLUMN_NAME_INTERVAL, cursor.getColumnIndexOrThrow(COLUMN_NAME_INTERVAL));
        columnNumbers.put(COLUMN_NAME_NOTE, cursor.getColumnIndexOrThrow(COLUMN_NAME_NOTE));
        columnNumbers.put(STATEMENT_ID_ALIAS, cursor.getColumnIndexOrThrow(STATEMENT_ID_ALIAS));
        columnNumbers.put(CATEGORY_ID_ALIAS, cursor.getColumnIndexOrThrow(CATEGORY_ID_ALIAS));
        columnNumbers.put(AbstractCategory.COLUMN_NAME_CATEGORY_NAME, cursor.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME));
        columnNumbers.put(COLUMN_NAME_IS_INCOME, cursor.getColumnIndexOrThrow(COLUMN_NAME_IS_INCOME));

        return columnNumbers;
    }

    private Statement buildStatement(Cursor cursor) {
        Map<String, String> values = getColumnValues(cursor);

        Category category = new Category(values.get(CATEGORY_ID_ALIAS), values.get(AbstractCategory.COLUMN_NAME_CATEGORY_NAME));
        StatementType type = INCOME_TYPE.equals(values.get(COLUMN_NAME_IS_INCOME)) ? StatementType.Income : StatementType.Expense;

        return new Statement.Builder(values.get(COLUMN_NAME_AMOUNT), values.get(COLUMN_NAME_DATE)).setId(values.get(STATEMENT_ID_ALIAS))
                .setNote(values.get(COLUMN_NAME_NOTE)).setRecurringInterval(RecurringInterval.valueOf(values.get(COLUMN_NAME_INTERVAL)))
                .setType(type).setCategory(category).build();
    }

    private void validateStatement(Statement statement) {
        validateObjectsNotNull(statement.getType(), statement.getCategory());
        validateStringsNotEmpty(statement.getAmount(), statement.getDate());
    }

    private void validateUpdateStatement(Statement statement) {
        validateObjectsNotNull(statement.getType(), statement.getCategory());
        validateStringsNotEmpty(statement.getAmount(), statement.getDate(), statement.getId());
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
}
