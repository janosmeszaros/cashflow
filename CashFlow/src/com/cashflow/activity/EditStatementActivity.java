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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
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
    private StatementPersistenceService service;
    @Inject
    private Balance balance;
    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;

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
        Statement statement = createStatement();

        if (isValuesChanged() && service.updateStatement(statement)) {
            refreshBalance();
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private boolean isValuesChanged() {
        boolean result = true;
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();

        if (amountStr.equals(originalAmount) && date.equals(originalDate) && note.equals(originalNotes) && interval.equals(originalInterval)) {
            result = false;
        }
        return result;
    }

    private void refreshBalance() {
        String amountStr = amountText.getText().toString();
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
        bindValuesToSpinner();
        setSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void setSelectedItem() {
        if (!originalInterval.equals(RecurringInterval.none)) {
            recurringCheckBox.setChecked(true);
            recurringCheckBoxArea.setVisibility(VISIBLE);
            recurringSpinner.setSelection(((ArrayAdapter<RecurringInterval>) spinnerAdapter).getPosition(originalInterval));
        }
    }

    private void bindValuesToSpinner() {
        recurringSpinner.setAdapter(spinnerAdapter);
    }

    private Statement createStatement() {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        String id = originalId;

        Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setId(id);

        if (type.isIncome()) {
            RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();
            builder.setRecurringInterval(interval);
        }

        return builder.build();
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
