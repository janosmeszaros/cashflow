package com.cashflow.statement.database;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.CATEGORY_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_ID_ALIAS;
import static com.cashflow.statement.database.StatementType.Expense;
import static com.cashflow.statement.database.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.exceptions.IllegalStatementIdException;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class StatementPersistenceServiceTest {

    private static final String ZERO_AMOUNT = "0";
    private static final String ID_STR = "0";
    private static final String WRONG_AMOUNT_FORMAT = "hello";
    private static final String AMOUNT_STR = "1234";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "cat";
    private static final String NOTE = "note";
    private static final String DATE_STR = "2012.01.01";
    private static final String EMPTY_STR = "";
    private static final String INTERVAL_STR = "daily";
    private static final String ILLEGAL_ID = "3";
    private StatementPersistenceService underTest;
    @Mock
    private StatementDao dao;
    @Mock
    private Cursor expenseCursor;
    @Mock
    private Cursor incomeCursor;
    @Mock
    private Cursor byidCursor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(dao.getExpenses()).thenReturn(expenseCursor);
        when(dao.getIncomes()).thenReturn(incomeCursor);
        when(dao.getStatementById(anyString())).thenReturn(byidCursor);

        underTest = new StatementPersistenceService(dao);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new StatementPersistenceService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenAmountStrIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(EMPTY_STR, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenDateIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(AMOUNT_STR, EMPTY_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(EMPTY_STR).setType(null)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenAmountIsNotANumberStringIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenCategoryIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense).setCategory(null).build();

        underTest.saveStatement(statement);
    }

    @Test
    public void testSaveStatementWhenAmountIsZeroStringThenShouldReturnFalse() {
        Statement statement = new Statement.Builder(ZERO_AMOUNT, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        boolean result = underTest.saveStatement(statement);

        assertThat(result, equalTo(false));
    }

    @Test
    public void testSaveStatementWhenEveryParameterIsFineThenShouldCallDaosSaveMethodAndReturnTrue() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        boolean result = underTest.saveStatement(statement);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).save(argument.capture());

        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(AMOUNT_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY), equalTo(CATEGORY_ID));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE), equalTo(DATE_STR));
        assertThat((Integer) argument.getValue().get(COLUMN_NAME_IS_INCOME), equalTo(0));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(NOTE));

        assertThat(result, equalTo(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementWhenTypeIsNullThenShouldThrowException() {
        underTest.getStatement(null);
    }

    @Test
    public void testGetStatementWhenTypeIsExpenseThenShouldReturnCursorWithExpenses() {
        Cursor statement = underTest.getStatement(Expense);

        verify(dao).getExpenses();
        assertThat(statement, equalTo(expenseCursor));
    }

    @Test
    public void testGetStatementWhenTypeIsIncomesThenShouldReturnCursorWithIncomes() {
        Cursor statement = underTest.getStatement(Income);

        verify(dao).getIncomes();
        assertThat(statement, equalTo(incomeCursor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountStrIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(EMPTY_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenDateIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(AMOUNT_STR, EMPTY_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(null).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountIsNotANumberThenShouldThrowException() {
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenIdStrIsEmptyThenShouldThrowException() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(EMPTY_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test
    public void testUpdateStatementWhenSomethingWentWrongOnSaveingThenShouldReturnFalse() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();
        when(dao.update((ContentValues) anyObject(), eq(ID_STR))).thenReturn(false);

        boolean result = underTest.updateStatement(statement);

        assertThat(result, equalTo(false));
    }

    @Test
    public void testUpdateStatementWhenEveryParameterIsFineThenShouldCallDaosUpdateMethodAndReturnTrue() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).setRecurringInterval(RecurringInterval.monthly).build();
        when(dao.update((ContentValues) anyObject(), eq(ID_STR))).thenReturn(true);

        boolean result = underTest.updateStatement(statement);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).update(argument.capture(), eq(ID_STR));

        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(AMOUNT_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY), equalTo(CATEGORY_ID));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE), equalTo(DATE_STR));
        assertThat((Integer) argument.getValue().get(COLUMN_NAME_IS_INCOME), equalTo(0));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(NOTE));
        assertThat((String) argument.getValue().get(COLUMN_NAME_INTERVAL), equalTo(RecurringInterval.monthly.toString()));

        assertThat(result, equalTo(true));
    }

    @Test
    public void testGetRecurringIncomesWhenRecurringIncomesNumberIsZeroThenShouldReturnEmptyList() {
        when(dao.getRecurringIncomes()).thenReturn(incomeCursor);
        when(incomeCursor.moveToNext()).thenReturn(false);

        List<Statement> result = underTest.getRecurringIncomes();

        assertThat(result, empty());
    }

    @Test
    public void testGetRecurringIncomesWhenRecurringIncomesIsntEmptyThenShouldReturnCorrectNumberOfStatements() {
        setupCursorMock(incomeCursor);
        when(dao.getRecurringIncomes()).thenReturn(incomeCursor);
        when(incomeCursor.moveToNext()).thenReturn(true, true, false);

        List<Statement> result = underTest.getRecurringIncomes();

        assertThat(result, hasSize(equalTo(2)));
    }

    @Test
    public void testGetRecurringIncomesWhenRecurringIncomesIsntEmptyThenShouldReturnListOfStatements() {
        setupCursorMock(incomeCursor);
        when(dao.getRecurringIncomes()).thenReturn(incomeCursor);
        when(incomeCursor.moveToNext()).thenReturn(true, true, false);
        Statement statement = createStatement();

        List<Statement> result = underTest.getRecurringIncomes();

        assertThat(result, contains(statement, statement));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementByIdWhenIdIsEmptyThenShouldThrowException() {
        underTest.getStatementById("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementByIdWhenIdIsNullThenShouldThrowException() {
        underTest.getStatementById(null);
    }

    @Test(expected = IllegalStatementIdException.class)
    public void testGetStatementByIdWhenStatementCantFindThenShouldThrowIllegalStatementIdException() {
        when(byidCursor.moveToNext()).thenReturn(false);

        underTest.getStatementById(ILLEGAL_ID);
    }

    @Test
    public void testGetStatementByIdWhenStatementCanFindThenShouldReturnThatStatement() {
        when(byidCursor.moveToNext()).thenReturn(true);
        setupCursorMock(byidCursor);
        Statement statement = createStatement();

        Statement result = underTest.getStatementById(ILLEGAL_ID);

        assertThat(result, equalTo(statement));
    }

    private void setupCursorMock(Cursor mock) {
        when(mock.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(mock.getColumnIndexOrThrow(COLUMN_NAME_DATE)).thenReturn(1);
        when(mock.getColumnIndexOrThrow(COLUMN_NAME_INTERVAL)).thenReturn(2);
        when(mock.getColumnIndexOrThrow(COLUMN_NAME_NOTE)).thenReturn(3);
        when(mock.getColumnIndexOrThrow(STATEMENT_ID_ALIAS)).thenReturn(4);
        when(mock.getColumnIndexOrThrow(CATEGORY_ID_ALIAS)).thenReturn(5);
        when(mock.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME)).thenReturn(6);
        when(mock.getColumnIndexOrThrow(COLUMN_NAME_IS_INCOME)).thenReturn(7);

        when(mock.getString(0)).thenReturn(AMOUNT_STR);
        when(mock.getString(1)).thenReturn(DATE_STR);
        when(mock.getString(2)).thenReturn(INTERVAL_STR);
        when(mock.getString(3)).thenReturn(NOTE);
        when(mock.getString(4)).thenReturn(ID_STR);
        when(mock.getString(5)).thenReturn(CATEGORY_ID);
        when(mock.getString(6)).thenReturn(CATEGORY_NAME);
        when(mock.getInt(7)).thenReturn(1);
    }

    private Statement createStatement() {
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setRecurringInterval(RecurringInterval.valueOf(INTERVAL_STR))
                .setType(StatementType.Income).setNote(NOTE).setId(ID_STR).setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();
        return statement;
    }
}