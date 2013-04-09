package com.cashflow.statement.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.statement.database.StatementType.Income;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
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

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.EditStatementActivityProvider;
import com.cashflow.category.database.CategoryPersistenceService;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class EditIncomeActivityTest {
    private static final String CHANGED_NOTE = "changedNote";
    private static final String CHANGED_DATE = "2012";
    private static final String CHANGED_AMOUNT = "123";
    private static final String CATEGORY_ID = "3";
    private static final String INCOME_ID = "1";
    private static final String NOTE = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final RecurringInterval INTERVAL = RecurringInterval.biweekly;
    private static final RecurringInterval NONE_INTERVAL = RecurringInterval.none;
    private static final Category CATEGORY = new Category(CATEGORY_ID, "category");
    private static final Category CHANGED_CATEGORY = new Category("4", "changed_category");
    private static final Statement INCOME_STATEMENT = new Statement.Builder(AMOUNT, DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
            .setCategory(CATEGORY).setRecurringInterval(NONE_INTERVAL).build();

    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, CHANGED_DATE, "note"};
    private EditStatementActivity underTest;
    private final ArrayAdapter<RecurringInterval> intervalArrayAdapter = new ArrayAdapter<RecurringInterval>(underTest,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private CategoryPersistenceService categoryPersistenceService;
    @Mock
    private DateButtonOnClickListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new EditStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new EditIncomeActivity();
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
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));

        underTest.onCreate(null);

        final Button button = (Button) underTest.findViewById(R.id.dateButton);
        final ShadowTextView shadowButton = shadowOf(button);
        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testWhenEditIncomeActivityCreatedThenShouldFillUpViewsWithDataFromIntent() {
        final Statement statement = new Statement.Builder(AMOUNT, DATE).setId(INCOME_ID).setCategory(CATEGORY).setNote(NOTE)
                .setRecurringInterval(NONE_INTERVAL).setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));

        underTest.onCreate(null);

        final TextView amount = (TextView) underTest.findViewById(R.id.amountText);
        final ShadowTextView amountShadow = shadowOf(amount);
        final TextView note = (TextView) underTest.findViewById(R.id.notesText);
        final ShadowTextView noteShadow = shadowOf(note);
        final Button date = (Button) underTest.findViewById(R.id.dateButton);
        final ShadowButton dateShadow = (ShadowButton) shadowOf(date);
        assertThat(amountShadow.getText().toString(), equalTo(AMOUNT));
        assertThat(noteShadow.getText().toString(), equalTo(NOTE));
        assertThat(dateShadow.getText().toString(), equalTo(DATE));
    }

    @Test
    public void testOnCreateWhenRecurringIsNoneThenRecurringCheckBoxShouldBeUnchekedAndSpinnerValueIsNone() {
        final Statement statement = new Statement.Builder(AMOUNT, DATE).setId(INCOME_ID).setCategory(CATEGORY).setNote(NOTE)
                .setRecurringInterval(NONE_INTERVAL).setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));

        underTest.onCreate(null);

        final CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox_income);
        final Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(NONE_INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(false));
    }

    @Test
    public void testOnCreateWhenRecurringIsOtherThenNoneThenRecurringCheckBoxShouldBeCheckedAndSpinnerValueIsSettedToCorrectValue() {
        final Statement statement = new Statement.Builder(AMOUNT, DATE).setId("12").setCategory(CATEGORY).setNote(NOTE)
                .setRecurringInterval(INTERVAL).setType(Income).build();
        underTest.setIntent(setUpIntentData(statement));
        when(statementPersistentService.getStatementById("12")).thenReturn(statement);
        underTest.onCreate(null);

        final Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        final CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox_income);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(true));
    }

    @Test
    public void testSubmitWhenAmountHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        final Statement incomeStatement = new Statement.Builder(CHANGED_AMOUNT, DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(incomeStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(incomeStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenRecurringIntervalHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        setViewsValues(INCOME_STATEMENT);
        final Statement changedStatement = new Statement.Builder(AMOUNT, DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(changedStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitExpenseWhenNothingWasChangedThenShouldCallProperFunctionAndResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        setViewsValues(INCOME_STATEMENT);
        final Statement changedStatement = new Statement.Builder(AMOUNT, DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedStatement);

        shadowButton.performClick();

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenDateHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        final Statement changedDateStatement = new Statement.Builder(AMOUNT, CHANGED_DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedDateStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(changedDateStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNoteHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);

        final Statement changedNoteStatement = new Statement.Builder(AMOUNT, DATE).setNote(CHANGED_NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CATEGORY).build();
        setViewsValues(changedNoteStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(changedNoteStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);

        final Statement changedNoteStatement = new Statement.Builder(AMOUNT, DATE).setNote(NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(changedNoteStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedButTheSaveFailedThenResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        when(statementPersistentService.updateStatement((Statement) anyObject())).thenReturn(false);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        final Statement changedNoteStatement = new Statement.Builder(AMOUNT, DATE).setNote(CHANGED_NOTE).setType(Income).setId(INCOME_ID)
                .setRecurringInterval(NONE_INTERVAL).setCategory(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);

        shadowButton.performClick();

        verify(statementPersistentService, times(1)).updateStatement(changedNoteStatement);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        final Activity activity = new Activity();
        final String toastText = activity.getResources().getString(R.string.database_error);
        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    @Test
    public void testSubmitWhenNothingHasChangedThenCallProperFunctionAndResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) shadowOf(submit);
        setViewsValues(INCOME_STATEMENT);

        shadowButton.performClick();

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(final Statement statement) {
        final EditText notes = (EditText) underTest.findViewById(R.id.notesText);
        notes.setText(statement.getNote());
        final EditText amount = (EditText) underTest.findViewById(R.id.amountText);
        amount.setText(statement.getAmount());
        final Button dateButton = (Button) underTest.findViewById(R.id.dateButton);
        dateButton.setText(statement.getDate());
        final Spinner categorySpinner = (Spinner) underTest.findViewById(R.id.categorySpinner);
        final List<Category> categories = new ArrayList<Category>();
        categories.add(statement.getCategory());
        final ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(underTest, android.R.layout.simple_spinner_dropdown_item,
                categories);
        categorySpinner.setAdapter(categoryArrayAdapter);
        final int categoryPos = categoryArrayAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);

        final Spinner recurringSpinner = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        recurringSpinner.setAdapter(intervalArrayAdapter);
        final int intervalPos = intervalArrayAdapter.getPosition(statement.getRecurringInterval());
        recurringSpinner.setSelection(intervalPos);

        final CheckBox checkBox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox_income);
        checkBox.setSelected(false);
        if (intervalPos != intervalArrayAdapter.getPosition(RecurringInterval.none)) {
            checkBox.setChecked(true);
        }

    }

    private Intent setUpIntentData(final Statement statement) {
        final Intent intent = new Intent();
        intent.putExtra(ID_EXTRA, statement.getId());
        return intent;
    }

    private void addBindings(final ActivityModule module) {
        final Balance balance = Balance.getInstance(statementPersistentService);
        module.addBinding(Balance.class, balance);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
    }

    private void setUpPersistentService() {
        final MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);
        when(statementPersistentService.getStatementById(INCOME_ID)).thenReturn(INCOME_STATEMENT);
        //        when(statementPersistentService.getStatementById(EXPENSE_ID)).thenReturn(EXPENSE_STATEMENT);
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(Income)).thenReturn(cursor);
        when(statementPersistentService.saveStatement((Statement) anyObject())).thenReturn(true);
        when(statementPersistentService.updateStatement((Statement) anyObject())).thenReturn(true);

        final List<Category> categories = new ArrayList<Category>();
        categories.add(CATEGORY);

        when(categoryPersistenceService.getCategories()).thenReturn(categories);
    }

}