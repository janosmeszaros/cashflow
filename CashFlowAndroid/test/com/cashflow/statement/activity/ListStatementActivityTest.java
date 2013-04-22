package com.cashflow.statement.activity;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;
import static com.cashflow.domain.StatementType.Expense;
import static com.cashflow.domain.StatementType.Income;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ListStatementActivityProvider;
import com.cashflow.dao.StatementDAO;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link ListStatementFragment} test, specially this class tests the expense listing functionality.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class ListStatementActivityTest {
    public static final String[] PROJECTION = new String[]{TABLE_NAME + "." + _ID, _ID, COLUMN_NAME_AMOUNT,
        AbstractCategory.COLUMN_NAME_CATEGORY_NAME, COLUMN_NAME_DATE, COLUMN_NAME_NOTE, COLUMN_NAME_INTERVAL};
    public static final int[] TO_VIEWS = {R.id.row_id, R.id.row_amount, R.id.row_category, R.id.row_date, R.id.row_note, R.id.row_interval};
    private static final Logger LOG = LoggerFactory.getLogger(ListStatementActivityTest.class);
    //    private static final int BAD_REQUEST_CODE = 1;
    private static final String CATEGORY_ID = "3";
    private static final String NOTE = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final Category CATEGORY = Category.builder("category").categoryId(CATEGORY_ID).build();

    //    private Fragment underTest;
    @Mock
    private StatementDAO statementDAO;

    @Before
    public void setUp() {
        LOG.debug("\nTest starts!!!");
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new ListStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        //        underTest = new ListStatementFragment();
    }

    //    private void setExpenseIntent() {
    //        final Bundle bundle = new Bundle();
    //        bundle.putString(STATEMENT_TYPE_EXTRA, Expense.toString());
    //        underTest.setArguments(bundle);
    //
    //        final ListActivity listActivity = new ListActivity();
    //        final FragmentTransaction transaction = listActivity.getSupportFragmentManager().beginTransaction();
    //        transaction.add(underTest, Expense.toString());
    //        transaction.commit();
    //    }

    //    private void setIncomeIntent() {
    //        final Bundle bundle = new Bundle();
    //        bundle.putString(STATEMENT_TYPE_EXTRA, Income.toString());
    //        underTest.setArguments(bundle);
    //
    //        final ListActivity listActivity = new ListActivity();
    //        final FragmentTransaction transaction = listActivity.getSupportFragmentManager().beginTransaction();
    //        transaction.add(underTest, Expense.toString());
    //        transaction.commit();
    //    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    //    @Test
    //    public void testWhenListStatementIsExpenseThenTitleShouldBeListExpenseTitle() {
    //        setExpenseIntent();
    //        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_expenses)));
    //    }

    //    @Test
    //    public void testEditButtonOnClickWhenStatementTypeIsExpenseThenCreateIntentAndStartsItWithExtrasSetted() {
    //        setExpenseIntent();
    //        ShadowFragment shadowFragment = Robolectric.shadowOf(underTest);
    //        setViewsValues(new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setCategory(CATEGORY).setId(ID).setRecurringInterval(INTERVAL)
    //                .setType(Expense).build());
    //
    //        underTest.editButtonOnClick(underTest.findViewById(R.id.list_statement));
    //
    //        IntentForResult startedActivityForResult = shadowActivity.getNextStartedActivityForResult();
    //        String idExtra = startedActivityForResult.intent.getStringExtra(ID_EXTRA);
    //        int requestCode = startedActivityForResult.requestCode;
    //
    //        assertThat(idExtra, equalTo(ID));
    //        assertThat(requestCode, equalTo(EDIT_ACTIVITY_CODE));
    //    }

    @Test
    public void testTrue() {
        Assert.assertTrue(true);
    }

    //    @Test
    //    public void testOnActivityResultWhenRequestCodeAndResultCodeIsOkThenItShouldRefreshList() {
    //        setExpenseIntent();
    //        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_OK, null);
    //
    //        // Needed 3 times because it gets invoked on test start when the application 
    //        // counting the Balance and when fills up the list at first time. The third one is the tested one.
    //        verify(statementDAO, times(3)).getExpenses();
    //    }
    //
    //    @Test
    //    public void testOnActivityResultWhenRequestCodeIsNotOkThenItShouldntRefreshList() {
    //        setExpenseIntent();
    //        underTest.onActivityResult(BAD_REQUEST_CODE, RESULT_OK, null);
    //
    //        // Needed 2 times because it gets invoked on test start when the application 
    //        // counting the Balance and when fills up the list at first time. 
    //        // It should'nt invoked in third time.
    //        verify(statementDAO, times(2)).getExpenses();
    //    }
    //
    //    @Test
    //    public void testOnActivityResultWhenResultCodeIsNotOkThenItShouldntRefreshList() {
    //        setExpenseIntent();
    //        underTest.onActivityResult(EDIT_ACTIVITY_CODE, RESULT_CANCELED, null);
    //
    //        // Needed 2 times because it gets invoked on test start when the application 
    //        // counting the Balance and when fills up the list at first time. 
    //        // It should'nt invoked in third time.
    //        verify(statementDAO, times(2)).getExpenses();
    //    }

    //    @Test
    //    public void shouldContainList() {
    //        setExpenseIntent();
    //        ListView listView = (ListView) underTest.findViewById(R.id.list_statement);
    //        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listView.getAdapter();
    //        Cursor cursor = adapter.getCursor();
    //
    //        // Needed 2 times because it gets invoked on test start when the application counting the Balance.
    //        verify(statementPersistentService, times(2)).getStatement(Expense);
    //        assertThat(cursor.getColumnCount(), equalTo(PROJECTION.length));
    //        assertThat(cursor.getInt(0), equalTo(values[0]));
    //        assertThat(cursor.getInt(1), equalTo(values[1]));
    //        assertThat(cursor.getLong(2), equalTo(values[2]));
    //        assertThat(cursor.getString(3), equalTo(values[3]));
    //        assertThat(cursor.getString(4), equalTo(values[4]));
    //    }

    private void addBindings(final ActivityModule module) {
        module.addBinding(StatementDAO.class, statementDAO);
    }

    private void setUpPersistentService() {
        final Statement income = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).category(CATEGORY).recurringInterval(RecurringInterval.none)
                .build();
        final Statement expense = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).category(CATEGORY)
                .recurringInterval(RecurringInterval.none).build();

        final List<Statement> expenses = new ArrayList<Statement>();
        expenses.add(expense);
        final List<Statement> incomes = new ArrayList<Statement>();
        incomes.add(income);

        when(statementDAO.getExpenses()).thenReturn(expenses);
        when(statementDAO.getIncomes()).thenReturn(incomes);
    }

    //    private void setViewsValues(Statement statement) {
    //        TextView notes = (TextView) underTest.findViewById(R.id.row_note);
    //        notes.setText(statement.getNote());
    //        TextView id = (TextView) underTest.findViewById(R.id.row_id);
    //        id.setText(statement.getId());
    //        TextView amount = (TextView) underTest.findViewById(R.id.row_amount);
    //        amount.setText(statement.getAmount());
    //        TextView category = (TextView) underTest.findViewById(R.id.row_category);
    //        category.setText(statement.getCategory().getName());
    //        TextView button = (TextView) underTest.findViewById(R.id.row_date);
    //        button.setText(statement.getDate());
    //        TextView interval = (TextView) underTest.findViewById(R.id.row_interval);
    //        interval.setText(statement.getRecurringInterval().toString());
    //    }
    //
    //    //*********** Income test ************************************//
    //
    //    @Test
    //    public void testWhenListStatementIsIncomeThenTitleShouldBeListIncomeTitle() {
    //        setIncomeIntent();
    //        ListStatementFragment underTest = new ListStatementFragment();
    //        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, Income.toString()));
    //        underTest.onCreate(null);
    //
    //        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_list_incomes)));
    //    }

}
