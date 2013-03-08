package com.cashflow.components;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;

/**
 * DatePicker fragment.
 * @author Kornel_Refi
 */
public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(YEAR);
        int month = c.get(MONTH);
        int day = c.get(DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Date setDate = getDateForYearMonthDay(year, month, day);

        updateButtonTextToDate(setDate);
    }

    private Date getDateForYearMonthDay(int year, int month, int day) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(year, month, day);

        return myCalendar.getTime();
    }

    private void updateButtonTextToDate(Date setDate) {
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);

        Button dateButton = (Button) getActivity().findViewById(R.id.dateButton);
        dateButton.setText(fmtDateAndTime.format(setDate));
    }

}
