package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.statement.StatementType.Expense;
import static com.cashflow.database.statement.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
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
    private StatementPersistenceService underTest;
    @Mock
    private StatementDao dao;
    @Mock
    private Cursor expenseCursor;
    @Mock
    private Cursor incomeCursor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(dao.getExpenses()).thenReturn(expenseCursor);
        when(dao.getIncomes()).thenReturn(incomeCursor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new StatementPersistenceService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenAmountStrIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(EMPTY_STR, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenDateIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, EMPTY_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(EMPTY_STR).setType(null)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenAmountIsNotANumberStringIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.saveStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenCategoryIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense).setCategory(null).build();

        underTest.saveStatement(statement);
    }

    @Test
    public void testSaveStatementWhenAmountIsZeroStringThenShouldReturnFalse() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(ZERO_AMOUNT, DATE_STR).setNote(NOTE).setType(Expense)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        boolean result = underTest.saveStatement(statement);

        assertThat(result, equalTo(false));
    }

    @Test
    public void testSaveStatementWhenEveryParameterIsFineThenShouldCallDaosSaveMethodAndReturnTrue() {
        underTest = new StatementPersistenceService(dao);
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
        underTest = new StatementPersistenceService(dao);

        underTest.getStatement(null);
    }

    @Test
    public void testGetStatementWhenTypeIsExpenseThenShouldReturnCursorWithExpenses() {
        underTest = new StatementPersistenceService(dao);

        Cursor statement = underTest.getStatement(Expense);

        verify(dao).getExpenses();
        assertThat(statement, equalTo(expenseCursor));
    }

    @Test
    public void testGetStatementWhenTypeIsIncomesThenShouldReturnCursorWithIncomes() {
        underTest = new StatementPersistenceService(dao);

        Cursor statement = underTest.getStatement(Income);

        verify(dao).getIncomes();
        assertThat(statement, equalTo(incomeCursor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountStrIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(EMPTY_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenDateIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, EMPTY_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(null).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountIsNotANumberStringIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(WRONG_AMOUNT_FORMAT, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenIdStrIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(EMPTY_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        underTest.updateStatement(statement);
    }

    @Test
    public void testUpdateStatementWhenEveryParameterIsFineThenShouldCallDaosUpdateMethodAndReturnTrue() {
        underTest = new StatementPersistenceService(dao);
        Statement statement = new Statement.Builder(AMOUNT_STR, DATE_STR).setNote(NOTE).setType(Expense).setId(ID_STR)
                .setCategory(new Category(CATEGORY_ID, CATEGORY_NAME)).build();

        boolean result = underTest.updateStatement(statement);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).update(argument.capture(), eq(ID_STR));

        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(AMOUNT_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY), equalTo(CATEGORY_ID));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE), equalTo(DATE_STR));
        assertThat((Integer) argument.getValue().get(COLUMN_NAME_IS_INCOME), equalTo(0));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(NOTE));

        assertThat(result, equalTo(true));
    }
}
