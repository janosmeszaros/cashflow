package com.cashflow.bill.activity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.ActionsActivity;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.FragmentProviderWithRoboFragmentActivity;
import com.cashflow.bill.database.AndroidBillDAO;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.category.database.AndroidCategoryDAO;
import com.cashflow.dao.BillDAO;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Bill;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Bill.Builder;
import com.cashflow.domain.Category;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class AddBillFragmentTest {
    private static final String EXCEPTION = "Exception";

    private static final Category CATEGORY = Category.builder("cat").categoryId("0").build();

    @Inject
    private Activity activity;

    @Mock
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Mock
    private AndroidCategoryDAO categoryDAO;
    @Mock
    private AndroidBillDAO billDAO;

    private AddBillFragment underTest;
    private final List<Category> categoryList = new ArrayList<Category>() {
        {
            add(CATEGORY);
        }
    };
    private final ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(new ActionsActivity(),
            android.R.layout.simple_spinner_dropdown_item, categoryList);
    private final ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(new ActionsActivity(),
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());

    private final Calendar calendar = Calendar.getInstance();
    private final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private final String date = fmtDateAndTime.format(calendar.getTime());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new FragmentProviderWithRoboFragmentActivity(new AddBillFragment()));

        module.addBinding(SpinnerAdapter.class, arrayAdapter);
        module.addBinding(RecurringCheckBoxOnClickListener.class, checkBoxListener);
        module.addBinding(CategoryDAO.class, categoryDAO);
        module.addBinding(BillDAO.class, billDAO);

        ActivityModule.setUp(this, module);

        underTest = (AddBillFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("Fragment");
    }

    @Test
    public void testOnViewCreatedWhenCalledShouldSetDateButton() {
        final Button dateButton = (Button) underTest.getView().findViewById(R.id.dateButton);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat(dateButton.getText().toString(), equalTo(date));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnViewCreatedWhenCalledThenShouldSetUpRecurringArea() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_bill);
        final ShadowTextView shadowCheckBox = Robolectric.shadowOf(recurringCheckBox);
        final LinearLayout recurringArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_area);
        final Spinner recurringSpinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat((RecurringCheckBoxOnClickListener) shadowCheckBox.getOnClickListener(), equalTo(checkBoxListener));
        assertThat(recurringArea.getVisibility(), equalTo(View.VISIBLE));
        assertThat((ArrayAdapter<RecurringInterval>) recurringSpinner.getAdapter(), equalTo(arrayAdapter));
    }

    @Test
    public void testCreateCategoryOnClickWhenClickedThenStartCreateCategoryActivity() {
        final ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        final ImageButton createCategoryButton = (ImageButton) underTest.getView().findViewById(R.id.createCategoryButton);

        createCategoryButton.performClick();

        final Intent intent = shadowActivity.getNextStartedActivityForResult().intent;
        final ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(CreateCategoryActivity.class.getName()));
    }

    @Test
    public void testOnViewCreatedwhenCalledThenShouldSetUpCategorySpinner() {
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        when(categoryDAO.getAllCategories()).thenReturn(categoryList);

        underTest.onViewCreated(underTest.getView(), null);

        assertThat((Category) categorySpinner.getAdapter().getItem(0), equalTo(categoryAdapter.getItem(0)));
    }

    @Test
    public void testOnActivityResultwhenCalledThenShouldSetUpCategorySpinner() {
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        when(categoryDAO.getAllCategories()).thenReturn(categoryList);

        underTest.onActivityResult(1, Activity.RESULT_OK, null);

        verify(categoryDAO, times(2)).getAllCategories();
        assertThat((Category) categorySpinner.getSelectedItem(), equalTo(categoryAdapter.getItem(0)));
    }

    @Test
    public void testOnActivityResultWhenNotProperThenShouldNotDoAnything() {
        when(categoryDAO.getAllCategories()).thenReturn(categoryList);

        underTest.onActivityResult(0, Activity.RESULT_OK, null);

        verify(categoryDAO, times(1)).getAllCategories();
    }

    @Test
    public void testOnActivityResultWhenCanceledThenShouldNotDoAnything() {
        when(categoryDAO.getAllCategories()).thenReturn(categoryList);

        underTest.onActivityResult(1, Activity.RESULT_CANCELED, null);

        verify(categoryDAO, times(1)).getAllCategories();
    }

    @Test
    public void testOnResumeWhenRecurringCheckBoxIsCheckedThenRecurringCheckBoxAreaShouldBeVisible() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_bill);
        final LinearLayout recurringCheckBoxArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_checkbox_area_bill);
        recurringCheckBox.setChecked(true);

        underTest.onResume();

        assertThat(recurringCheckBoxArea.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void testOnResumeWhenRecurringCheckBoxIsNotCheckedThenRecurringCheckBoxAreaShouldBeInVisible() {
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_bill);
        final LinearLayout recurringCheckBoxArea = (LinearLayout) underTest.getView().findViewById(R.id.recurring_checkbox_area_bill);
        recurringCheckBox.setChecked(false);

        underTest.onResume();

        assertThat(recurringCheckBoxArea.getVisibility(), equalTo(View.INVISIBLE));
    }

    @Test
    public void testSubmitButtonOnClickWhenEverythingIsFineAndNotRecurringThenShouldSaveBill() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final Bill testBill = createTestBill(false);
        fillViewsWithData(testBill);

        submitButton.performClick();

        verify(billDAO).save(testBill);
    }

    @Test
    public void testSubmitButtonOnClickWhenEverythingIsFineAndRecurringThenShouldSaveBill() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final Bill testBill = createTestBill(true);
        fillViewsWithData(testBill);

        submitButton.performClick();

        verify(billDAO).save(testBill);
    }

    @Test
    public void testSubmitButtonOnClickWhenSaveBillIsReturnTrueThenShouldSetResultToOkAndFinish() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        final Bill testBill = createTestBill(true);
        fillViewsWithData(testBill);
        when(billDAO.save((Bill) any())).thenReturn(true);

        submitButton.performClick();

        assertThat(shadowActivity.getResultCode(), equalTo(Activity.RESULT_OK));
        assertThat(shadowActivity.isFinishing(), equalTo(true));
    }

    @Test
    public void testSubmitButtonOnClickWhenSaveBillIsReturnFalseThenShouldShowAToast() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final String toastText = activity.getResources().getString(R.string.database_error);
        final Bill testBill = createTestBill(true);
        fillViewsWithData(testBill);
        when(billDAO.save((Bill) any())).thenReturn(false);

        submitButton.performClick();

        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(toastText));
    }

    @Test
    public void testSubmitButtonOnClickWhenSaveBillIsThrowExceptionThenShouldShowAToast() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);
        final Bill testBill = createTestBill(true);
        fillViewsWithData(testBill);
        when(billDAO.save((Bill) any())).thenThrow(new IllegalArgumentException(EXCEPTION));

        submitButton.performClick();

        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(EXCEPTION));
    }

    @Test
    public void testSubmitButtonOnClickWhenSomethingMissingFromBillsMandatoryFieldsThenShouldShowAToast() {
        final Button submitButton = (Button) underTest.getView().findViewById(R.id.submitButton);

        submitButton.performClick();

        assertThat(ShadowToast.shownToastCount(), equalTo(1));
    }

    private void fillViewsWithData(final Bill testBill) {
        final EditText amountText = (EditText) underTest.getView().findViewById(R.id.amountText);
        final EditText notesText = (EditText) underTest.getView().findViewById(R.id.notesText);
        final Spinner categorySpinner = (Spinner) underTest.getView().findViewById(R.id.categorySpinner);
        final Button deadLineDateButton = (Button) underTest.getView().findViewById(R.id.dateButton);
        final CheckBox recurringCheckBox = (CheckBox) underTest.getView().findViewById(R.id.recurring_checkbox_bill);

        amountText.setText(testBill.getAmount());
        notesText.setText(testBill.getNote());
        deadLineDateButton.setText(testBill.getDeadlineDate());
        if (RecurringInterval.none.equals(testBill.getInterval())) {
            recurringCheckBox.setChecked(false);
        } else {
            recurringCheckBox.setChecked(true);
            final Spinner recurringSpinner = (Spinner) underTest.getView().findViewById(R.id.recurring_spinner);
            final int position = arrayAdapter.getPosition(testBill.getInterval());
            recurringSpinner.setSelection(position);
        }

        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);
    }

    private Bill createTestBill(final boolean isRecurring) {
        final Builder billbuilder = Bill.builder("1234", date, "deadLine");
        billbuilder.category(CATEGORY);
        billbuilder.note("Note");
        billbuilder.isPayed(false);
        billbuilder.payedDate("");

        if (isRecurring) {
            billbuilder.interval(RecurringInterval.annually);
        }
        return billbuilder.build();
    }
}
