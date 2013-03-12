package com.cashflow.activity;

import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INTERVAL_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.components.DatePickerFragment;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;

/**
 * Activity for editing statements. It gets the previous values through the intent. 
 * 
 * <p>
 * Extras' names used to get original values: <br/>
 *  <ul> 
 *      <li>Amount:  <code>AMOUNT_EXTRA</code></li>
 *      <li>ID:      <code>ID_EXTRA</code></li>
 *      <li>NOTE:    <code>NOTE_EXTRA</code></li>
 *      <li>DATE:    <code>DATE_EXTRA</code></li>
 *      <li>INTERVAL:<code>INTERVAL_EXTRA</code></li>
 *  </ul>
 * </p>
 * 
 * @author Janos_Gyula_Meszaros
 */
public class EditStatementActivity extends RoboFragmentActivity {
    private static final Logger LOG = LoggerFactory.getLogger(EditStatementActivity.class);
    @Inject
    private StatementPersistenceService service;
    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_checkbox)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.recurring_checkbox_area)
    private LinearLayout recurringCheckBoxArea;
    @Inject
    private Balance balance;
    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;

    private String originalAmount;
    private String originalNotes;
    private String originalDate;
    private String originalId;
    private RecurringInterval originalInterval;

    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("EditStatementActivity is creating...");
        setContentView(R.layout.activity_add_statement);

        setListenerForDateButton();
        getOriginalDatas();
        fillFieldsWithData();
        setStatementType();
        setTitle();

        LOG.debug("EditStatementActivity has created with type: " + type);
    }

    /**
     * Submit onClick method. Save the statement to database. If the save was successful then refresh the balance 
     * and set the result to <code>RESULT_OK</code> then close the activity.
     * @param view
     *            Required for onclick.
     */
    public void submit(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        String id = originalId;
        Statement statement = createStatement(id, amountStr, date, note, type);

        if (isValuesChanged(amountStr, date, note) && service.updateStatement(statement)) {
            refreshBalance(amountStr);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    /**
     * Date button onClick method. Shows the date picker dialog.
     * @param view
     *            Required for onclick.
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private boolean isValuesChanged(String amountStr, String date, String note) {
        boolean result = true;
        if (amountStr.equals(originalAmount) && date.equals(originalDate) && note.equals(originalNotes)) {
            result = false;
        }
        return result;
    }

    private void refreshBalance(String amountStr) {
        BigDecimal originalAmount = new BigDecimal(this.originalAmount);
        BigDecimal currentAmount = new BigDecimal(amountStr);
        BigDecimal sum = originalAmount.subtract(currentAmount);

        LOG.debug("Original amount " + originalAmount.doubleValue() + " changed to " + currentAmount.doubleValue());

        balance.subtract(sum);
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_edit_incomes);
        } else {
            setTitle(R.string.title_activity_edit_expenses);
        }
    }

    private void fillFieldsWithData() {
        amountText.setText(originalAmount);
        notesText.setText(originalNotes);
        dateButton.setText(originalDate);

        setSpinnerSelectedItem();
    }

    private void setSpinnerSelectedItem() {
        if (!originalInterval.equals(RecurringInterval.none)) {
            ArrayAdapter<RecurringInterval> adapter = new ArrayAdapter<RecurringInterval>(this, android.R.layout.simple_spinner_dropdown_item,
                    RecurringInterval.values());
            recurringCheckBox.setChecked(true);
            recurringCheckBoxArea.setVisibility(VISIBLE);
            recurringSpinner.setAdapter(adapter);
            recurringSpinner.setSelection(adapter.getPosition(originalInterval));
        }
    }

    private void setStatementType() {
        Intent intent = getIntent();
        type = StatementType.valueOf(intent.getStringExtra(STATEMENT_TYPE_EXTRA));

        if (type.isIncome()) {
            setUpSpinner();
        }
    }

    private void setUpSpinner() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
    }

    private Statement createStatement(String id, String amountStr, String date, String note, StatementType type) {
        return new Statement.Builder(amountStr, date).setNote(note).setType(type).setId(id).build();
    }

    private void setListenerForDateButton() {
        dateButton.setOnClickListener(listener);
    }

    private void getOriginalDatas() {
        Intent intent = getIntent();
        originalAmount = intent.getStringExtra(AMOUNT_EXTRA);
        originalNotes = intent.getStringExtra(NOTE_EXTRA);
        originalDate = intent.getStringExtra(DATE_EXTRA);
        originalId = intent.getStringExtra(ID_EXTRA);
        originalInterval = RecurringInterval.valueOf(intent.getStringExtra(INTERVAL_EXTRA));
    }
}
