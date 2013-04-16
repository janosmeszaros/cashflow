package com.cashflow.statement.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static com.cashflow.domain.StatementType.Expense;
import static com.cashflow.domain.StatementType.Income;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.times;
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
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.ActionsActivity;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.FragmentProviderWithRoboFragmentActivity;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.cashflow.service.CategoryPersistenceService;
import com.cashflow.service.StatementPersistenceService;
import com.cashflow.statement.activity.AddStatementFragment.CreateCategoryOnClickListener;
import com.cashflow.statement.activity.AddStatementFragment.SubmitButtonOnClickListener;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowImageView;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

/**
 * {@link AddStatementFragment} test, specially this class tests the income adding functionality.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddStatementFragmentTest {

    private static final String ERROR = "Error";
    private static final String NOTES = "notes";
    private static final String DATE = "2013";
    private static final String AMOUNT = "1234";
    private static final String INVALID_AMOUNT = "12";
    private static final Category CATEGORY = new Category("3", "category");
    private static final Statement VALID_STATEMENT = Statement.builder(AMOUNT, DATE).category(CATEGORY).note(NOTES).type(Expense)
            .recurringInterval(RecurringInterval.none).build();
    private static final Statement INVALID_STATEMENT = Statement.builder(INVALID_AMOUNT, DATE).category(CATEGORY).note(NOTES).type(Income)
            .recurringInterval(RecurringInterval.none).build();

    private final String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private final Object[] values = new Object[]{1, 1234L, "2012", "note"};
    private final ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(new ActionsActivity(),
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    private final List<Category> categoryList = new ArrayList<Category>() {
        {
            add(CATEGORY);
        }
    };
    private final ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(new ActionsActivity(),
            android.R.layout.simple_spinner_dropdown_item, categoryList);

    @Inject
    private Activity activity;
    @Mock
    private StatementPersistenceService statementPersistentService;
    @Mock
    private DateButtonOnClickListener listener;
    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Mock
    private CategoryPersistenceService categoryService;

    private AddStatementFragment underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        final ActivityModule module = new ActivityModule(new FragmentProviderWithRoboFragmentActivity(new AddStatementFragment()));
        setUpMocks();
        addBindings(module);
        ActivityModule.setUp(this, module);

        underTest = (AddStatementFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("Fragment");
    }

    @After
    public void tearDown() {
        ActivityModule.tearDown();
    }

    @Test
    public void testOnViewCreatedWhenCalledThenShouldSetTheDateButtonToTheCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final String date = fmtDateAndTime.format(calendar.getTime());
        final Button buttonButton = (Button) underTest.getView().findViewById(R.id.dateButton);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat((String) buttonButton.getText(), equalTo(date));
    }

    @Test
    public void testOnViewCreatedWhenCalledThenShouldSetUpCategorySpinner() {
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        when(categoryService.getCategories()).thenReturn(categoryList);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat((Category) categorySpinner.getAdapter().getItem(0), equalTo(categoryAdapter.getItem(0)));
    }

    @Test
    public void testOnViewCreatedWhenCalledThenShouldSetUpListenersToButtons() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowTextView shadowSubmitButton = Robolectric.shadowOf(submitButton);
        final ImageButton createCategoryButton = (ImageButton) underTest.getView().findViewById(R.id.createCategoryButton);
        final ShadowImageView shadowCreateCategoryButton = Robolectric.shadowOf(createCategoryButton);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat(shadowSubmitButton.getOnClickListener(), instanceOf(SubmitButtonOnClickListener.class));
        assertThat(shadowCreateCategoryButton.getOnClickListener(), instanceOf(CreateCategoryOnClickListener.class));
    }

    @Test
    public void testSubmitValidExpenseThenShouldSetTheResultToOkAndCloseTheActivity() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        setViewsValues(VALID_STATEMENT);
        when(statementPersistentService.saveStatement(VALID_STATEMENT)).thenReturn(true);

        submit.performClick();

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitWhenSomethingWentWrongThenShouldShowAToast() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        setViewsValues(INVALID_STATEMENT);
        when(statementPersistentService.saveStatement(INVALID_STATEMENT)).thenReturn(false);

        submit.performClick();

        final String toastText = activity.getResources().getString(R.string.database_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    @Test
    public void testSubmitWhenSaveStatementThrowsExceptionThenShouldShowAToastWithTheExcpetionMessage() {
        final Button submit = (Button) underTest.getView().findViewById(R.id.submitButton);
        setViewsValues(VALID_STATEMENT);
        when(statementPersistentService.saveStatement(VALID_STATEMENT)).thenThrow(new IllegalArgumentException(ERROR));

        submit.performClick();

        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(ERROR));
    }

    @Test
    public void testCreateCategoryWhenButtonPressedThenStartNewCreateCategoryActivity() {
        final ImageButton createCategoryButton = (ImageButton) underTest.getView().findViewById(R.id.createCategoryButton);
        final ShadowActivity shadowActivity = Robolectric.shadowOf(activity);

        createCategoryButton.performClick();

        final Intent intent = shadowActivity.getNextStartedActivityForResult().intent;
        final ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(CreateCategoryActivity.class.getName()));
    }

    @Test
    public void testOnActivityResultWhenRequestIdIsCorrectThenRefreshSpinner() {
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        final Category addedCat = new Category("1", "cat2");
        final List<Category> extendedList = new ArrayList<Category>(categoryList);
        extendedList.add(addedCat);
        when(categoryService.getCategories()).thenReturn(extendedList);

        underTest.onActivityResult(1, Activity.RESULT_OK, null);

        verify(categoryService, times(2)).getCategories();
        assertThat((Category) categorySpinner.getSelectedItem(), equalTo(addedCat));
    }

    @Test
    public void testOnActivityResultWhenRequestIdIsNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(0, Activity.RESULT_OK, null);

        // once, on creation
        verify(categoryService, times(1)).getCategories();
    }

    @Test
    public void testOnActivityResultWhenResultCodeIsNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(1, Activity.RESULT_CANCELED, null);

        // once, on creation
        verify(categoryService, times(1)).getCategories();
    }

    @Test
    public void testOnActivityResultWhenResultCodeAndRequestIdAreNotCorrectThenShouldNotRefreshSpinner() {
        underTest.onActivityResult(0, Activity.RESULT_CANCELED, null);

        // once, on creation
        verify(categoryService, times(1)).getCategories();
    }

    private void setViewsValues(final Statement statement) {
        final EditText notes = (EditText) underTest.getView().findViewById(R.id.notesText);
        final EditText amount = (EditText) underTest.getView().findViewById(R.id.amountText);
        final Button button = (Button) underTest.getView().findViewById(R.id.dateButton);
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);

        notes.setText(statement.getNote());
        amount.setText(statement.getAmount());
        button.setText(statement.getDate());

        categorySpinner.setAdapter(categoryAdapter);
        final int categoryPos = categoryAdapter.getPosition(statement.getCategory());
        categorySpinner.setSelection(categoryPos);
    }

    private void addBindings(final ActivityModule module) {
        module.addBinding(DateButtonOnClickListener.class, listener);
        module.addBinding(StatementPersistenceService.class, statementPersistentService);
        module.addBinding(SpinnerAdapter.class, arrayAdapter);
        module.addBinding(RecurringCheckBoxOnClickListener.class, checkBoxListener);
        module.addBinding(CategoryPersistenceService.class, categoryService);
    }

    private void setUpMocks() {
        final MatrixCursor cursor = new MatrixCursor(fromColumns);
        cursor.addRow(values);

        when(statementPersistentService.getAllStatementsByType(StatementType.Expense)).thenReturn(cursor);
        when(statementPersistentService.getAllStatementsByType(StatementType.Income)).thenReturn(cursor);
    }
}
