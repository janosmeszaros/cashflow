package com.cashflow.statement.database;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.SQLiteDbProvider;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link StatementDao} test class.
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class StatementDaoTest {
    private static final String ID = "1";

    private StatementDao underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase db;
    @Mock
    private Cursor cursorMock;
    @Mock
    private ContentValues values;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(db);
        when(provider.getReadableDb()).thenReturn(db);

        underTest = new StatementDao(provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new StatementDao(null);
    }

    @Test
    public void testGetExpensesWhenEverythingIsOkThenCallProperFunctionAndReturnCursor() {
        when(db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, EXPENSE_SELECTION, null, null, null, null)).thenReturn(cursorMock);

        Cursor cursor = underTest.getExpenses();

        verify(provider).getReadableDb();
        verify(db).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, EXPENSE_SELECTION, null, null, null, null);
        assertThat(cursor, equalTo(cursorMock));
    }

    @Test
    public void testGetIncomesWhenEverythingIsOkThenCallProperFunctionAndReturnCursor() {
        when(db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null)).thenReturn(cursorMock);

        Cursor cursor = underTest.getIncomes();

        verify(provider).getReadableDb();
        verify(db).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null);
        assertThat(cursor, equalTo(cursorMock));
    }

    @Test
    public void testGetRecurringIncomesWhenEverythingIsOkThenCallProperFunctionAndReturnCursor() {
        when(db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null)).thenReturn(
                cursorMock);

        Cursor cursor = underTest.getRecurringIncomes();

        verify(provider).getReadableDb();
        verify(db).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
        assertThat(cursor, equalTo(cursorMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatementByIdWhenParamIdIsEmptyThenShouldThrowException() {
        underTest.getStatementById("");
    }

    @Test
    public void testGetStatementByIdWhenParamIdIsOkThenShouldCallProperFunctionAndReturnCursor() {
        when(db.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{ID})).thenReturn(cursorMock);

        Cursor cursor = underTest.getStatementById(ID);

        verify(provider).getReadableDb();
        verify(db).rawQuery(SELECT_STATEMENT_BY_ID, new String[]{ID});
        assertThat(cursor, equalTo(cursorMock));
    }
}
