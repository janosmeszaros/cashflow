package com.cashflow.activity.components;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;

/**
 * OnDateSet button service for {@link DatePickerFragment}.
 * @author Kornel_Refi
 */
public class OnDateSetListenerService implements OnDateSetListener {
    private final Activity activity;

    /**
     * Creates an {@link OnDateSetListenerService}.
     * @param activity
     *            {@link Activity}
     */
    public OnDateSetListenerService(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
        final Button dateButton = getDateButton();
        final Date setDate = getDateForYearMonthDay(year, monthOfYear, dayOfMonth);
        updateButtonTextToDate(dateButton, setDate);
    }

    @SuppressLint("NewApi")
    private Button getDateButton() {
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.pager);

        Button dateButton;
        switch (pager.getCurrentItem()) {
        case 0:
            dateButton = (Button) activity.findViewById(R.id.incomeDateButton);
            break;
        case 1:
            dateButton = (Button) activity.findViewById(R.id.expenseDateButton);
            break;
        default:
            dateButton = (Button) activity.findViewById(R.id.billDateButton);
            break;
        }
        return dateButton;
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
