package com.cashflow.statement.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.CATEGORY_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.dao.StatementDAO;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.cashflow.domain.StatementType;
import com.cashflow.exceptions.IllegalStatementIdException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DAO based on Android for {@link Statement}.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class AndroidStatementDAO implements StatementDAO {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidStatementDAO.class);

    private static final String EQUALS = " = ?";
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    private static final Integer INCOME_TYPE = 1;
    private final SQLiteDbProvider provider;

    /**
     * Default constructor which gets a Provider. Can't be <code>null</code>.
     * @throws IllegalArgumentException
     *             when Provider is <code>null</code>.
     * @param provider
     *            {@link SQLiteDbProvider} to get database.
     */
    @Inject
    public AndroidStatementDAO(final SQLiteDbProvider provider) {
        nullCheck(provider);
        this.provider = provider;
    }

    @Override
    public boolean save(final Statement statement) {
        nullCheck(statement);
        final ContentValues values = createContentValue(statement);
        return persistStatement(values);

    }

    private boolean persistStatement(final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final long newRowId = database.insert(TABLE_NAME, null, values);

        LOG.debug("New row created with row ID: " + newRowId);

        return isSuccessful(newRowId);
    }

    private boolean isSuccessful(final long newRowId) {
        return newRowId >= 0;
    }

    @Override
    public boolean update(final Statement statement, final String statementId) {
        nullCheck(statement);
        idCheck(statementId);

        final ContentValues values = createContentValue(statement);

        return updateStatement(statementId, values);
    }

    private boolean updateStatement(final String statementId, final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final int updatedRows = database.update(TABLE_NAME, values, _ID + EQUALS, new String[]{statementId});

        LOG.debug("Num of rows updated: " + updatedRows);
        return isUpdateSuccessed(updatedRows);
    }

    private boolean isUpdateSuccessed(final int update) {
        return update > 0;
    }

    /**
     * Returns all the {@link Statement}.
     * @return {@link List} of {@link Statement} which contains the data.
     */
    @Override
    public List<Statement> getAllStatements() {
        final Cursor query = queryAllStatements();

        return createStatements(query);
    }

    private Cursor queryAllStatements() {
        final SQLiteDatabase database = provider.getReadableDb();
        return database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, null, null, null, null, null);
    }

    private List<Statement> createStatements(final Cursor cursor) {
        final List<Statement> statements = new ArrayList<Statement>();

        while (cursor.moveToNext()) {
            final Statement statement = getStatementFromCursor(cursor);
            statements.add(statement);
        }

        return statements;
    }

    private Statement getStatementFromCursor(final Cursor cursor) {
        final int dateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
        final int amountIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT);
        final int intervalIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_INTERVAL);
        final int noteIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_NOTE);
        final int isIncomeIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_IS_INCOME);
        final int idIndex = cursor.getColumnIndexOrThrow(STATEMENT_ID_ALIAS);
        final int categoryIdIndex = cursor.getColumnIndexOrThrow(CATEGORY_ID_ALIAS);
        final int categoryIndex = cursor.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME);

        final Category category = Category.builder(cursor.getString(categoryIndex)).categoryId(cursor.getString(categoryIdIndex)).build();
        final StatementType type = INCOME_TYPE.equals(cursor.getInt(isIncomeIndex)) ? StatementType.Income : StatementType.Expense;

        final Builder builder = Statement.builder(cursor.getString(amountIndex), cursor.getString(dateIndex));
        builder.category(category);
        builder.statementId(cursor.getString(idIndex));
        builder.note(cursor.getString(noteIndex));
        builder.recurringInterval(RecurringInterval.valueOf(cursor.getString(intervalIndex)));
        builder.type(type);
        return builder.build();
    }

    @Override
    public List<Statement> getExpenses() {
        final Cursor cursor = queryExpenses();

        return createStatements(cursor);
    }

    private Cursor queryExpenses() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, EXPENSE_SELECTION, null, null, null, null);

    }

    @Override
    public List<Statement> getIncomes() {
        final Cursor cursor = queryIncomes();

        return createStatements(cursor);
    }

    private Cursor queryIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, INCOME_SELECTION, null, null, null, null);
    }

    @Override
    public List<Statement> getRecurringIncomes() {
        final Cursor cursor = queryRecurringIncomes();

        return createStatements(cursor);
    }

    private Cursor queryRecurringIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
    }

    @Override
    public Statement getStatementById(final String statementId) {
        idCheck(statementId);
        final Cursor cursor = queryStatementById(statementId);
        cursorHasStatement(statementId, cursor);
        return getOneStatement(cursor);
    }

    private void cursorHasStatement(final String statementId, final Cursor cursor) {
        if (cursor.getCount() > 0) {
            throw new IllegalStatementIdException(statementId);
        }
    }

    private Statement getOneStatement(final Cursor cursor) {
        cursor.moveToNext();
        return getStatementFromCursor(cursor);
    }

    private Cursor queryStatementById(final String statementId) {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{statementId});
    }

    private void idCheck(final String statementId) {
        Validate.notEmpty(statementId);
    }

    private void nullCheck(final Object object) {
        Validate.notNull(object);
    }

    private ContentValues createContentValue(final Statement statement) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_AMOUNT, statement.getAmount());
        values.put(COLUMN_NAME_CATEGORY, statement.getCategory().getId());
        values.put(COLUMN_NAME_DATE, statement.getDate());
        values.put(COLUMN_NAME_IS_INCOME, statement.getType().isIncome() ? TRUE : FALSE);
        values.put(COLUMN_NAME_NOTE, statement.getNote());
        values.put(COLUMN_NAME_INTERVAL, statement.getRecurringInterval().toString());

        LOG.debug("Content created: " + values);

        return values;
    }

}
