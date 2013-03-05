package com.cashflow.activity;

import static com.cashflow.constants.Constants.INCOME_EXTRA;
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
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AddExpenseActivity} test.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddIncomeActivityTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    @Mock
    private StatementPersistentService statementPersistentService;
    private Balance balance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new AddStatementActivityProvider());

        setUpPersistentService();

        module.addBinding(StatementPersistentService.class, statementPersistentService);
        balance = Balance.getInstance(statementPersistentService);
        module.addBinding(Balance.class, balance);
        ActivityModule.setUp(this, module);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement(anyString(), anyString(), anyString(), (StatementType) anyObject())).thenReturn(true);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenIncomeActivityThanTitleShouldBeAddExpense() {
        AddStatementActivity activity = new AddStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        activity.onCreate(null);

        assertThat((String) activity.getTitle(), equalTo(activity.getString(R.string.title_activity_add_income)));
    }

    //    @Test
    //    public void testWhenIncomeActivityThanContentViewShouldBeAddStatement() {
    //        AddStatementActivity activity = new AddStatementActivity();
    //        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
    //        activity.onCreate(null);
    //        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(activity);
    //
    //        assertThat(shadowActivity.getContentView().getId(), equalTo(R.layout.activity_add_statement));
    //    }

    @Test
    public void testSubmitWhenOkThanCallProperFunctionAndRefreshBalanceAndCloseing() {
        AddStatementActivity activity = new AddStatementActivity();
        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        activity.onCreate(null);
        setViewsValues(activity);

        activity.submit(null);

        verify(statementPersistentService).saveStatement(AMOUNT, DATE, NOTES, StatementType.Income);
        assertThat(balance.getBalance(), equalTo(-1234D));
    }

    private void setViewsValues(AddStatementActivity activity) {
        EditText notes = (EditText) activity.findViewById(R.id.notesText);
        notes.setText(NOTES);
        EditText amount = (EditText) activity.findViewById(R.id.amountText);
        amount.setText(AMOUNT);
        Button button = (Button) activity.findViewById(R.id.dateButton);
        button.setText(DATE);
    }
}
