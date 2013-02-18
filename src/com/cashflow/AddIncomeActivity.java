package com.cashflow;

import java.text.DateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.StatementBuilderService;

/**
 * Add new income.
 * @author Kornel_Refi
 */
public class AddIncomeActivity extends FragmentActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        // // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        // // Show the Up button in the action bar.
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // }

        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);

        Button dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_income, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
        // case android.R.id.home:
        // // This ID represents the Home or Up button. In the case of this
        // // activity, the Up button is shown. Use NavUtils to allow users
        // // to navigate up one level in the application structure. For
        // // more details, see the Navigation pattern on Android Design:
        // //
        // // http://developer.android.com/design/patterns/navigation.html#up-vs-back
        // //
        // NavUtils.navigateUpFromSameTask(this);
        // result = true;
        // break;
        default:
            result = super.onOptionsItemSelected(item);
        }
        return result;

    }

    /**
     * Add new income onClick method.
     * @param view
     *            Required for onclick.
     */
    public void addIncome(View view) {
        // Get values
        EditText amountText = (EditText) findViewById(R.id.amountText);
        String amountStr = amountText.getText().toString();

        Button dateButton = (Button) findViewById(R.id.dateButton);
        String date = dateButton.getText().toString();

        EditText notesText = (EditText) findViewById(R.id.notesText);
        String note = notesText.getText().toString();

        StatementBuilderService builderService = new StatementBuilderService(this);
        if (builderService.saveStatement(amountStr, date, note, true)) {
            finish();
        }
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
