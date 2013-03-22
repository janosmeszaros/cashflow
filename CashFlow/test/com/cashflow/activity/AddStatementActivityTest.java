package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.statement.StatementType.Expense;
import static com.cashflow.database.statement.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
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
public class AddStatementActivityTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private static final Category CATEGORY = new Category("3", "category");
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
    }

    private void createAddIncome() {
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, Income.toString()));
        underTest.onCreate(null);
    }

    private void createAddExpense() {
        underTest.setIntent(new Intent().putExtra(STATEMENT_TYPE_EXTRA, Expense.toString()));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnCreateWhenTypeIsIncomeThenTitleShouldBeAddIncome() {
        createAddIncome();
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_add_income)));
    }

    @Test
    public void testOnCreateWhenTypeIsExpenseThenTitleShouldBeAddExpense() {
        createAddExpense();
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_add_expense)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheDateButtonToTheCurrentDate() {
        createAddIncome();
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Button buttonButton = (Button) underTest.findViewById(R.id.dateButton);
        String date = fmtDateAndTime.format(calendar.getTime());

        assertThat((String) buttonButton.getText(), equalTo(date));
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
        createAddIncome();
        LinearLayout recurringArea = (LinearLayout) underTest.findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.VISIBLE));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringCheckBoxOnClickListenerShouldBeRecurringCheckBoxOnClickListener() {
        createAddIncome();
        CheckBox recurringCheckBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);
        ShadowTextView shadowTextView = Robolectric.shadowOf(recurringCheckBox);

        assertThat((RecurringCheckBoxOnClickListener) shadowTextView.getOnClickListener(), equalTo(checkBoxListener));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnCreateWhenCalledThenRecurringSpinnersAdapterShouldBeSettedToArrayAdapterWithValuesFromRecurringInterval() {
        createAddIncome();
        Spinner recurringSpinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);

        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        createAddIncome();
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testSubmitWhenIncomeIsNotRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        Statement statement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setCategory(CATEGORY)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitValidIncomeThenShouldSetTheResultToOkAndCloseTheActivity() {
        createAddIncome();
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitValidExpenseThenShouldSetTheResultToOkAndCloseTheActivity() {
        createAddExpense();
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Expense)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringAreaShouldBeGone() {
        createAddExpense();
        LinearLayout recurringArea = (LinearLayout) underTest.findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.GONE));
    }

    @Test
    public void testSubmitWhenIncomeIsRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.biweekly).build();
        setViewsValues(statement);

        underTest.submit(null);

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        createAddIncome();
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest);
        Statement statement = new Statement.Builder(INVALID_AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);

        underTest.submit(null);

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_CANCELED));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(Statement statement) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(statement.getNote());
        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(statement.getAmount());
        Button button = (Button) underTest.findViewById(R.id.dateButton);
        button.setText(statement.getDate());
        Spinner spinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        spinner.setAdapter(arrayAdapter);
        int selection = arrayAdapter.getPosition(statement.getRecurringInterval());
        spinner.setSelection(selection);

        Spinner categorySpinner = (Spinner) underTest.findViewById(R.id.categorySpinner);
        List<Category> categories = new ArrayList<Category>();
        categories.add(statement.getCategory());
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(underTest, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryArrayAdapter);
        int categoryPos = categoryArrayAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);

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

        Statement statement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setCategory(CATEGORY)
                .setRecurringInterval(RecurringInterval.none).build();
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);
        when(statementPersistentService.saveStatement(statement)).thenReturn(false);
    }
}
