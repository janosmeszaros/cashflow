package com.cashflow.bill.activity;

import static android.view.View.VISIBLE;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.cashflow.service.BillPersistenceService;
import com.cashflow.service.CategoryPersistenceService;
import com.google.inject.Inject;

/**
 * Add bills.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddBillFragment extends RoboFragment {
    private static final Logger LOG = LoggerFactory.getLogger(AddBillFragment.class);

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.dateButton)
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
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;
    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private BillPersistenceService persistenceService;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.add_bill_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDateButton();
        activateRecurringArea();
        setCategorySpinner();
        submitButton.setOnClickListener(new AddBillOnClickListener());
        createCategory.setOnClickListener(new CreateCategoryOnClickListener());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setCategorySpinner();
        categorySpinner.setSelection(categorySpinner.getAdapter().getCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recurringCheckBox.isChecked()) {
            recurringCheckBoxArea.setVisibility(VISIBLE);
        }
    }

    private void setCategorySpinner() {
        final List<Category> list = categoryService.getCategories();

        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
        categorySpinner.setAdapter(adapter);
    }

    private void setUpDateButton() {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        deadLineDateButton.setText(fmtDateAndTime.format(calendar.getTime()));

        deadLineDateButton.setOnClickListener(listener);
    }

    private void activateRecurringArea() {
        LOG.debug(checkBoxListener.toString());
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        recurringSpinner.setAdapter(spinnerAdapter);
    }

    protected class CreateCategoryOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
            startActivityForResult(intent, 1);
        }

    }

    /**
     * {@link OnClickListener} for AddBillActivity's submit button.
     * @author Janos_Gyula_Meszaros
     *
     */
    public class AddBillOnClickListener implements OnClickListener {
        @Override
        public void onClick(final View view) {
            final Bill billToSave = createBill();
            final Activity parent = (Activity) view.getContext();

            try {
                if (persistenceService.saveBill(billToSave)) {
                    parent.setResult(Activity.RESULT_OK);
                    parent.finish();
                } else {
                    showToast(parent, parent.getString(R.string.database_error));
                }

            } catch (final IllegalArgumentException e) {
                showToast(parent, e.getMessage());
            }
        }

        private void showToast(final Activity parent, final String msg) {
            final Toast toast = Toast.makeText(parent, msg, Toast.LENGTH_SHORT);
            toast.show();
        }

        private Bill createBill() {
            final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            final Calendar myCalendar = Calendar.getInstance();

            final Bill billToSave = new Bill(amountText.getText().toString(), dateFormatter.format(myCalendar.getTime()), deadLineDateButton
                    .getText().toString());
            billToSave.setCategory((Category) categorySpinner.getSelectedItem());
            billToSave.setNote(notesText.getText().toString());
            billToSave.setPayed(false);
            billToSave.setPayedDate("");

            if (recurringCheckBox.isChecked()) {
                billToSave.setInterval((RecurringInterval) recurringSpinner.getSelectedItem());
            }

            return billToSave;
        }
    }
}