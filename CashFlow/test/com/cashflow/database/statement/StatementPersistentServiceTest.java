package com.cashflow.database.statement;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
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

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class StatementPersistentServiceTest {

    private static final String ZERO_AMOUNT = "0";
    private static final String ID_STR = "0";
    private static final String WRONG_AMOUNT_FORMAT = "hello";
    private static final String AMOUNT_STR = "1234";
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

        underTest.saveStatement(EMPTY_STR, DATE_STR, NOTE, StatementType.Expense);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenDateIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.saveStatement(AMOUNT_STR, EMPTY_STR, NOTE, StatementType.Expense);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.saveStatement(AMOUNT_STR, DATE_STR, EMPTY_STR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStatementWhenAmountIsNotANumberStringIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.saveStatement(WRONG_AMOUNT_FORMAT, DATE_STR, NOTE, StatementType.Expense);
    }

    @Test
    public void testSaveStatementWhenAmountIsZeroStringIsEmptyThenShouldReturnFalse() {
        underTest = new StatementPersistenceService(dao);

        boolean statement = underTest.saveStatement(ZERO_AMOUNT, DATE_STR, NOTE, StatementType.Expense);

        assertThat(statement, equalTo(false));
    }

    @Test
    public void testSaveStatementWhenEveryParameterIsFineThenShouldCallDaosSaveMethodAndReturnTrue() {
        underTest = new StatementPersistenceService(dao);
        // ContentValues values = createContentValue(new BigDecimal(AMOUNT_STR), DATE_STR, NOTE, StatementType.Expense);

        boolean statement = underTest.saveStatement(AMOUNT_STR, DATE_STR, NOTE, StatementType.Expense);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).save(argument.capture());

        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(AMOUNT_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE), equalTo(DATE_STR));
        assertThat((Integer) argument.getValue().get(COLUMN_NAME_IS_INCOME), equalTo(0));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(NOTE));

        assertThat(statement, equalTo(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementWhenTypeIsNullThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.getStatement(null);
    }

    @Test
    public void testGetStatementWhenTypeIsExpenseThenShouldReturnCursorWithExpenses() {
        underTest = new StatementPersistenceService(dao);

        Cursor statement = underTest.getStatement(StatementType.Expense);

        verify(dao).getExpenses();
        assertThat(statement, equalTo(expenseCursor));
    }

    @Test
    public void testGetStatementWhenTypeIsIncomesThenShouldReturnCursorWithIncomes() {
        underTest = new StatementPersistenceService(dao);

        Cursor statement = underTest.getStatement(StatementType.Income);

        verify(dao).getIncomes();
        assertThat(statement, equalTo(incomeCursor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountStrIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.updateStatement(ID_STR, EMPTY_STR, DATE_STR, NOTE, StatementType.Expense);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenDateIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.updateStatement(ID_STR, AMOUNT_STR, EMPTY_STR, NOTE, StatementType.Expense);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenStatementTypeIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.updateStatement(ID_STR, AMOUNT_STR, DATE_STR, EMPTY_STR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenAmountIsNotANumberStringIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.updateStatement(ID_STR, WRONG_AMOUNT_FORMAT, DATE_STR, NOTE, StatementType.Expense);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatementWhenIdStrIsEmptyThenShouldThrowException() {
        underTest = new StatementPersistenceService(dao);

        underTest.updateStatement(EMPTY_STR, AMOUNT_STR, DATE_STR, NOTE, StatementType.Expense);
    }

    @Test
    public void testUpdateStatementWhenEveryParameterIsFineThenShouldCallDaosUpdateMethodAndReturnTrue() {
        underTest = new StatementPersistenceService(dao);
        // ContentValues values = createContentValue(new BigDecimal(AMOUNT_STR), DATE_STR, NOTE, StatementType.Expense);

        boolean statement = underTest.updateStatement(ID_STR, AMOUNT_STR, DATE_STR, NOTE, StatementType.Expense);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).update(argument.capture(), eq(ID_STR));

        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(AMOUNT_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE), equalTo(DATE_STR));
        assertThat((Integer) argument.getValue().get(COLUMN_NAME_IS_INCOME), equalTo(0));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(NOTE));

        assertThat(statement, equalTo(true));
    }
    // private ContentValues createContentValue(BigDecimal amount, String date, String note, StatementType type) {
    // ContentValues values = new ContentValues();
    // values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT, amount.toString());
    // values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE, date);
    // values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_IS_INCOME, type.equals(StatementType.Income) ? 1 : 0);
    // values.put(DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE, note);
    //
    // return values;
    // }

}
