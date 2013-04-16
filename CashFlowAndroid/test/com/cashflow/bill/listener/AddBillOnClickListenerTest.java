package com.cashflow.bill.listener;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.cashflow.service.BillPersistenceService;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class AddBillOnClickListenerTest {
    private static final String TOAST_MESSAGE = "Something happened with the database. Please try again!";
    @Inject
    private AddBillOnClickListener underTest;
    @Mock
    private BillPersistenceService service;
    @Mock
    private Activity activity;
    @Mock
    private View submitButton;

    private final ArrayAdapter<RecurringInterval> arrayAdapter = new ArrayAdapter<RecurringInterval>(activity,
            android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());
    private Bill billToSave;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpActivityModule();
        when(submitButton.getContext()).thenReturn(activity);
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenServiceIsNullThenShouldThrowException() {
        underTest = new AddBillOnClickListener(null);
    }

    @Test
    public void testOnClickWhenViewSettedThenShouldCallServicesSaveBillMethod() {
        when(service.saveBill(billToSave)).thenReturn(true);

        underTest.onClick(submitButton);

        verify(service).saveBill(billToSave);
        verify(activity).setResult(Activity.RESULT_OK);
        verify(activity).finish();
    }

    @Test
    public void testOnClickWhenViewSettedButSaveWasUnsuccessfulThenShouldCallServicesSaveBillMethodAndShowToast() {
        when(service.saveBill(billToSave)).thenReturn(false);
        when(submitButton.getContext()).thenReturn(activity);
        when(activity.getString(R.string.database_error)).thenReturn(TOAST_MESSAGE);

        underTest.onClick(submitButton);

        verify(service).saveBill(billToSave);
        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(TOAST_MESSAGE));
    }

    @Test
    public void testOnClickWhenSaveThrowsExceptionThenShouldShowToast() {
        when(service.saveBill(billToSave)).thenThrow(new IllegalArgumentException("Exception"));

        underTest.onClick(submitButton);

        verify(service).saveBill(billToSave);
        assertThat(ShadowToast.shownToastCount(), equalTo(1));
    }

    private void setUpActivityModule() {
        final ActivityModule module = new ActivityModule(new ActivityProvider());

        billToSave = createBillToSave(false);
        setViewsValues(billToSave, module);
        module.addBinding(BillPersistenceService.class, service);

        ActivityModule.setUp(this, module);
    }

    private Bill createBillToSave(final boolean isSaved) {
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final Calendar calendar = Calendar.getInstance();

        final Bill billToSave = new Bill("123", dateFormatter.format(calendar.getTime()), dateFormatter.format(calendar.getTime()));
        billToSave.category(new Category("1", "category"));
        billToSave.interval(RecurringInterval.none);
        billToSave.note("note");
        billToSave.isPayed(isSaved);
        billToSave.payedDate("");

        return billToSave;
    }

    private void setViewsValues(final Bill bill, final ActivityModule module) {
        final EditText amount = new EditText(activity);
        amount.setText(bill.getAmount());
        module.addViewBinding(R.id.amountText, amount);

        final Button button = new Button(activity);
        button.setText(bill.getDate());
        module.addViewBinding(R.id.dateButton, button);

        final EditText notes = new EditText(activity);
        notes.setText(bill.getNote());
        module.addViewBinding(R.id.notesText, notes);

        final Spinner spinner = new Spinner(activity);
        spinner.setAdapter(arrayAdapter);
        final int selection = arrayAdapter.getPosition(bill.getInterval());
        spinner.setSelection(selection);
        module.addViewBinding(R.id.recurring_spinner, spinner);

        final Spinner categorySpinner = new Spinner(activity);
        final List<Category> categories = new ArrayList<Category>();
        categories.add(bill.getCategory());
        final ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(activity, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);
        final int categoryPos = categoryAdapter.getPosition(bill.getCategory());
        categorySpinner.setSelection(categoryPos);
        module.addViewBinding(R.id.categorySpinner, categorySpinner);
    }

}