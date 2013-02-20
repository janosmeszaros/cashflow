package com.cashflow;

import java.text.DateFormat;
import java.util.Calendar;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Inject;

/**
 * Add new income.
 * @author Kornel_Refi
 */
public class AddIncomeActivity extends RoboFragmentActivity {

    @Inject
    private StatementPersistentService service;

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;

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
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();

        if (service.saveStatement(amountStr, date, note, true)) {
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
