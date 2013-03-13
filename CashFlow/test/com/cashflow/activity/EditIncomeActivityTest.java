package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.INTERVAL_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
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
import android.database.MatrixCursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.EditStatementActivityProvider;
import com.cashflow.constants.RecurringInterval;
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
 * {@link EditStatementActivity} test, specially this class tests the income editing functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class EditIncomeActivityTest {
    private static final String CHANGED_NOTE = "changedNote";
    private static final String CHANGED_DATE = "2012";
    private static final String CHANGED_AMOUNT = "123";
    private static final String ID = "1";
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String NONE_INTERVAL_EXTRA_VALUE = "none";
    private static final String INTERVAL_EXTRA_VALUE = "biweekly";
    private static final RecurringInterval INTERVAL = RecurringInterval.biweekly;
    private static final RecurringInterval NONE_INTERVAL = RecurringInterval.none;
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, CHANGED_DATE, "note"};
    private EditStatementActivity underTest;
    private ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(underTest,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());
    private Balance balance;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new EditStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new EditStatementActivity();
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenEditIncomeActivityThanTitleShouldBeEditIncome() {
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_incomes)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        Button button = (Button) underTest.findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testWhenEditIncomeActivityCreatedThanShouldFillUpViewsWithDataFromIntent() {
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        TextView amount = (TextView) underTest.findViewById(R.id.amountText);
        TextView note = (TextView) underTest.findViewById(R.id.notesText);
        Button date = (Button) underTest.findViewById(R.id.dateButton);

        assertThat(amount.getText().toString(), equalTo(AMOUNT));
        assertThat(note.getText().toString(), equalTo(NOTES));
        assertThat(date.getText().toString(), equalTo(DATE));
    }

    @Test
    public void testWhenRecurringIsNoneThenRecurringCheckBoxShouldBeUnchekedAndSpinnerValueIsNone() {
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(NONE_INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(false));
    }

    @Test
    public void testWhenRecurringIsOtherThanNoneThenRecurringCheckBoxShouldBeCheckedAndSpinnerValueIsSettedToCorrectValue() {
        underTest.setIntent(setUpIntentData(INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(true));
    }

    @Test
    public void testSubmitWhenAmountHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        setViewsValues(CHANGED_AMOUNT, NOTES, DATE);
        Statement statement = createStatement(ID, CHANGED_AMOUNT, DATE, NOTES, StatementType.Income, NONE_INTERVAL);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(statement);
        assertThat(balance.getBalance(), equalTo(-1111D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenRecurringIntervalHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        setViewsValues(AMOUNT, NOTES, DATE);
        setRecurringInterval(INTERVAL, true);
        Statement statement = createStatement(ID, AMOUNT, DATE, NOTES, StatementType.Income, INTERVAL);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(statement);
        assertThat(balance.getBalance(), equalTo(0D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenDateHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        setViewsValues(AMOUNT, NOTES, CHANGED_DATE);
        Statement statement = createStatement(ID, AMOUNT, CHANGED_DATE, NOTES, StatementType.Income, NONE_INTERVAL);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(statement);
        assertThat(balance.getBalance(), equalTo(0D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNotesHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        setViewsValues(AMOUNT, CHANGED_NOTE, DATE);
        Statement statement = createStatement(ID, AMOUNT, DATE, CHANGED_NOTE, StatementType.Income, NONE_INTERVAL);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(statement);
        assertThat(balance.getBalance(), equalTo(0D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNothingHasChangedThanCallProperFunctionAndResultCodeShouldBeCanceled() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(NONE_INTERVAL_EXTRA_VALUE));
        underTest.onCreate(null);

        setViewsValues(AMOUNT, NOTES, DATE);

        underTest.submit(null);

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(String amountValue, String notesValue, String dateValue) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(notesValue);
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(amountValue);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(dateValue);

    }

    private Intent setUpIntentData(String interval) {
        Intent intent = new Intent();
        intent.putExtra(ID_EXTRA, ID);
        intent.putExtra(AMOUNT_EXTRA, AMOUNT);
        intent.putExtra(NOTE_EXTRA, NOTES);
        intent.putExtra(DATE_EXTRA, DATE);
        intent.putExtra(STATEMENT_TYPE_EXTRA, INCOME_EXTRA);
        intent.putExtra(INTERVAL_EXTRA, interval);
        return intent;
    }

    private void addBindings(ActivityModule module) {
        balance = Balance.getInstance(statementPersistentService);
        module.addBinding(Balance.class, balance);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement((Statement) anyObject())).thenReturn(true);
        when(statementPersistentService.updateStatement((Statement) anyObject())).thenReturn(true);
    }

    private void setRecurringInterval(RecurringInterval interval, boolean chechbox) {
        Spinner spinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(arrayAdapter.getPosition(interval));

        CheckBox recurringCheckBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);
        recurringCheckBox.setChecked(true);
    }

    private Statement createStatement(String id, String amountStr, String date, String note, StatementType type, RecurringInterval interval) {
        return new Statement.Builder(amountStr, date).setNote(note).setType(type).setId(id).setRecurringInterval(interval).build();
    }
}
