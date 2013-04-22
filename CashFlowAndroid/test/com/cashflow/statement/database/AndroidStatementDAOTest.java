package com.cashflow.statement.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.StatementDAO;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.cashflow.exceptions.IllegalStatementIdException;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AndroidStatementDAO} test class.
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class AndroidStatementDAOTest {
    private static final String EQUALS = " = ?";
    private static final String STATEMENT_ID = "1";
    private static final String ID_STR = "0";
    private static final String AMOUNT_STR = "1234";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "cat";
    private static final String NOTE = "note";
    private static final String DATE_STR = "2012.01.01";
    private static final String INTERVAL_STR = "daily";
    private final Statement expenseStatement = Statement.builder(AMOUNT_STR, DATE_STR).note(NOTE).type(StatementType.Expense)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).statementId(ID_STR).build();
    private final Statement incomeStatement = Statement.builder(AMOUNT_STR, DATE_STR).note(NOTE).type(StatementType.Income)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).statementId(ID_STR)
            .recurringInterval(RecurringInterval.valueOf(INTERVAL_STR)).build();

    private StatementDAO underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase database;
    @Mock
    private Cursor cursorMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(database);
        when(provider.getReadableDb()).thenReturn(database);

        underTest = new AndroidStatementDAO(provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new AndroidStatementDAO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenStatementIsIncomeThenShouldSaveToDatabase() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(1L);

        final boolean saved = underTest.save(incomeStatement);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(true));
    }

    @Test
    public void testSaveWhenStatementIsExpenseThenShouldSaveToDatabase() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(1L);

        final boolean saved = underTest.save(expenseStatement);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(true));
    }

    @Test
    public void testSaveWhenStatementIsOkButSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(-1L);

        final boolean saved = underTest.save(incomeStatement);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIsNullThenShouldThrowException() {
        underTest.update(null, "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenIdIsEmptyThenShouldThrowException() {
        underTest.update(incomeStatement, "");
    }

    @Test
    public void testUpdateWhenStatementIsIncomeThenShouldUpdateAndReturnTrue() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{incomeStatement.getStatementId()}))).thenReturn(1);

        final boolean updated = underTest.update(incomeStatement, incomeStatement.getStatementId());

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(true));
    }

    @Test
    public void testUpdateWhenStatementIsExpenseThenShouldUpdateAndReturnTrue() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{incomeStatement.getStatementId()}))).thenReturn(1);

        final boolean updated = underTest.update(incomeStatement, expenseStatement.getStatementId());

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(true));
    }

    @Test
    public void testUpdateWhenStatementIsExpenseButSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{incomeStatement.getStatementId()})))
                .thenReturn(-1);

        final boolean updated = underTest.update(incomeStatement, expenseStatement.getStatementId());

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(false));
    }

    @Test
    public void testGetAllStatementsWhenCalledThenReturnListOfStatement() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, null, null, null, null, null)).thenReturn(cursorMock);
        setupCursorMock(1);
        final List<Statement> list = new ArrayList<Statement>();
        list.add(incomeStatement);

        final List<Statement> statements = underTest.getAllStatements();

        verify(provider).getReadableDb();
        assertThat(list, equalTo(statements));
    }

    @Test
    public void testGetAllStatementsWithExpensesWhenCalledThenReturnListOfStatement() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, null, null, null, null, null)).thenReturn(cursorMock);
        setupCursorMock(0);
        final List<Statement> list = new ArrayList<Statement>();
        list.add(expenseStatement);

        final List<Statement> statements = underTest.getAllStatements();

        verify(provider).getReadableDb();
        assertThat(list, equalTo(statements));
    }

    @Test
    public void testGetExpensesWhenEverythingIsOkThenCallProperFunctionAndReturnList() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, EXPENSE_SELECTION, null, null, null, null))
                .thenReturn(cursorMock);
        setupCursorMock(0);
        final List<Statement> list = new ArrayList<Statement>();
        list.add(expenseStatement);

        final List<Statement> expenses = underTest.getExpenses();

        verify(provider).getReadableDb();
        verify(database).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, EXPENSE_SELECTION, null, null, null, null);
        assertThat(expenses, equalTo(list));
    }

    @Test
    public void testGetIncomesWhenEverythingIsOkThenCallProperFunctionAndReturnList() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, INCOME_SELECTION, null, null, null, null)).thenReturn(cursorMock);
        setupCursorMock(1);
        final List<Statement> list = new ArrayList<Statement>();
        list.add(incomeStatement);

        final List<Statement> incomes = underTest.getIncomes();

        verify(provider).getReadableDb();
        verify(database).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, INCOME_SELECTION, null, null, null, null);
        assertThat(incomes, equalTo(list));
    }

    @Test
    public void testGetRecurringIncomesWhenEverythingIsOkThenCallProperFunctionAndReturnList() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null)).thenReturn(
                cursorMock);
        setupCursorMock(1);
        final List<Statement> list = new ArrayList<Statement>();
        list.add(incomeStatement);

        final List<Statement> incomes = underTest.getRecurringIncomes();

        verify(provider).getReadableDb();
        verify(database).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
        assertThat(incomes, equalTo(list));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementByIdWhenParamIdIsEmptyThenShouldThrowException() {
        underTest.getStatementById("");
    }

    @Test
    public void testGetStatementByIdWhenParamIdIsOkThenShouldCallProperFunctionAndReturnStatement() {
        when(database.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{STATEMENT_ID})).thenReturn(cursorMock);
        setupCursorMock(1);

        final Statement statement = underTest.getStatementById(STATEMENT_ID);

        verify(provider).getReadableDb();
        verify(database).rawQuery(SELECT_STATEMENT_BY_ID, new String[]{STATEMENT_ID});
        assertThat(statement, equalTo(incomeStatement));
    }

    @Test(expected = IllegalStatementIdException.class)
    public void testGetStatementByIdWhenCursorHasNoStatementThenShouldThrowException() {
        when(cursorMock.getCount()).thenReturn(0);
        when(database.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{STATEMENT_ID})).thenReturn(cursorMock);

        underTest.getStatementById(STATEMENT_ID);
    }

    private void setupCursorMock(final int statementType) {
        when(cursorMock.moveToNext()).thenReturn(true, false);
        when(cursorMock.getCount()).thenReturn(1);

        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_DATE)).thenReturn(1);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_INTERVAL)).thenReturn(2);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_NOTE)).thenReturn(3);
        when(cursorMock.getColumnIndexOrThrow(STATEMENT_ID_ALIAS)).thenReturn(4);
        when(cursorMock.getColumnIndexOrThrow(AbstractCategory.CATEGORY_ID_ALIAS)).thenReturn(5);
        when(cursorMock.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME)).thenReturn(6);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_IS_INCOME)).thenReturn(7);

        when(cursorMock.getString(0)).thenReturn(AMOUNT_STR);
        when(cursorMock.getString(1)).thenReturn(DATE_STR);
        if (statementType == 1) {
            when(cursorMock.getString(2)).thenReturn(INTERVAL_STR);
        } else {
            when(cursorMock.getString(2)).thenReturn(RecurringInterval.none.toString());
        }
        when(cursorMock.getString(3)).thenReturn(NOTE);
        when(cursorMock.getString(4)).thenReturn(ID_STR);
        when(cursorMock.getString(5)).thenReturn(CATEGORY_ID);
        when(cursorMock.getString(6)).thenReturn(CATEGORY_NAME);
        when(cursorMock.getInt(7)).thenReturn(statementType);
    }

}
