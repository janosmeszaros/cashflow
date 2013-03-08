package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.AddStatementActivityProvider;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;

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
    private static final String SAME_AMOUNT = "12";
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private Balance balance;
    private AddStatementActivity underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new AddStatementActivityProvider());

        setUpPersistentService();
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
    public void testWhenIncomeActivityThanTitleShouldBeAddExpense() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_add_income)));
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
    public void testSubmitWhenOkThanCallProperFunctionRefreshBalanceSetResultToTrueAndClose() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(AMOUNT);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(AMOUNT, DATE, NOTES, StatementType.Income);
        assertThat(shadowActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(balance.getBalance(), equalTo(1234D));
    }

    @Test
    public void testSubmitWhenAmountIsTheSameThanSetResultToCanceledAndClose() {
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        setViewsValues(SAME_AMOUNT);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(SAME_AMOUNT, DATE, NOTES, StatementType.Income);
        assertThat(shadowActivity.getResultCode(), equalTo(RESULT_CANCELED));
    }

    private void setViewsValues(String amountStr) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(NOTES);
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(amountStr);
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(DATE);
    }

    private void addBindings(ActivityModule module) {
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
        balance = Balance.getInstance(statementPersistentService);
        module.addBinding(Balance.class, balance);
    }

    private void setUpPersistentService() {
        MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement(eq(AMOUNT), anyString(), anyString(), (StatementType) anyObject())).thenReturn(true);
        when(statementPersistentService.saveStatement(eq(SAME_AMOUNT), anyString(), anyString(), (StatementType) anyObject())).thenReturn(false);
    }
}
