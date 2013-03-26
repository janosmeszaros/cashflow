package com.cashflow.bill.listener;

import java.text.DateFormat;
import java.util.Calendar;

import org.apache.commons.lang.Validate;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;

    private final BillPersistenceService persistenceService;

    /**
     * Constructor which gets a {@link BillPersistenceService} to save data.
     * @param persistenceService {@link BillPersistenceService} to save data.
     */
    @Inject
    public AddBillOnClickListener(final BillPersistenceService persistenceService) {
        Validate.notNull(persistenceService);
        this.persistenceService = persistenceService;

    }

    @Override
    public void onClick(final View view) {
        final Bill billToSave = createBill();
        final Activity parent = (Activity) view.getContext();

        if (persistenceService.saveBill(billToSave)) {
            parent.setResult(Activity.RESULT_OK);
        } else {
            parent.setResult(Activity.RESULT_CANCELED);
        }

        parent.finish();
    }

    private Bill createBill() {
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final Calendar myCalendar = Calendar.getInstance();

        Bill billToSave = new Bill(amountText.getText().toString(), dateFormatter.format(myCalendar.getTime()), deadlineDateButton.getText()
                .toString());
        billToSave.setCategory((Category) categorySpinner.getSelectedItem());
        billToSave.setInterval((RecurringInterval) recurringSpinner.getSelectedItem());
        billToSave.setNote(notesText.getText().toString());
        billToSave.setPayed(false);
        billToSave.setPayedDate("");

        return billToSave;
    }

}