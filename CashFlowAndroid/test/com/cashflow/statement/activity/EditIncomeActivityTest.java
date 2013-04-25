package com.cashflow.statement.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.domain.StatementType.Expense;
import static com.cashflow.domain.StatementType.Income;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
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
import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.service.Balance;
import com.cashflow.statement.activity.edit.EditIncomeActivity;
import com.cashflow.statement.database.AndroidStatementDAO;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class EditIncomeActivityTest {
    private static final String CHANGED_DATE = "2012";
    private static final String CATEGORY_ID = "3";
    private static final String INCOME_ID = "1";
    private static final String NOTE = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final RecurringInterval INTERVAL = RecurringInterval.biweekly;
    private static final RecurringInterval NONE_INTERVAL = RecurringInterval.none;
    private static final Category CATEGORY = Category.builder("category").categoryId(CATEGORY_ID).build();
    private static final Category CHANGED_CATEGORY = Category.builder("changed_category").categoryId("4").build();
    private static final Statement INCOME_STATEMENT = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).statementId(INCOME_ID)
            .category(CATEGORY).recurringInterval(NONE_INTERVAL).build();

    private EditIncomeActivity underTest;
    private final ArrayAdapter<RecurringInterval> intervalArrayAdapter = new ArrayAdapter<RecurringInterval>(underTest,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    @Mock
    private AndroidStatementDAO statementDAO;
    @Mock
    private CategoryDAO categoryDAO;
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
        underTest.setIntent(setUpIntentData(INCOME_STATEMENT));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnCreateWhenEditIncomeActivityThenTitleShouldBeEditIncome() {

        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_incomes)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        final Button button = (Button) underTest.findViewById(R.id.dateButton);
        final ShadowTextView shadowButton = shadowOf(button);
        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testWhenEditIncomeActivityCreatedThenShouldFillUpViewsWithDataFromIntent() {
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
        final CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox_income);
        final Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(NONE_INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(false));
    }

    @Test
    public void testOnCreateWhenRecurringIsOtherThenNoneThenRecurringCheckBoxShouldBeCheckedAndSpinnerValueIsSettedToCorrectValue() {
        final Statement statement = Statement.builder(AMOUNT, DATE).statementId("12").category(CATEGORY).note(NOTE).recurringInterval(INTERVAL)
                .type(Income).build();
        underTest.setIntent(setUpIntentData(statement));
        when(statementDAO.getStatementById("12")).thenReturn(statement);
        underTest.onCreate(null);

        final Spinner interval = (Spinner) underTest.findViewById(R.id.recurring_spinner);
        final CheckBox checkbox = (CheckBox) underTest.findViewById(R.id.recurring_checkbox_income);

        assertThat((RecurringInterval) interval.getSelectedItem(), equalTo(INTERVAL));
        assertThat(checkbox.isChecked(), equalTo(true));
    }

    @Test
    public void testSubmitWhenRecurringIntervalHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).statementId(INCOME_ID).recurringInterval(INTERVAL)
                .category(CATEGORY).build();
        setViewsValues(changedStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedStatement, changedStatement.getStatementId());
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitNothingHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        setViewsValues(INCOME_STATEMENT);

        submit.performClick();

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenRecurringIntervalAndDateHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedStatement = Statement.builder(AMOUNT, CHANGED_DATE).note(NOTE).type(Income).statementId(INCOME_ID)
                .recurringInterval(INTERVAL).category(CATEGORY).build();
        setViewsValues(changedStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedStatement, changedStatement.getStatementId());
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).statementId(INCOME_ID)
                .recurringInterval(NONE_INTERVAL).category(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, changedNoteStatement.getStatementId());
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenSomethingHappendInDBThenShouldShowToastWithDatabaseError() {
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).statementId(INCOME_ID)
                .recurringInterval(NONE_INTERVAL).category(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);
        when(statementDAO.update(changedNoteStatement, INCOME_ID)).thenReturn(false);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, changedNoteStatement.getStatementId());
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(underTest.getString(R.string.database_error)));
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
        intent.putExtra(ID_EXTRA, statement.getStatementId());
        return intent;
    }

    private void addBindings(final ActivityModule module) {
        final Balance balance = Balance.getInstance(statementDAO);
        module.addBinding(Balance.class, balance);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementDAO.class, statementDAO);
    }

    private void setUpPersistentService() {
        final Statement income = Statement.builder(AMOUNT, DATE).note(NOTE).type(Income).category(CATEGORY).recurringInterval(RecurringInterval.none)
                .build();
        final Statement expense = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).category(CATEGORY)
                .recurringInterval(RecurringInterval.none).build();

        final List<Statement> expenses = new ArrayList<Statement>();
        expenses.add(expense);
        final List<Statement> incomes = new ArrayList<Statement>();
        incomes.add(income);

        when(statementDAO.getExpenses()).thenReturn(expenses);
        when(statementDAO.getIncomes()).thenReturn(incomes);
        when(statementDAO.getStatementById(INCOME_ID)).thenReturn(INCOME_STATEMENT);
        when(statementDAO.update((Statement) anyObject(), anyString())).thenReturn(true);
        final List<Category> categories = new ArrayList<Category>();
        categories.add(CATEGORY);

        when(categoryDAO.getAllCategories()).thenReturn(categories);
    }

}
