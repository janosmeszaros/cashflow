package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
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
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ListStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link ListStatementActivity} test, specially this class tests the income listing functionality.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class ListIncomesActivityTest {

    private static final int BAD_REQUEST_CODE = 1;
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private ListStatementActivity underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new ListStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new ListStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        underTest.onCreate(null);
    }

    private void addBindings(ActivityModule module) {
        module.addBinding(StatementPersistentService.class, statementPersistentService);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenListStatementIsExpenseThenTitleShouldBeListExpenseTitle() {
        ListStatementActivity underTest = new ListStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        underTest.onCreate(null);

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_incomes)));
    }

    @Test
    public void testOnActivityResultWhenRequestCodeAndResultCodeIsOkThenItShouldRefreshList() {
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_OK, null);

        // Needed 3 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. The third one is the tested one.
        verify(statementPersistentService, times(3)).getStatement(StatementType.Income);
    }

    @Test
    public void testOnActivityResultWhenRequestCodeIsNotOkThenItShouldntRefreshList() {
        underTest.onActivityResult(BAD_REQUEST_CODE, RESULT_OK, null);

        // Needed 2 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Income);
    }

    @Test
    public void testOnActivityResultWhenResultCodeIsNotOkThenItShouldntRefreshList() {
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_CANCELED, null);

        // Needed 2 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Income);
    }

    @Test
    public void shouldContainList() {
        ListView listView = (ListView) underTest.findViewById(R.id.list_statement);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        Cursor cursor = adapter.getCursor();

        // Needed 2 times because it gets invoked on test start when app counting the Balance.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Income);
        assertThat(cursor.getColumnCount(), equalTo(fromColumns.length));
        assertThat(cursor.getInt(0), equalTo(values[0]));
        assertThat(cursor.getLong(1), equalTo(values[1]));
        assertThat(cursor.getString(2), equalTo(values[2]));
        assertThat(cursor.getString(3), equalTo(values[3]));
    }

    private void setUpPersistentService() {
        MatrixCursor matrixCursor = new MatrixCursor(fromColumns);
        matrixCursor.addRow(values);
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(matrixCursor);
    }
}
