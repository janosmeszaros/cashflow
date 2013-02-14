package com.cashflow.components;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
		DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
	
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.set(year, month, day);
		
		Button dateButton = (Button)getActivity().findViewById(R.id.dateButton);
		dateButton.setText(fmtDateAndTime.format(myCalendar.getTime()));
	}
}