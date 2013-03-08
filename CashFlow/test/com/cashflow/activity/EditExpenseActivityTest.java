package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.EditStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;

/**
 * {@link EditStatementActivity} test, specially this class tests the expense editing functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class EditExpenseActivityTest {
    private static final String CHANGED_NOTE = "changedNote";
    private static final String CHANGED_DATE = "2012";
    private static final String CHANGED_AMOUNT = "123";
    private static final String ID = "1";
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, CHANGED_DATE, "note"};
    private EditStatementActivity underTest;
    private ShadowFragmentActivity shadowFragmentActivity;
    private Balance balance;
    @Mock
    private StatementPersistenceService statementPersistenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new EditStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new EditStatementActivity();
        shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData());
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testWhenEditExpenseActivityThanTitleShouldBeEditExpense() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_expenses)));
    }

    @Test
    public void testWhenEditExpenseActivityCreatedThanShouldFillUpViewsWithDataFromIntent() {
        TextView amount = (TextView) underTest.findViewById(R.id.amountText);
        TextView note = (TextView) underTest.findViewById(R.id.notesText);
        Button date = (Button) underTest.findViewById(R.id.dateButton);
        assertThat(amount.getText().toString(), equalTo(AMOUNT));
        assertThat(note.getText().toString(), equalTo(NOTES));
        assertThat(date.getText().toString(), equalTo(DATE));
    }

    @Test
    public void testSubmitWhenAmountHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        setViewsValues(CHANGED_AMOUNT, NOTES, DATE);

        underTest.submit(null);

        verify(statementPersistenceService, times(1)).updateStatement(ID, CHANGED_AMOUNT, DATE, NOTES, StatementType.Expense);
        assertThat(balance.getBalance(), equalTo(-1111D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
    }

    @Test
    public void testSubmitWhenDateHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        setViewsValues(AMOUNT, NOTES, CHANGED_DATE);

        underTest.submit(null);

        verify(statementPersistenceService, times(1)).updateStatement(ID, AMOUNT, CHANGED_DATE, NOTES, StatementType.Expense);
        assertThat(balance.getBalance(), equalTo(0D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
    }

    @Test
    public void testSubmitWhenNotesHasChangedThanShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        setViewsValues(AMOUNT, CHANGED_NOTE, DATE);

        underTest.submit(null);

        verify(statementPersistenceService, times(1)).updateStatement(ID, AMOUNT, DATE, CHANGED_NOTE, StatementType.Expense);
        assertThat(balance.getBalance(), equalTo(0D));
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
    }

    @Test
    public void testSubmitWhenNothingHasChangedThanCallProperFunctionAndResultCodeShouldBeCanceled() {
        setViewsValues(AMOUNT, NOTES, DATE);

        underTest.submit(null);

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
    }

    private void setViewsValues(String amountValue, String notesValue, String dateValue) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(notesValue);
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(amountValue);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(dateValue);
    }

    private Intent setUpIntentData() {
        Intent intent = new Intent();
        intent.putExtra(ID_EXTRA, ID);
        intent.putExtra(AMOUNT_EXTRA, AMOUNT);
        intent.putExtra(NOTE_EXTRA, NOTES);
        intent.putExtra(DATE_EXTRA, DATE);
        intent.putExtra(STATEMENT_TYPE_EXTRA, EXPENSE_EXTRA);
        return intent;
    }

    private void addBindings(ActivityModule module) {
        module.addBinding(StatementPersistenceService.class, statementPersistenceService);
        balance = Balance.getInstance(statementPersistenceService);
        module.addBinding(Balance.class, balance);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistenceService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistenceService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistenceService.saveStatement(anyString(), anyString(), anyString(), (StatementType) anyObject())).thenReturn(true);
        when(statementPersistenceService.updateStatement(anyString(), anyString(), anyString(), anyString(), (StatementType) anyObject())).thenReturn(
                true);
    }
}
