package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.statement.database.StatementType.Expense;
import static com.cashflow.statement.database.StatementType.Income;
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
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.ActionsActivity;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.AddStatementFragmentProvider;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.balance.Balance;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowTextView;

/**
 * {@link AddStatementFragment} test, specially this class tests the income adding functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddStatementFragmentTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private static final Category CATEGORY = new Category("3", "category");
    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private Balance balance;
    private ArrayAdapter<RecurringInterval> arrayAdapter;
    private AddStatementFragment underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;
    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        arrayAdapter = new ArrayAdapter<RecurringInterval>(new ActionsActivity(), android.R.layout.simple_spinner_dropdown_item,
                RecurringInterval.values());

        ActivityModule module = new ActivityModule(new AddStatementFragmentProvider());

        setUpMocks();
        addBindings(module);

        ActivityModule.setUp(this, module);
    }

    private void createAddIncome() {
        Bundle bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, Income.toString());
        underTest = new AddStatementFragment();

        underTest.setArguments(bundle);

        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(underTest, null);
        fragmentTransaction.commit();

    }

    private void createAddExpense() {
        Bundle bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, Expense.toString());
        underTest.setArguments(bundle);
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheDateButtonToTheCurrentDate() {
        createAddIncome();
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Button buttonButton = (Button) underTest.getView().findViewById(R.id.dateButton);
        String date = fmtDateAndTime.format(calendar.getTime());

        assertThat((String) buttonButton.getText(), equalTo(date));
    }

    @Test
    public void testOnCreateWhenCalledThenRercurringAreaShouldBeVisible() {
        createAddIncome();
        LinearLayout recurringArea = (LinearLayout) underTest.getActivity().findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.VISIBLE));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringCheckBoxOnClickListenerShouldBeRecurringCheckBoxOnClickListener() {
        createAddIncome();
        CheckBox recurringCheckBox = (CheckBox) underTest.getActivity().findViewById(R.id.recurring_checkbox_income);
        ShadowTextView shadowTextView = Robolectric.shadowOf(recurringCheckBox);

        assertThat((RecurringCheckBoxOnClickListener) shadowTextView.getOnClickListener(), equalTo(checkBoxListener));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnCreateWhenCalledThenRecurringSpinnersAdapterShouldBeSettedToArrayAdapterWithValuesFromRecurringInterval() {
        createAddIncome();
        Spinner recurringSpinner = (Spinner) underTest.getActivity().findViewById(R.id.recurring_spinner);

        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        createAddIncome();
        Button button = (Button) underTest.getActivity().findViewById(R.id.dateButton);
        ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testSubmitWhenIncomeIsNotRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        Button submit = (Button) underTest.getActivity().findViewById(R.id.submitButton);
        Statement statement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setCategory(CATEGORY)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);

        submit.callOnClick();

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitValidIncomeThenShouldSetTheResultToOkAndCloseTheActivity() {
        createAddIncome();
        Button submit = (Button) underTest.getActivity().findViewById(R.id.submitButton);
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        submit.callOnClick();

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitValidExpenseThenShouldSetTheResultToOkAndCloseTheActivity() {
        createAddExpense();
        Button submit = (Button) underTest.getActivity().findViewById(R.id.submitButton);
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Expense)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        submit.callOnClick();

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringAreaShouldBeGone() {
        createAddExpense();
        LinearLayout recurringArea = (LinearLayout) underTest.getActivity().findViewById(R.id.recurring_income);

        assertThat(recurringArea.getVisibility(), equalTo(LinearLayout.GONE));
    }

    @Test
    public void testSubmitWhenIncomeIsRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        Button submit = (Button) underTest.getActivity().findViewById(R.id.submitButton);
        Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.biweekly).build();
        setViewsValues(statement);

        submit.callOnClick();

        verify(statementPersistentService).saveStatement(statement);
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        createAddIncome();
        Button submit = (Button) underTest.getActivity().findViewById(R.id.submitButton);
        ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        Statement statement = new Statement.Builder(INVALID_AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);

        submit.callOnClick();

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_CANCELED));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    private void setViewsValues(Statement statement) {
        EditText notes = (EditText) underTest.getActivity().findViewById(R.id.notesText);
        notes.setText(statement.getNote());
        EditText amount = (EditText) underTest.getActivity().findViewById(R.id.amountText);
        amount.setText(statement.getAmount());
        Button button = (Button) underTest.getActivity().findViewById(R.id.dateButton);
        button.setText(statement.getDate());
        Spinner spinner = (Spinner) underTest.getActivity().findViewById(R.id.recurring_spinner);
        spinner.setAdapter(arrayAdapter);
        int selection = arrayAdapter.getPosition(statement.getRecurringInterval());
        spinner.setSelection(selection);

        Spinner categorySpinner = (Spinner) underTest.getActivity().findViewById(R.id.categorySpinner);
        List<Category> categories = new ArrayList<Category>();
        categories.add(statement.getCategory());
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(underTest.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryArrayAdapter);
        int categoryPos = categoryArrayAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);

        if (selection != 0) {
            CheckBox recurringCheckBox = (CheckBox) underTest.getActivity().findViewById(R.id.recurring_checkbox_income);
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
