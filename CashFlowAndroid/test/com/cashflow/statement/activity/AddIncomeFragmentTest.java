package com.cashflow.statement.activity;

import static com.cashflow.domain.StatementType.Expense;
import static com.cashflow.domain.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.ActionsActivity;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.FragmentProviderWithRoboFragmentActivity;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class AddIncomeFragmentTest {

    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private static final Category CATEGORY = Category.builder("category").categoryId("3").build();
    private final ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(new ActionsActivity(),
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    @Inject
    private Activity activity;
    @Mock
    private StatementDAO statementDAO;
    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;
    private AddIncomeFragment underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        final ActivityModule module = new ActivityModule(new FragmentProviderWithRoboFragmentActivity(new AddIncomeFragment()));

        setUpMocks();
        addBindings(module);

        ActivityModule.setUp(this, module);
        underTest = (AddIncomeFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("Fragment");
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnViewCreatedWhenCalledThenShouldSetUpRecurringArea() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_income);
        final ShadowTextView shadowCheckBox = Robolectric.shadowOf(recurringCheckBox);
        final LinearLayout recurringArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_income);
        final Spinner recurringSpinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat((RecurringCheckBoxOnClickListener) shadowCheckBox.getOnClickListener(), equalTo(checkBoxListener));
        assertThat(recurringArea.getVisibility(), equalTo(View.VISIBLE));
        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testSubmitWhenIncomeIsNotRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        final Statement statement = Statement.builder(AMOUNT, DATE).note(NOTES).type(Income).category(CATEGORY)
                .recurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementDAO.save(statement)).thenReturn(true);

        shadowButton.performClick();

        verify(statementDAO).save(statement);
        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenIncomeIsRecurringThenShouldCallSaveStatementWithCorrectStatement() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final ShadowFragmentActivity shadowActivity = Robolectric.shadowOf(underTest.getActivity());
        final Statement statement = Statement.builder(AMOUNT, DATE).category(CATEGORY).note(NOTES).type(Income)
                .recurringInterval(RecurringInterval.biweekly).build();
        setViewsValues(statement);
        when(statementDAO.save(statement)).thenReturn(true);

        shadowButton.performClick();

        verify(statementDAO).save(statement);
        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldSetTheResultToCanceledAndCloseTheActivity() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        final Statement statement = Statement.builder(INVALID_AMOUNT, DATE).category(CATEGORY).note(NOTES).type(Income)
                .recurringInterval(RecurringInterval.none).build();
        setViewsValues(statement);
        when(statementDAO.save(statement)).thenReturn(false);

        shadowButton.performClick();

        final String toastText = activity.getResources().getString(R.string.database_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    @Test
    public void testOnResumeWhenRecurringCheckBoxIsCheckedThenRecurringCheckBoxAreaShouldBeVisible() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_income);
        final LinearLayout recurringCheckBoxArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_checkbox_area_income);
        recurringCheckBox.setChecked(true);

        underTest.onResume();

        assertThat(recurringCheckBoxArea.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void testOnResumeWhenRecurringCheckBoxIsNotCheckedThenRecurringCheckBoxAreaShouldBeInVisible() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_income);
        final LinearLayout recurringCheckBoxArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_checkbox_area_income);
        recurringCheckBox.setChecked(false);

        underTest.onResume();

        assertThat(recurringCheckBoxArea.getVisibility(), equalTo(View.INVISIBLE));
    }

    private void setViewsValues(final Statement statement) {
        final EditText notes = (EditText) underTest.getView().findViewById(R.id.notesText);
        final EditText amount = (EditText) underTest.getView().findViewById(R.id.amountText);
        final Button button = (Button) underTest.getView().findViewById(R.id.dateButton);
        final Spinner spinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);

        notes.setText(statement.getNote());
        amount.setText(statement.getAmount());
        button.setText(statement.getDate());

        spinner.setAdapter(arrayAdapter);
        final int selection = arrayAdapter.getPosition(statement.getRecurringInterval());
        spinner.setSelection(selection);

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
        module.addBinding(StatementDAO.class, statementDAO);
        module.addBinding(SpinnerAdapter.class, arrayAdapter);
        module.addBinding(RecurringCheckBoxOnClickListener.class, checkBoxListener);
    }

    private void setUpMocks() {
        final Statement income = Statement.builder(AMOUNT, DATE).note(NOTES).type(Income).category(CATEGORY)
                .recurringInterval(RecurringInterval.none).build();
        final Statement expense = Statement.builder(AMOUNT, DATE).note(NOTES).type(Expense).category(CATEGORY)
                .recurringInterval(RecurringInterval.none).build();

        final List<Statement> expenses = new ArrayList<Statement>();
        expenses.add(expense);
        final List<Statement> incomes = new ArrayList<Statement>();
        incomes.add(income);
        when(statementDAO.getExpenses()).thenReturn(expenses);
        when(statementDAO.getIncomes()).thenReturn(incomes);
    }
}
