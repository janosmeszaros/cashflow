package com.cashflow.statement.activity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.domain.StatementType.Expense;
import static com.cashflow.domain.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
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

import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.EditStatementActivityProvider;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.category.database.AndroidCategoryDAO;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.statement.activity.EditStatementActivity.CreateCategoryOnClickListener;
import com.cashflow.statement.activity.EditStatementActivity.SubmitButtonOnClickListener;
import com.cashflow.statement.database.AndroidStatementDAO;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;
import com.xtremelabs.robolectric.shadows.ShadowImageView;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

/**
 * {@link EditStatementActivity} test, specially this class tests the income editing functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class EditStatementActivityTest {
    private static final String ERROR = "Error";
    private static final String CHANGED_NOTE = "changedNote";
    private static final String CHANGED_DATE = "2012";
    private static final String CHANGED_AMOUNT = "123";
    private static final String CATEGORY_ID = "3";
    private static final String EXPENSE_ID = "2";
    private static final String NOTE = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final RecurringInterval NONE_INTERVAL = RecurringInterval.none;
    private static final Category CATEGORY = Category.builder("category").categoryId(CATEGORY_ID).build();
    private static final Category CHANGED_CATEGORY = Category.builder("changed_category").categoryId("4").build();
    private static final Statement EXPENSE_STATEMENT = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
            .category(CATEGORY).recurringInterval(NONE_INTERVAL).build();

    private final List<Category> categories = new ArrayList<Category>() {
        {
            add(CATEGORY);
        }
    };
    private EditStatementActivity underTest;

    @Mock
    private AndroidStatementDAO statementDAO;
    @Mock
    private AndroidCategoryDAO categoryDAO;
    @Mock
    private DateButtonOnClickListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new EditStatementActivityProvider());

        setUpPersistentService();
        addBindings(module);

        ActivityModule.setUp(this, module);

        underTest = new EditStatementActivity();
        underTest.setIntent(setUpIntentData(EXPENSE_STATEMENT));
        underTest.onCreate(null);
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnCreateWhenEditExpenseActivityThenTitleShouldBeEditExpense() {
        assertThat((String) underTest.getTitle(), equalTo(underTest.getString(R.string.title_activity_edit_expenses)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTheListenerClassesToButtons() {
        final Button button = (Button) underTest.findViewById(R.id.dateButton);
        final ShadowTextView shadowButton = Robolectric.shadowOf(button);
        final ImageButton createCategoryButton = (ImageButton) underTest.findViewById(R.id.createCategoryButton);
        final ShadowImageView shadowCreateCategoryButton = Robolectric.shadowOf(createCategoryButton);
        final Button submitButton = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowTextView shadowSubmitButton = Robolectric.shadowOf(submitButton);
        assertThat((DateButtonOnClickListener) shadowButton.getOnClickListener(), equalTo(listener));
        assertThat(shadowCreateCategoryButton.getOnClickListener(), instanceOf(CreateCategoryOnClickListener.class));
        assertThat(shadowSubmitButton.getOnClickListener(), instanceOf(SubmitButtonOnClickListener.class));
    }

    @Test
    public void testOnCreateShouldFillUpViewsWithDataFromIntent() {
        final TextView amount = (TextView) underTest.findViewById(R.id.amountText);
        final TextView note = (TextView) underTest.findViewById(R.id.notesText);
        final Button date = (Button) underTest.findViewById(R.id.dateButton);
        final Spinner categorySpinner = (Spinner) underTest.findViewById(R.id.categorySpinner);
        assertThat(amount.getText().toString(), equalTo(AMOUNT));
        assertThat(note.getText().toString(), equalTo(NOTE));
        assertThat(date.getText().toString(), equalTo(DATE));
        assertThat((Category) categorySpinner.getSelectedItem(), equalTo(CATEGORY));
    }

    @Test
    public void testSubmitWhenAmountHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement expenseStatement = Statement.builder(CHANGED_AMOUNT, DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CATEGORY).build();
        setViewsValues(expenseStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(expenseStatement, EXPENSE_ID);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitExpenseWhenNothingWasChangedThenShouldCallProperFunctionAndResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        setViewsValues(EXPENSE_STATEMENT);
        final Statement changedStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CATEGORY).build();
        setViewsValues(changedStatement);

        submit.performClick();

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenDateHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedDateStatement = Statement.builder(AMOUNT, CHANGED_DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CATEGORY).build();
        setViewsValues(changedDateStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedDateStatement, EXPENSE_ID);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenNoteHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(CHANGED_NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CATEGORY).build();
        setViewsValues(changedNoteStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, EXPENSE_ID);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedThenShouldCallProperFunctionAndResultCodeShouldBeOK() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, EXPENSE_ID);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_OK));
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedButTheSaveFailedThenResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        when(statementDAO.update((Statement) anyObject(), anyString())).thenReturn(false);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(CHANGED_NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, EXPENSE_ID);
        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        final String toastText = underTest.getResources().getString(R.string.database_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    @Test
    public void testSubmitWhenNothingHasChangedThenCallProperFunctionAndResultCodeShouldBeCanceled() {
        final ShadowFragmentActivity shadowFragmentActivity = Robolectric.shadowOf(underTest);
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final ShadowButton shadowButton = (ShadowButton) Robolectric.shadowOf(submit);
        setViewsValues(EXPENSE_STATEMENT);

        shadowButton.performClick();

        assertThat(shadowFragmentActivity.getResultCode(), equalTo(RESULT_CANCELED));
        assertThat(shadowFragmentActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenCategoryHasChangedButUpdateStatementThrowExceptionThenShouldShowToast() {
        final Button submit = (Button) underTest.findViewById(R.id.submitButton);
        final Statement changedNoteStatement = Statement.builder(AMOUNT, DATE).note(NOTE).type(Expense).statementId(EXPENSE_ID)
                .recurringInterval(NONE_INTERVAL).category(CHANGED_CATEGORY).build();
        setViewsValues(changedNoteStatement);
        when(statementDAO.update(changedNoteStatement, EXPENSE_ID)).thenThrow(new IllegalArgumentException(ERROR));

        submit.performClick();

        verify(statementDAO, times(1)).update(changedNoteStatement, EXPENSE_ID);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(ERROR));
    }

    @Test
    public void testCreateCategoryWhenButtonPressedThenStartNewCreateCategoryActivity() {
        final ImageButton createCategoryButton = (ImageButton) underTest.findViewById(R.id.createCategoryButton);
        final ShadowActivity shadowActivity = Robolectric.shadowOf(underTest);

        createCategoryButton.performClick();

        final Intent intent = shadowActivity.getNextStartedActivityForResult().intent;
        final ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(CreateCategoryActivity.class.getName()));
    }

    @Test
    public void testOnActivityResultWhenRequestIdIsCorrectThenRefreshSpinner() {
        final Spinner categorySpinner = (Spinner) underTest.findViewById(R.id.categorySpinner);
        final Category addedCat = Category.builder("cat2").categoryId("1").build();
        final List<Category> extendedList = new ArrayList<Category>(categories);
        extendedList.add(addedCat);
        when(categoryDAO.getAllCategories()).thenReturn(extendedList);

        underTest.onActivityResult(1, Activity.RESULT_OK, null);

        verify(categoryDAO, times(2)).getAllCategories();
        assertThat((Category) categorySpinner.getSelectedItem(), equalTo(addedCat));
    }

    @Test
    public void testOnActivityResultWhenRequestIdIsNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(0, Activity.RESULT_OK, null);

        // once, on creation
        verify(categoryDAO, times(1)).getAllCategories();
    }

    @Test
    public void testOnActivityResultWhenResultCodeIsNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(1, Activity.RESULT_CANCELED, null);

        // once, on creation
        verify(categoryDAO, times(1)).getAllCategories();
    }

    @Test
    public void testOnActivityResultWhenResultCodeAndRequestIdAreNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(0, Activity.RESULT_CANCELED, null);

        // once, on creation
        verify(categoryDAO, times(1)).getAllCategories();
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
    }

    private Intent setUpIntentData(final Statement statement) {
        final Intent intent = new Intent();
        intent.putExtra(ID_EXTRA, statement.getId());
        return intent;
    }

    private void addBindings(final ActivityModule module) {
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementDAO.class, statementDAO);
        module.addBinding(CategoryDAO.class, categoryDAO);
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

        when(statementDAO.getStatementById(EXPENSE_ID)).thenReturn(EXPENSE_STATEMENT);
        when(statementDAO.getExpenses()).thenReturn(expenses);
        when(statementDAO.getIncomes()).thenReturn(incomes);
        when(statementDAO.save((Statement) anyObject())).thenReturn(true);
        when(statementDAO.update((Statement) anyObject(), anyString())).thenReturn(true);

        when(categoryDAO.getAllCategories()).thenReturn(categories);
    }

}
