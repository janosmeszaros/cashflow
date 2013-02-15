package com.cashflow;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.Dao;
import com.cashflow.database.DatabaseContracts;

public class AddIncomeActivity extends FragmentActivity {
	
	private static int TRUE = 1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_income);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		final Calendar calendar = Calendar.getInstance();
		DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		Button dateButton = (Button) findViewById(R.id.dateButton);
		dateButton.setText( fmtDateAndTime.format(calendar.getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_income, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	public void addIncome(View view) {
		//Get values
		EditText amountText = (EditText) findViewById(R.id.amountText);
		
		BigDecimal amount = BigDecimal.ZERO;
		if(amountText.length() != 0){
			amount = new BigDecimal(amountText.getText().toString());
		}
		
		Button dateButton = (Button) findViewById(R.id.dateButton);
		String date = dateButton.getText().toString();
		
		EditText notesText = (EditText) findViewById(R.id.notesText);
		String note = notesText.getText().toString();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(DatabaseContracts.Statement.COLUMN_NAME_AMOUNT, amount.toString());
		values.put(DatabaseContracts.Statement.COLUMN_NAME_DATE, date);
		values.put(DatabaseContracts.Statement.COLUMN_NAME_IS_INCOME, TRUE);
		values.put(DatabaseContracts.Statement.COLUMN_NAME_NOTE, note);
		
		Dao dao = new Dao(this);
		
		if(amount.compareTo(BigDecimal.ZERO) != 0){
			dao.save(values);
			finish();
		}
	}
	
	public void showDatePickerDialog(View view){
		DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	    
	}

}
