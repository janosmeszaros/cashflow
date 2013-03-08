package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ListStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowActivity.IntentForResult;

/**
 * {@link ListStatementActivity} test, specially this class tests the expense listing functionality.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class ListExpensesActivityTest {
    private static final Logger LOG = LoggerFactory.getLogger(ListExpensesActivityTest.class);
    private static final int BAD_REQUEST_CODE = 1;
    private static final String ID = "2";
    private static final String NOTES = "notes2";
    private static final String DATE = "2013";
    private static final String AMOUNT = "12345678";
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private ListStatementActivity underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;

    @Before
    public void setUp() {
        LOG.debug("\nTest starts!!!");
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new ListStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new ListStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenListStatementIsExpenseThenTitleShouldBeListExpenseTitle() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_expenses)));
    }

    @Test
    public void testOnClickWhenStatementTypeIsExpenseThenCreateIntentAndStartsItWithExtrasSetted() {
        ShadowActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(AMOUNT, NOTES, DATE, ID);

        underTest.onClick(underTest.findViewById(R.id.list_statement));

        IntentForResult startedActivityForResult = shadowActivity.getNextStartedActivityForResult();
        String idExtra = startedActivityForResult.intent.getStringExtra(ID_EXTRA);
        String amountExtra = startedActivityForResult.intent.getStringExtra(AMOUNT_EXTRA);
        String noteExtra = startedActivityForResult.intent.getStringExtra(NOTE_EXTRA);
        String dateExtra = startedActivityForResult.intent.getStringExtra(DATE_EXTRA);
        String typeExtra = startedActivityForResult.intent.getStringExtra(STATEMENT_TYPE_EXTRA);
        int requestCode = startedActivityForResult.requestCode;
        assertThat(idExtra, equalTo(ID));
        assertThat(amountExtra, equalTo(AMOUNT));
        assertThat(noteExtra, equalTo(NOTES));
        assertThat(dateExtra, equalTo(DATE));
        assertThat(typeExtra, equalTo(EXPENSE_EXTRA));
        assertThat(requestCode, equalTo(EDIT_ACTIVITY_CODE));
    }

    @Test
    public void testOnActivityResultWhenRequestCodeAndResultCodeIsOkThenItShouldRefreshList() {
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_OK, null);

        // Needed 3 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. The third one is the tested one.
        verify(statementPersistentService, times(3)).getStatement(StatementType.Expense);
    }

    @Test
    public void testOnActivityResultWhenRequestCodeIsNotOkThenItShouldntRefreshList() {
        underTest.onActivityResult(BAD_REQUEST_CODE, RESULT_OK, null);

        // Needed 2 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Expense);
    }

    @Test
    public void testOnActivityResultWhenResultCodeIsNotOkThenItShouldntRefreshList() {
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_CANCELED, null);

        // Needed 2 times because it gets invoked on test start when app 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Expense);
    }

    @Test
    public void shouldContainList() {
        ListView listView = (ListView) underTest.findViewById(R.id.list_statement);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        Cursor cursor = adapter.getCursor();

        // Needed 2 times because it gets invoked on test start when app counting the Balance.
        verify(statementPersistentService, times(2)).getStatement(StatementType.Expense);
        assertThat(cursor.getColumnCount(), equalTo(fromColumns.length));
        assertThat(cursor.getInt(0), equalTo(values[0]));
        assertThat(cursor.getLong(1), equalTo(values[1]));
        assertThat(cursor.getString(2), equalTo(values[2]));
        assertThat(cursor.getString(3), equalTo(values[3]));
    }

    private void addBindings(ActivityModule module) {
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
    }

    private void setUpPersistentService() {
        MatrixCursor matrixCursor = new MatrixCursor(fromColumns);
        matrixCursor.addRow(values);
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(matrixCursor);
    }

    private void setViewsValues(String amountValue, String notesValue, String dateValue, String idValue) {
        TextView notes = (TextView) underTest.findViewById(R.id.row_note);
        notes.setText(notesValue);
        TextView id = (TextView) underTest.findViewById(R.id.row_id);
        id.setText(idValue);
        TextView amount = (TextView) underTest.findViewById(R.id.row_amount);
        amount.setText(amountValue);
        TextView button = (TextView) underTest.findViewById(R.id.row_date);
        button.setText(dateValue);
    }
}
