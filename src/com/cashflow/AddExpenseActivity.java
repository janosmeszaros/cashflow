package com.cashflow;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.cashflow.components.DatePickerFragment;

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
     * Show the date picker. DateButton on click method.
     * @param view
     *            Required for onclick.
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
