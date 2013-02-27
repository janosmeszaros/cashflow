package com.cashflow.activity;

import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
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
 * Expense adding.
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementActivity extends RoboFragmentActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AddStatementActivity.class);
    @Inject
    private StatementPersistentService service;

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @Inject
    private Balance balance;
    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("AddStatementActivity is creating...");

        setContentView(R.layout.activity_add_statement);

        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));
        setStatementType();
        setTitle();

        LOG.debug("AddStatementActivity has created with type: " + type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_expense, menu);
        return true;
    }

    /**
     * Onclick event for add expense button on add expense screen. Save the expense to database. If the save was successful then refresh the
     * balance.
     * @param view
     *            Required for onclick.
     */
    public void submit(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();

        if (service.saveStatement(amountStr, date, note, type)) {
            balance.subtract(new BigDecimal(amountStr));
            finish();
        }
    }

    /**
     * Show the date picker. DateButton on click method.
     * @param view
     *            Required for onclick.
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void setStatementType() {
        if (getIntent().getStringExtra(STATEMENT_TYPE_EXTRA).equals(INCOME_EXTRA)) {
            type = StatementType.Income;
        } else {
            type = StatementType.Expense;
        }
    }

    private void setTitle() {
        if (type.equals(StatementType.Income)) {
            setTitle(R.string.title_activity_add_income);
        } else {
            setTitle(R.string.title_activity_add_expense);
        }
    }

}
