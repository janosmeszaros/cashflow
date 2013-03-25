package com.cashflow.activity.statement;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.BaseColumns._ID;
import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;
import static com.cashflow.database.statement.StatementType.Expense;
import static com.cashflow.database.statement.StatementType.Income;
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
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
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
public class ListStatementActivityTest {
    public static final String[] PROJECTION = new String[]{TABLE_NAME + "." + _ID, _ID, COLUMN_NAME_AMOUNT,
        AbstractCategory.COLUMN_NAME_CATEGORY_NAME, COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};
    public static final int[] TO_VIEWS = {R.id.row_id, R.id.row_amount, R.id.row_category, R.id.row_date, R.id.row_note, R.id.row_interval};
    private static final Logger LOG = LoggerFactory.getLogger(ListStatementActivityTest.class);
    private static final int BAD_REQUEST_CODE = 1;
    private static final String ID = "2";
    private static final String NOTES = "notes2";
    private static final Category CATEGORY = new Category("1", "category2");
    private static final String DATE = "2013";
    private static final String AMOUNT = "12345678";
    private static final RecurringInterval INTERVAL = RecurringInterval.biweekly;

    private final Object[] values = new Object[]{1, 1, 1234L, "category", "2012", "note", "none"};
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
    }

    private void setExpenseIntent() {
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        underTest.onCreate(null);
    }

    private void setIncomeIntent() {
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenListStatementIsExpenseThenTitleShouldBeListExpenseTitle() {
        setExpenseIntent();
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_expenses)));
    }

    @Test
    public void testEditButtonOnClickWhenStatementTypeIsExpenseThenCreateIntentAndStartsItWithExtrasSetted() {
        setExpenseIntent();
        ShadowActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setCategory(CATEGORY).setId(ID).setRecurringInterval(INTERVAL)
                .setType(Expense).build());

        underTest.editButtonOnClick(underTest.findViewById(R.id.list_statement));

        IntentForResult startedActivityForResult = shadowActivity.getNextStartedActivityForResult();
        String idExtra = startedActivityForResult.intent.getStringExtra(ID_EXTRA);
        int requestCode = startedActivityForResult.requestCode;

        assertThat(idExtra, equalTo(ID));
        assertThat(requestCode, equalTo(EDIT_ACTIVITY_CODE));
    }

    @Test
    public void testOnActivityResultWhenRequestCodeAndResultCodeIsOkThenItShouldRefreshList() {
        setExpenseIntent();
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_OK, null);

        // Needed 3 times because it gets invoked on test start when the application 
        // counting the Balance and when fills up the list at first time. The third one is the tested one.
        verify(statementPersistentService, times(3)).getStatement(Expense);
    }

    @Test
    public void testOnActivityResultWhenRequestCodeIsNotOkThenItShouldntRefreshList() {
        setExpenseIntent();
        underTest.onActivityResult(BAD_REQUEST_CODE, RESULT_OK, null);

        // Needed 2 times because it gets invoked on test start when the application 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(Expense);
    }

    @Test
    public void testOnActivityResultWhenResultCodeIsNotOkThenItShouldntRefreshList() {
        setExpenseIntent();
        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_CANCELED, null);

        // Needed 2 times because it gets invoked on test start when the application 
        // counting the Balance and when fills up the list at first time. 
        // It should'nt invoked in third time.
        verify(statementPersistentService, times(2)).getStatement(Expense);
    }

    @Test
    public void shouldContainList() {
        setExpenseIntent();
        ListView listView = (ListView) underTest.findViewById(R.id.list_statement);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
        Cursor cursor = adapter.getCursor();

        // Needed 2 times because it gets invoked on test start when the application counting the Balance.
        verify(statementPersistentService, times(2)).getStatement(Expense);
        assertThat(cursor.getColumnCount(), equalTo(PROJECTION.length));
        assertThat(cursor.getInt(0), equalTo(values[0]));
        assertThat(cursor.getInt(1), equalTo(values[1]));
        assertThat(cursor.getLong(2), equalTo(values[2]));
        assertThat(cursor.getString(3), equalTo(values[3]));
        assertThat(cursor.getString(4), equalTo(values[4]));
    }

    private void addBindings(ActivityModule module) {
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
    }

    private void setUpPersistentService() {
        MatrixCursor matrixCursor = new MatrixCursor(PROJECTION);
        matrixCursor.addRow(values);
        when(statementPersistentService.getStatement(Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(Income)).thenReturn(matrixCursor);
    }

    private void setViewsValues(Statement statement) {
        TextView notes = (TextView) underTest.findViewById(R.id.row_note);
        notes.setText(statement.getNote());
        TextView id = (TextView) underTest.findViewById(R.id.row_id);
        id.setText(statement.getId());
        TextView amount = (TextView) underTest.findViewById(R.id.row_amount);
        amount.setText(statement.getAmount());
        TextView category = (TextView) underTest.findViewById(R.id.row_category);
        category.setText(statement.getCategory().getName());
        TextView button = (TextView) underTest.findViewById(R.id.row_date);
        button.setText(statement.getDate());
        TextView interval = (TextView) underTest.findViewById(R.id.row_interval);
        interval.setText(statement.getRecurringInterval().toString());
    }

    //*********** Income test ************************************//

    @Test
    public void testWhenListStatementIsIncomeThenTitleShouldBeListIncomeTitle() {
        setIncomeIntent();
        ListStatementActivity underTest = new ListStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        underTest.onCreate(null);

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_incomes)));
    }

}
