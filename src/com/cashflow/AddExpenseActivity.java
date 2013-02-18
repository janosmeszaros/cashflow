package com.cashflow;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.StatementBuilderService;

/**
 * Expense adding.
 * @author Janos_Gyula_Meszaros
 */
public class AddExpenseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);

        Button dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_expense, menu);
        return true;
    }

    /**
     * Onclick event for add expense button on add expense screen. Save the expense to database.
     * @param view
     *            Required for onclick.
     */
    public void addExpense(View view) {
        EditText amountText = (EditText) findViewById(R.id.amountText);
        String amountStr = amountText.getText().toString();

        Button dateButton = (Button) findViewById(R.id.dateButton);
        String date = dateButton.getText().toString();

        EditText notesText = (EditText) findViewById(R.id.notesText);
        String note = notesText.getText().toString();

        StatementBuilderService builderService = new StatementBuilderService(this);
        if (builderService.saveStatement(amountStr, date, note, false)) {
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

}
