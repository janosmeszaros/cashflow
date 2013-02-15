package com.cashflow;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class AddExpenseActivity extends Activity {

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

}
