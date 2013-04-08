package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class AddIncomeFragmentTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private static final Category CATEGORY = new Category("3", "category");
    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private ArrayAdapter<RecurringInterval> arrayAdapter;

    private AddIncomeFragment underTest;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;
    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;
    private ActionsActivity actionsActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        arrayAdapter = new ArrayAdapter<RecurringInterval>(new ActionsActivity(), android.R.layout.simple_spinner_dropdown_item,
                RecurringInterval.values());

        final ActivityModule module = new ActivityModule(new AddStatementFragmentProvider());
        actionsActivity = new ActionsActivity();

        setUpMocks();
        addBindings(module);

        ActivityModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    private void createAddIncome() {
        final Bundle bundle = new Bundle();
        bundle.putString(STATEMENT_TYPE_EXTRA, Income.toString());
        underTest = new AddIncomeFragment();
        underTest.setArguments(bundle);

        final FragmentManager fragmentManager = actionsActivity.getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(underTest, null);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheDateButtonToTheCurrentDate() {
        createAddIncome();
        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final Button buttonButton = (Button) underTest.getView().findViewById(R.id.dateButton);
        final String date = fmtDateAndTime.format(calendar.getTime());

        assertThat((String) buttonButton.getText(), equalTo(date));
    }

    @Test
    public void testOnCreateWhenCalledThenRecurringCheckBoxOnClickListenerShouldBeRecurringCheckBoxOnClickListener() {
        createAddIncome();
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_income);
        final ShadowTextView shadowTextView = Robolectric.shadowOf(recurringCheckBox);

        assertThat((RecurringCheckBoxOnClickListener) shadowTextView.getOnClickListener(), equalTo(checkBoxListener));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnCreateWhenCalledThenRecurringSpinnersAdapterShouldBeSettedToArrayAdapterWithValuesFromRecurringInterval() {
        createAddIncome();
        final Spinner recurringSpinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);

        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testSubmitWhenIncomeIsNotRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        final Statement statement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setCategory(CATEGORY)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        shadowButton.performClick();

        verify(statementPersistentService).saveStatement(statement);
        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassToTheDateButton() {
        createAddIncome();
        final Button button = (Button) underTest.getView().findViewById(R.id.dateButton);
        final ShadowTextView shadowButton = Robolectric.shadowOf(button);

        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
    }

    @Test
    public void testSubmitWhenIncomeIsRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        createAddIncome();
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        final Statement statement = new Statement.Builder(AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.biweekly).build();
        setViewsValues(statement);
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);

        shadowButton.performClick();

        verify(statementPersistentService).saveStatement(statement);
        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        createAddIncome();
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final Statement statement = new Statement.Builder(INVALID_AMOUNT, DATE).setCategory(CATEGORY).setNote(NOTES).setType(Income)
                .setRecurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);

        shadowButton.performClick();

        final String toastText = actionsActivity.getResources().getString(R.string.database_error);
        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    private void setViewsValues(final Statement statement) {
        final EditText notes = (EditText) underTest.getView().findViewById(R.id.notesText);
        notes.setText(statement.getNote());
        final EditText amount = (EditText) underTest.getView().findViewById(R.id.amountText);
        amount.setText(statement.getAmount());
        final Button button = (Button) underTest.getView().findViewById(R.id.dateButton);
        button.setText(statement.getDate());

        final Spinner spinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);
        spinner.setAdapter(arrayAdapter);
        final int selection = arrayAdapter.getPosition(statement.getRecurringInterval());
        spinner.setSelection(selection);

        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        final List<Category> categories = new ArrayList<Category>();
        categories.add(statement.getCategory());
        final ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(underTest.getView().getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);
        final int categoryPos = categoryAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);

        if (selection != 0) {
            final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_income);
            recurringCheckBox.setChecked(true);
        }
    }

    private void addBindings(final ActivityModule module) {
        final Balance balance = Balance.getInstance(statementPersistentService);

        module.addBinding(Balance.class, balance);
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
        module.addBinding(RecurringCheckBoxOnClickListener.class, checkBoxListener);
        module.addBinding(SpinnerAdapter.class, arrayAdapter);
    }

    private void setUpMocks() {
        final MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(cursor);

        final Statement statement = new Statement.Builder(AMOUNT, DATE).setNote(NOTES).setType(Income).setCategory(CATEGORY)
                .setRecurringInterval(RecurringInterval.none).build();
        when(statementPersistentService.saveStatement(statement)).thenReturn(true);
        when(statementPersistentService.saveStatement(statement)).thenReturn(false);
    }
}
