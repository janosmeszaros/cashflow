package com.cashflow.activity;

import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.AddStatementActivityProvider;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowTextView;

/**
 * {@link AddStatementActivity} test, specially this class tests the income adding functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddIncomeActivityTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private Balance balance;
    private AddStatementActivity underTest;
    private final ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(underTest,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;
    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new AddStatementActivityProvider());

        setUpMocks();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new AddStatementActivity();
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
        underTest.onCreate(null);

    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenIncomeActivityThenTitleShouldBeAddExpense() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_add_income)));
    }

    //    @Test
    //    public void testWhenIncomeActivityThenContentViewShouldBeAddStatement() {
    //        AddStatementActivity activity = new AddStatementActivity();
    //        activity.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA));
    //        activity.onCreate(null);
    //        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(activity);
    //
    //        assertThat(shadowActivity.getContentView().getId(), equalTo(R.layout.activity_add_statement));
    //    }

    @Test
    public void testOnCreateWhenCalledThenRercurringAreaShouldBeVisible() {
        LinearLayout recurringArea = (LinearLayout) underTest.findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.VISIBLE));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringCheckBoxOnClickListenerShouldBeRecurringCheckBoxOnClickListener() {
        CheckBox recurringCheckBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);
        ShadowTextView shadowTextView = Robolectric.shadowOf(recurringCheckBox);

        assertThat((RecurringCheckBoxOnClickListener) shadowTextView.getOnClickListener(), equalTo(checkBoxListener));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnCreateWhenCalledThenRecurringSpinnersAdapterShouldBeSettedToArrayAdapterWithValuesFromRecurringInterval() {
        Spinner recurringSpinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);

        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testSubmitWhenOkThenShouldRefreshBalance() {
        setViewsValues(AMOUNT, 0);

        underTest.submit(null);

        assertThat(balance.getBalance(), equalTo(1234D));
    }

    @Test
    public void testSubmitWhenIncomeIsNotRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        setViewsValues(AMOUNT, 0);
        Statement statement = createStatement(AMOUNT, DATE, NOTES, StatementType.Income, 0);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitWhenOkThenShouldSetTheResultToOkAndCloseTheActivity() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(AMOUNT, 0);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenIncomeIsRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        setViewsValues(AMOUNT, 3);
        Statement statement = createStatement(AMOUNT, DATE, NOTES, StatementType.Income, 3);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(INVALID_AMOUNT, 0);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_CANCELED));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(String amountStr, int selection) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(NOTES);
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(amountStr);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(DATE);
        Spinner spinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(selection);

        if (selection != 0) {
            CheckBox recurringCheckBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);
            recurringCheckBox.setChecked(true);
        }
    }

    private void addBindings(ActivityModule module) {
        balance = Balance.getInstance(statementPersistentService);

        module.addBinding(Balance.class, balance);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
        module.addBinding(RecurringCheckBoxOnClickListener.class, checkBoxListener);
        module.addBinding(SpinnerAdapter.class, arrayAdapter);
    }

    private void setUpMocks() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement(createStatement(AMOUNT, DATE, NOTES, StatementType.Income, 0))).thenReturn(true);
        when(statementPersistentService.saveStatement(createStatement(INVALID_AMOUNT, DATE, NOTES, StatementType.Income, 0))).thenReturn(false);
    }

    private Statement createStatement(String amount, String date, String note, StatementType type, int recurringPos) {
        Builder builder = new Statement.Builder(amount, date);
        builder.setNote(note).setType(type);
        if (recurringPos != 0) {
            RecurringInterval item = arrayAdapter.getItem(recurringPos);
            builder.setRecurringInterval(item);
        }
        return builder.build();
    }
}
