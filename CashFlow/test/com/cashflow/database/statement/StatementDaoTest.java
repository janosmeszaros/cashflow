package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts;
import com.cashflow.database.SQLiteDbProvider;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link StatementDao} test class.
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class StatementDaoTest {
    private StatementDao underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(db);
        when(provider.getReadableDb()).thenReturn(db);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new StatementDao(null);
    }

    @Test
    public void testSaveWhenEverythingIsOkThenCallProperFunction() {
        underTest = new StatementDao(provider);
        ContentValues values = new ContentValues();

        underTest.save(values);

        verify(db, times(1)).insert(DatabaseContracts.AbstractStatement.TABLE_NAME,
                DatabaseContracts.AbstractStatement.COLUMN_NAME_NULLABLE, values);
    }

    @Test
    public void testUpdateWhenEverythingIsOkThenCallProperFunction() {
        underTest = new StatementDao(provider);
        ContentValues values = new ContentValues();
        String id = "id";

        underTest.update(values, id);

        verify(db, times(1)).update(DatabaseContracts.AbstractStatement.TABLE_NAME, values, _ID + " = " + id, null);
    }

    @Test
    public void testGetExpensesWhenEverythingIsOkThenCallProperFunction() {
        underTest = new StatementDao(provider);

        underTest.getExpenses();

        verify(provider).getReadableDb();
        verify(db).query(TABLE_NAME, PROJECTION, EXPENSE_SELECTION,
                null,
                null, null, null);
    }

    @Test
    public void testGetIncomesWhenEverythingIsOkThenCallProperFunction() {
        underTest = new StatementDao(provider);

        underTest.getIncomes();

        verify(provider).getReadableDb();
        verify(db).query(TABLE_NAME, PROJECTION, INCOME_SELECTION,
                null,
                null, null, null);
    }
}
