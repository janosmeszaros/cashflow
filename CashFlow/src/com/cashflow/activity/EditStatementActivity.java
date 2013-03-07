package com.cashflow.activity;

import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
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
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.R;
import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Activity for editing incomes.
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
    private Intent intent;
    @Inject
    private Balance balance;

    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("EditStatementActivity is creating...");
        setContentView(R.layout.activity_add_statement);

        intent = getIntent();
        fillFieldsWithData();
        setStatementType();
        setTitle();

        LOG.debug("EditStatementActivity has created with type: " + type);
    }

    /**
     * Add new income onClick method. Save the income to database. If the save was successful then refresh the balance.
     * @param view
     *            Required for onclick.
     */
    public void submit(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        String id = intent.getStringExtra(ID_EXTRA);

        if (isValuesChanged(amountStr, date, note) && service.updateStatement(id, amountStr, date, note, type)) {
            refreshBalance(amountStr);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    /**
     * Date button onClick method.
     * @param view
     *            Required for onclick.
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private boolean isValuesChanged(String amountStr, String date, String note) {
        boolean result = false;
        if (amountStr.equals(intent.getStringExtra(AMOUNT_EXTRA)) || date.equals(intent.getStringExtra(DATE_EXTRA))
                || note.equals(intent.getStringExtra(DATE_EXTRA))) {
            result = true;
        }
        return result;
    }

    private void refreshBalance(String amountStr) {
        BigDecimal originalAmount = new BigDecimal(intent.getStringExtra(AMOUNT_EXTRA));
        BigDecimal currentAmount = new BigDecimal(amountStr);
        BigDecimal sum = originalAmount.subtract(currentAmount);

        LOG.debug("Original amount " + originalAmount.doubleValue() + " changed to " + currentAmount.doubleValue());

        balance.subtract(sum);
    }

    private void setTitle() {
        if (type.equals(StatementType.Income)) {
            setTitle(R.string.title_activity_edit_incomes);
        } else {
            setTitle(R.string.title_activity_edit_expenses);
        }
    }

    private void fillFieldsWithData() {
        amountText.setText(intent.getStringExtra(AMOUNT_EXTRA));
        notesText.setText(intent.getStringExtra(NOTE_EXTRA));
        dateButton.setText(intent.getStringExtra(DATE_EXTRA));
    }

    private void setStatementType() {
        if (getIntent().getStringExtra(STATEMENT_TYPE_EXTRA).equals(INCOME_EXTRA)) {
            type = StatementType.Income;
        } else {
            type = StatementType.Expense;
        }
    }
}
