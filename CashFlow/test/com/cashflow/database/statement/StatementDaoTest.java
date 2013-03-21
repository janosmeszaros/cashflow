package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
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
    private static final String EQUALS = " = ?";
    private StatementDao underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase db;
    @Mock
    private Cursor cursorMock;

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

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenEverythingIsOkThenCallProperFunctionAndReturnTrue() {
        ContentValues values = new ContentValues();
        when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(1L);

        boolean result = underTest.save(values);

        verify(provider, times(1)).getWritableDb();
        verify(db, times(1)).insert(TABLE_NAME, COLUMN_NAME_NULLABLE, values);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testSaveWhenSomethinWrongWithDatabaseThenShouldReturnFalse() {
        ContentValues values = new ContentValues();
        when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(-1L);

        boolean result = underTest.save(values);

        verify(provider, times(1)).getWritableDb();
        verify(db, times(1)).insert(TABLE_NAME, COLUMN_NAME_NULLABLE, values);
        assertThat(result, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIdIsEmptyThenThrowException() {
        ContentValues values = new ContentValues();

        underTest.update(values, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamValuesIsNullThenThrowException() {
        underTest.update(null, ID);
    }

    @Test
    public void testUpdateWhenEverythingIsOkThenShouldCallProperFunctionAndReturnTrue() {
        ContentValues values = new ContentValues();
        String id = "id";
        when(db.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(1);

        boolean result = underTest.update(values, id);

        verify(provider, times(1)).getWritableDb();
        verify(db, times(1)).update(TABLE_NAME, values, _ID + EQUALS, new String[]{id});
        assertThat(result, equalTo(true));
    }

    @Test
    public void testUpdateWhenSomethinWrongWithDatabaseThenShouldReturnFalse() {
        ContentValues values = new ContentValues();
        String id = "2";
        when(db.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(0);

        boolean result = underTest.update(values, id);

        verify(provider, times(1)).getWritableDb();
        verify(db, times(1)).update(TABLE_NAME, values, _ID + EQUALS, new String[]{id});
        assertThat(result, equalTo(false));
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
        when(db.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null)).thenReturn(cursorMock);

        Cursor cursor = underTest.getIncomes();

        verify(provider).getReadableDb();
        verify(db).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null);
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
