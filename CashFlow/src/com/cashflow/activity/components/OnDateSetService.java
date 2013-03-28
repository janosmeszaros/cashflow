package com.cashflow.activity.components;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.Button;
import android.widget.DatePicker;

/**
 * OnDateSet button service for {@link DatePickerFragment}.
 * @author Kornel_Refi
 *
 */
public class OnDateSetService {

    /**
     * Sets the button text to the picked date.
     * @param dateButton {@link Button}
     * @param datePicker {@link DatePicker}
     */
    public void onDateSet(final Button dateButton, final DatePicker datePicker) {
        final Date setDate = getDateForYearMonthDay(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        updateButtonTextToDate(dateButton, setDate);
    }

    private Date getDateForYearMonthDay(final int year, final int month, final int day) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(year, month, day);

        return myCalendar.getTime();
    }

    private void updateButtonTextToDate(final Button dateButton, final Date setDate) {
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(setDate));
    }
}
