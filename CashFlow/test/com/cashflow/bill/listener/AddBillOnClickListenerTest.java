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
import com.cashflow.bill.database.BillPersistenceService;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class AddBillOnClickListenerTest {
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
    public void testOnClickWhenViewSettedButSaveWasUnsuccessfulThenShouldCallServicesSaveBillMethodAndSetResultToCanceled() {
        when(service.saveBill(billToSave)).thenReturn(false);

        underTest.onClick(submitButton);

        verify(service).saveBill(billToSave);
        verify(activity).setResult(Activity.RESULT_CANCELED);
        verify(activity).finish();
    }

    @Test
    public void testOnClickWhenSaveThrowsExceptionThenShouldShowToast() {
        when(service.saveBill(billToSave)).thenThrow(new IllegalArgumentException("Exception"));

        underTest.onClick(submitButton);

        verify(service).saveBill(billToSave);
        assertThat(ShadowToast.shownToastCount(), equalTo(1));
    }

    private void setUpActivityModule() {
        ActivityModule module = new ActivityModule(new ActivityProvider());

        billToSave = createBillToSave(false);
        setViewsValues(billToSave, module);
        module.addBinding(BillPersistenceService.class, service);

        ActivityModule.setUp(this, module);
    }

    private Bill createBillToSave(boolean isSaved) {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar calendar = Calendar.getInstance();

        Bill billToSave = new Bill("123", dateFormatter.format(calendar.getTime()), dateFormatter.format(calendar.getTime()));
        billToSave.setCategory(new Category("1", "category"));
        billToSave.setInterval(RecurringInterval.none);
        billToSave.setNote("note");
        billToSave.setPayed(isSaved);
        billToSave.setPayedDate("");

        return billToSave;
    }

    private void setViewsValues(Bill bill, ActivityModule module) {
        EditText amount = new EditText(activity);
        amount.setText(bill.getAmount());
        module.addViewBinding(R.id.amountText, amount);

        Button button = new Button(activity);
        button.setText(bill.getDate());
        module.addViewBinding(R.id.dateButton, button);

        EditText notes = new EditText(activity);
        notes.setText(bill.getNote());
        module.addViewBinding(R.id.notesText, notes);

        Spinner spinner = new Spinner(activity);
        spinner.setAdapter(arrayAdapter);
        int selection = arrayAdapter.getPosition(bill.getInterval());
        spinner.setSelection(selection);
        module.addViewBinding(R.id.recurring_spinner, spinner);

        Spinner categorySpinner = new Spinner(activity);
        List<Category> categories = new ArrayList<Category>();
        categories.add(bill.getCategory());
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(activity, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryArrayAdapter);
        int categoryPos = categoryArrayAdapter.getPosition(bill.getCategory());
        categorySpinner.setSelection(categoryPos);
        module.addViewBinding(R.id.categorySpinner, categorySpinner);
    }

}