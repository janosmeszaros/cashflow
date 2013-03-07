package com.cashflow.activity;

import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.cashflow.R;
import com.cashflow.activity.util.ActivityModule;
import com.cashflow.activity.util.ListStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link ListStatementActivity} test, specially this class tests the expense listing functionality.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class ListExpensesActivityTest {

    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    @Mock
    private StatementPersistentService statementPersistentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new ListStatementActivityProvider());
        MatrixCursor matrixCursor = new MatrixCursor(fromColumns);
        matrixCursor.addRow(values);
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(matrixCursor);

        module.addBinding(StatementPersistentService.class, statementPersistentService);
        ActivityModule.setUp(this, module);

    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void shouldTrue() {
        Assert.assertTrue(true);
    }

    @Test
    public void testWhenListStatementIsExpenseThenTitleShouldBeListExpenseTitle() {
        ListStatementActivity activity = new ListStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        activity.onCreate(null);

        assertThat((String) activity.getTitle(), equalTo(activity.getString(R.string.title_activity_list_expenses)));
    }

    @Test
    public void shouldContainList() {
        ListStatementActivity activity = new ListStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        activity.onCreate(null);

        ListView listView = (ListView) activity.findViewById(R.id.list_statement);

        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        Cursor cursor = adapter.getCursor();
        assertThat(cursor.getColumnCount(), equalTo(fromColumns.length));
        assertThat(cursor.getInt(0), equalTo(values[0]));
        assertThat(cursor.getLong(1), equalTo(values[1]));
        assertThat(cursor.getString(2), equalTo(values[2]));
        assertThat(cursor.getString(3), equalTo(values[3]));
    }
}
