package com.cashflow.activity.components;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;

/**
 * OnDateSet button service for {@link DatePickerFragment}.
 * @author Kornel_Refi
 *
 */
public class OnDateSetListenerService implements OnDateSetListener {

    private final Activity activity;

    /**
     * Creates an {@link OnDateSetListenerService}.
     * @param activity {@link Activity}
     */
    public OnDateSetListenerService(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Button dateButton = (Button) activity.findViewById(R.id.dateButton);
        final Date setDate = getDateForYearMonthDay(year, monthOfYear, dayOfMonth);
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
