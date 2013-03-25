package com.cashflow.bill.listener;

import java.text.DateFormat;
import java.util.Calendar;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cashflow.R;
import com.cashflow.bill.database.BillPersistenceService;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.google.inject.Inject;

/**
 * {@link OnClickListener} for AddBillActivity's submit button.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddBillOnClickListener implements OnClickListener {
    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button deadlineDateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.recurring_area)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_checkbox)
    private CheckBox recurringCheckBox;

    private final BillPersistenceService persistenceService;

    /**
     * Constructor which gets a {@link BillPersistenceService} to save data.
     * @param persistenceService {@link BillPersistenceService} to save data.
     */
    @Inject
    public AddBillOnClickListener(BillPersistenceService persistenceService) {
        this.persistenceService = persistenceService;

    }

    @Override
    public void onClick(View v) {
        Bill billToSave = createBill();
        Activity parent = (Activity) v.getContext();

        if (persistenceService.saveBill(billToSave)) {
            parent.setResult(Activity.RESULT_OK);
        } else {
            parent.setResult(Activity.RESULT_CANCELED);
        }

        parent.finish();
    }

    private Bill createBill() {
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar myCalendar = Calendar.getInstance();

        Bill billToSave = new Bill(amountText.getText().toString(), fmtDateAndTime.format(myCalendar.getTime()), deadlineDateButton.getText()
                .toString());
        billToSave.setCategory((Category) categorySpinner.getSelectedItem());
        billToSave.setInterval((RecurringInterval) recurringSpinner.getSelectedItem());
        billToSave.setNote(notesText.getText().toString());
        billToSave.setPayed(false);
        billToSave.setPayedDate("");

        return billToSave;
    }

}
