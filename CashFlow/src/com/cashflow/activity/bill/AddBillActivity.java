package com.cashflow.activity.bill;

import static android.view.View.VISIBLE;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.database.category.CategoryPersistenceService;
import com.cashflow.domain.Category;
import com.google.inject.Inject;

/**
 * Add bills.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddBillActivity extends RoboFragmentActivity {
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.recurring_area)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_checkbox)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.submitButton)
    private Button submitButton;

    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;
    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private AddBillOnClickListener submitListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        setUpDateButton();
        activateRecurringArea();
        setCategorySpinner();
        setSubmitButton();
    }

    private void setSubmitButton() {
        submitButton.setOnClickListener(submitListener);
    }

    private void setCategorySpinner() {
        List<Category> list = categoryService.getCategories();

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);
        categorySpinner.setAdapter(adapter);
    }

    private void setUpDateButton() {
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));

        dateButton.setOnClickListener(listener);
    }

    private void activateRecurringArea() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        recurringSpinner.setAdapter(spinnerAdapter);
    }

}
