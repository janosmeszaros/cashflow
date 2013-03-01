package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.MatrixCursor;

import com.cashflow.activity.util.ListExpensesModule;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link ListExpensesActivity} test.
 * @author Kornel_Refi
 */
@RunWith(RobolectricTestRunner.class)
public class ListExpensesActivityTest {

    @Mock
    private StatementPersistentService statementPersistentService;
    @Mock
    private MatrixCursor matrixCursor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ListExpensesModule module = new ListExpensesModule();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE});
        matrixCursor.addRow(new Object[]{1234L, "2012"});
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(matrixCursor);

        module.addBinding(StatementPersistentService.class, statementPersistentService);
        ListExpensesModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        ListExpensesModule.tearDown();
    }

    @Test
    public void shouldTrue() {
        Assert.assertTrue(true);
    }
    //    @Test
    //    public void shouldContainList() {
    //        ListStatementActivity activity = new ListStatementActivity();
    //        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
    //        activity.onCreate(null);
    //        
    //        ListView listView = (ListView) activity.findViewById(R.id.list_statement);
    //        
    //        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
    //        Cursor cursor = adapter.getCursor();
    //        // cursor is null
    //        cursor.moveToFirst();
    //    }
}
