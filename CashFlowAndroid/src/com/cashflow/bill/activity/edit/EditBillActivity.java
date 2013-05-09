package com.cashflow.bill.activity.edit;

import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.ID_EXTRA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.dao.BillDAO;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;

/**
 * Edit bill activity.
 * @author Janos_Gyula_Meszaros
 */
public class EditBillActivity extends RoboSherlockActivity {
    private static final Logger LOG = LoggerFactory.getLogger(EditBillActivity.class);

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.billDateButton)
    private Button deadLineDateButton;
    @InjectView(R.id.recurring_area)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_checkbox_area_bill)
    private LinearLayout recurringCheckBoxArea;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_checkbox_bill)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.submitButton)
    private Button submitButton;
    @InjectView(R.id.createCategoryButton)
    private ImageButton createCategory;

    @Inject
    private SpinnerAdapter spinnerAdapter;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private BillDAO billDAO;
    @Inject
    private CategoryDAO categoryDAO;

    private Bill originalBill;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_fragment);
        setupActionBar();
        getOriginalData();
        fillFieldsWithData();
    }

    private void setupActionBar() {
        final Drawable header = getResources().getDrawable(R.drawable.main_header_selector);
        getSupportActionBar().setBackgroundDrawable(header);
    }

    private void getOriginalData() {
        final Intent intent = getIntent();
        final String billId = intent.getStringExtra(ID_EXTRA);
        originalBill = billDAO.getBillById(billId);
        LOG.debug(originalBill.toString());
    }

    private void fillFieldsWithData() {
        amountText.setText(originalBill.getAmount());
        notesText.setText(originalBill.getNote());
        deadLineDateButton.setText(originalBill.getDate());

        final ArrayAdapter<Category> adapter = setCategorySpinner();
        final int position = adapter.getPosition(originalBill.getCategory());
        categorySpinner.setSelection(position);
        setUpRecurringSpinner();
    }

    private void setUpRecurringSpinner() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        bindValuesToSpinner();
        setSelectedItem();
    }

    private void bindValuesToSpinner() {
        recurringSpinner.setAdapter(spinnerAdapter);
    }

    @SuppressWarnings("unchecked")
    private void setSelectedItem() {
        final RecurringInterval interval = originalBill.getInterval();
        if (!RecurringInterval.none.equals(interval)) {
            recurringCheckBox.setChecked(true);
            recurringCheckBoxArea.setVisibility(VISIBLE);
            recurringSpinner.setSelection(((ArrayAdapter<RecurringInterval>) spinnerAdapter).getPosition(interval));
        }
    }

    private ArrayAdapter<Category> setCategorySpinner() {
        final List<Category> list = categoryDAO.getAllCategories();
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);

        categorySpinner.setAdapter(adapter);
        return adapter;
    }

}
