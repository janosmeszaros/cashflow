package com.cashflow.activity;

import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.database.MatrixCursor;
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.R;
import com.cashflow.activity.util.ActivityModule;
import com.cashflow.activity.util.AddStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AddExpenseActivity} test.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddExpenseActivityTest {
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, "2012", "note"};
    @Mock
    private StatementPersistenceService statementPersistenceService;
    private Balance balance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new AddStatementActivityProvider());

        setUpPersistentService();

        module.addBinding(StatementPersistenceService.class, statementPersistenceService);
        balance = Balance.getInstance(statementPersistenceService);
        module.addBinding(Balance.class, balance);
        ActivityModule.setUp(this, module);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistenceService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistenceService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistenceService.saveStatement(anyString(), anyString(), anyString(), (StatementType) anyObject())).thenReturn(true);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenExpenseActivityThanTitleShouldBeAddExpense() {
        AddStatementActivity activity = new AddStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        activity.onCreate(null);

        assertThat((String) activity.getTitle(), equalTo(activity.getString(R.string.title_activity_add_expense)));
    }

    @Test
    public void testSubmitWhenOkThanCallProperFunctionAndRefreshBalanceAndCloseing() {
        AddStatementActivity activity = new AddStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        activity.onCreate(null);
        setViewsValues(activity);

        activity.submit(null);

        verify(statementPersistenceService).saveStatement(AMOUNT, DATE, NOTES, StatementType.Expense);
        assertThat(balance.getBalance(), equalTo(-1234D));
    }

    //    @Test
    //    public void testWhenClickOnDateButtonThenShowDatePickerDialog() {
    //        AddStatementActivity activity = new AddStatementActivity();
    //        //        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(activity);
    //        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
    //        activity.onCreate(null);
    //        Button button = (Button) activity.findViewById(R.id.dateButton);
    //
    //        button.performClick();
    //
    //        FragmentManager fragmentManager = activity.getSupportFragmentManager();
    //        Fragment fragment = fragmentManager.getFragment(new Bundle(), "datePicker");
    //        Assert.assertNotNull(fragment);
    //    }

    private void setViewsValues(AddStatementActivity activity) {
        EditText notes = (EditText) activity.findViewById(R.id.notesText);
        notes.setText(NOTES);
        EditText amount = (EditText) activity.findViewById(R.id.amountText);
        amount.setText(AMOUNT);
        Button button = (Button) activity.findViewById(R.id.dateButton);
        button.setText(DATE);
    }
}
