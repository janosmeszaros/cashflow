package com.cashflow.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.database.statement.StatementType.Expense;
import static com.cashflow.database.statement.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.database.MatrixCursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.EditStatementActivityProvider;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.category.CategoryPersistenceService;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.domain.Category;
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
public class EditStatementActivityTest {
    private static final String CHANGED_NOTE = "changedNote";
    private static final String CHANGED_DATE = "2012";
    private static final String CHANGED_AMOUNT = "123";
    private static final String CATEGORY_ID = "3";
    private static final String INCOME_ID = "1";
    private static final String EXPENSE_ID = "2";
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final RecurringInterval INTERVAL = RecurringInterval.biweekly;
    private static final RecurringInterval NONE_INTERVAL = RecurringInterval.none;
    private static final Category CATEGORY = new Category(CATEGORY_ID, "category");
    private static final Statement INCOME_STATEMENT = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setId(INCOME_ID)
            .setCategory(CATEGORY).setRecurringInterval(NONE_INTERVAL).build();
    private static final Statement EXPENSE_STATEMENT = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Expense).setId(EXPENSE_ID)
            .setCategory(CATEGORY).setRecurringInterval(NONE_INTERVAL).build();

    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, CHANGED_DATE, "note"};
    private EditStatementActivity underTest;
    private final ArrayAdapter<RecurringInterval> intervalArrayAdapter = new ArrayAdapter<RecurringInterval>(underTest,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    private Balance balance;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private CategoryPersistenceService categoryPersistenceService;
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
    public void testOnCreateWhenEditIncomeActivityThenTitleShouldBeEditIncome() {
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_incomes)));
    }

    @Test
    public void testOnCreateWhenEditExpenseActivityThenTitleShouldBeEditExpense() {
        underTest.setIntent(setUpIntentData(EXPENSE_STATEMENT));
        underTest.onCreate(null);

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_expenses)));
    }

    @Test
    public void testOnCreateWhenExpenseThenRecurringSpinnerShouldNotBeShown() {
        underTest.setIntent(setUpIntentData(EXPENSE_STATEMENT));
        underTest.onCreate(null);

        LinearLayout recurring = (LinearLayout) underTest.findViewById(R.id.recurring_income);

        assertThat(recurring.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        Button button = (Button) underTest.findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testWhenEditIncomeActivityCreatedThenShouldFillUpViewsWithDataFromIntent() {
        Statement statement = new Statement.Builder(AMOUNT, DATE).setId(INCOME_ID).setCategory(CATEGORY).setNote(NOTES)
                .setRecurringInterval(NONE_INTERVAL).setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));
        underTest.onCreate(null);

        TextView amount = (TextView) underTest.findViewById(R.id.amountText);
        TextView note = (TextView) underTest.findViewById(R.id.notesText);
        Button date = (Button) underTest.findViewById(R.id.dateButton);

        assertThat(amount.getText().toString(), equalTo(AMOUNT));
        assertThat(note.getText().toString(), equalTo(NOTES));
        assertThat(date.getText().toString(), equalTo(DATE));
    }

    @Test
    public void testOnCreateWhenRecurringIsNoneThenRecurringCheckBoxShouldBeUnchekedAndSpinnerValueIsNone() {
        Statement statement = new Statement.Builder(AMOUNT, DATE).setId(INCOME_ID).setCategory(CATEGORY).setNote(NOTES)
                .setRecurringInterval(NONE_INTERVAL).setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));
        underTest.onCreate(null);

        Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(NONE_INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(false));
    }

    @Test
    public void testOnCreateWhenRecurringIsOtherThenNoneThenRecurringCheckBoxShouldBeCheckedAndSpinnerValueIsSettedToCorrectValue() {
        Statement statement = new Statement.Builder(AMOUNT, DATE).setId("12").setCategory(CATEGORY).setNote(NOTES).setRecurringInterval(INTERVAL)
                .setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));
        when(statementPersistentService.getStatementById("12")).thenReturn(statement);
        underTest.onCreate(null);

        Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(true));
    }

    @Test
    public void testSubmitWhenAmountHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        Statement incomeStatement = new Statement.Builder(CHANGED_AMOUNT, DATE).setNote(NOTES).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(incomeStatement);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(incomeStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenRecurringIntervalHasChangedThenShouldCallProperFunctionAndRefreshBalanceAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        setViewsValues(INCOME_STATEMENT);
        Statement changedStatement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedStatement);

        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(changedStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenDateHasChangedThenShouldCallProperFunctionAAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        Statement changedDateStatement = new Statement.Builder(AMOUNT, CHANGED_DATE).setNote(NOTES).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedDateStatement);
        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(changedDateStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNotesHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        Statement changedNoteStatement = new Statement.Builder(AMOUNT, DATE).setNote(CHANGED_NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedNoteStatement);
        underTest.submit(null);

        verify(statementPersistentService, times(1)).updateStatement(changedNoteStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNothingHasChangedThenCallProperFunctionAndResultCodeShouldBeCanceled() {
        ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);

        setViewsValues(INCOME_STATEMENT);

        underTest.submit(null);

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(Statement statement) {
        EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(statement.getNote());

        EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(statement.getAmount());

        Button dateButton = (Button) underTest.findViewById(R.id.dateButton);
        dateButton.setText(statement.getDate());

        Spinner categorySpinner = (Spinner) underTest.findViewById(R.id.categorySpinner);
        List<Category> categories = new ArrayList<Category>();
        categories.add(statement.getCategory());
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(underTest, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryArrayAdapter);
        int categoryPos = categoryArrayAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);

        Spinner recurringSpinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        recurringSpinner.setAdapter(intervalArrayAdapter);
        int intervalPos = intervalArrayAdapter.getPosition(statement.getRecurringInterval());
        recurringSpinner.setSelection(intervalPos);

        CheckBox checkBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox);
        checkBox.setSelected(false);
        if (intervalPos != intervalArrayAdapter.getPosition(RecurringInterval.none)) {
            checkBox.setChecked(true);
        }

    }

    private Intent setUpIntentData(Statement statement) {
        Intent intent = new Intent();
        intent.putExtra(ID_EXTRA, statement.getId());
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

        when(statementPersistentService.getStatementById(INCOME_ID)).thenReturn(INCOME_STATEMENT);
        when(statementPersistentService.getStatementById(EXPENSE_ID)).thenReturn(EXPENSE_STATEMENT);

        when(statementPersistentService.getStatement(Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement((Statement) anyObject())).thenReturn(true);
        when(statementPersistentService.updateStatement((Statement) anyObject())).thenReturn(true);

        List<Category> categories = new ArrayList<Category>();
        categories.add(CATEGORY);

        when(categoryPersistenceService.getCategories()).thenReturn(categories);
    }

}
