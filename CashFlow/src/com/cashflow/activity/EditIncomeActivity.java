package com.cashflow.activity;

import static com.cashflow.constants.EditConstants.AMOUNT_EXTRA;
import static com.cashflow.constants.EditConstants.DATE_EXTRA;
import static com.cashflow.constants.EditConstants.ID_EXTRA;
import static com.cashflow.constants.EditConstants.NOTE_EXTRA;

import java.math.BigDecimal;

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
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Activity for editing incomes.
 * @author Janos_Gyula_Meszaros
 */
public class EditIncomeActivity extends RoboFragmentActivity {

    @Inject
    private StatementPersistentService service;
    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    private Intent intent;
    @Inject
    private Balance balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        intent = getIntent();
        fillFieldsWithData();

    }

    private void fillFieldsWithData() {
        amountText.setText(intent.getStringExtra(AMOUNT_EXTRA));
        notesText.setText(intent.getStringExtra(NOTE_EXTRA));
        dateButton.setText(intent.getStringExtra(DATE_EXTRA));
    }

    /**
     * Add new income onClick method. Save the income to database. If the save was successful then refresh the balance.
     * @param view
     *            Required for onclick.
     */
    public void addIncome(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        String id = intent.getStringExtra(ID_EXTRA);

        if (service.updateStatement(id, amountStr, date, note, StatementType.Income)) {
            refreshBalance(amountStr);
            finish();
        }
    }

    private void refreshBalance(String amountStr) {
        BigDecimal originalAmount = new BigDecimal(intent.getStringExtra(AMOUNT_EXTRA));
        BigDecimal currentAmount = new BigDecimal(amountStr);
        BigDecimal sum = originalAmount.subtract(currentAmount);

        balance.subtract(sum);
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

}
