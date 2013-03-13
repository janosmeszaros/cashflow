package com.cashflow.activity;

import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.AddStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowTextView;

/**
 * {@link AddStatementActivity} test, specially this class tests the expense adding functionality.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddExpenseActivityTest {
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private Balance balance;
    private AddStatementActivity underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new AddStatementActivityProvider());

        setUpPersistentService();

        addBindings(module);
        ActivityModule.setUp(this, module);

        underTest = new AddStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnCreateWhenTypeIsExpenseThenTitleShouldBeAddExpense() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_add_expense)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheDateButtonToTheCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        String date = fmtDateAndTime.format(calendar.getTime());

        assertThat((String) button.getText(), equalTo(date));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringAreaShouldBeGone() {
        LinearLayout recurringArea = (LinearLayout) underTest.findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.GONE));
    }

    @Test
    public void testSubmitWhenOkThenShouldRefreshBalance() {
        setViewsValues(AMOUNT);

        underTest.submit(null);

        assertThat(balance.getBalance(), equalTo(-1234D));
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldntRefreshBalance() {
        setViewsValues(INVALID_AMOUNT);

        underTest.submit(null);

        assertThat(balance.getBalance(), equalTo(0D));
    }

    @Test
    public void testSubmitWhenOkThenShouldSetTheResultToOkAndCloseTheActivity() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(AMOUNT);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(INVALID_AMOUNT);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_CANCELED));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
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

    private void setViewsValues(String amountStr) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(NOTES);
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(amountStr);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(DATE);
    }

    private void addBindings(ActivityModule module) {
        balance = Balance.getInstance(statementPersistentService);

        module.addBinding(StatementPersistenceService.class, statementPersistentService);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(Balance.class, balance);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement(createStatement(AMOUNT, DATE, NOTES, StatementType.Expense))).thenReturn(true);
        when(statementPersistentService.saveStatement(createStatement(INVALID_AMOUNT, DATE, NOTES, StatementType.Expense))).thenReturn(false);
    }

    private Statement createStatement(String amount, String date, String note, StatementType type) {
        return new Statement.Builder(amount, date).setNote(note).setType(type).build();
    }
}
