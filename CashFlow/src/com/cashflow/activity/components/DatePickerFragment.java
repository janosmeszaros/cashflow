package com.cashflow.activity.components;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DatePicker fragment.
 * @author Kornel_Refi
 */
@Singleton
public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

    @Inject
    private OnDateSetService dateSetService;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        final int year = c.get(YEAR);
        final int month = c.get(MONTH);
        final int day = c.get(DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int month, final int day) {
        final Button dateButton = (Button) getActivity().findViewById(R.id.dateButton);
        dateSetService.onDateSet(dateButton, view);
    }

}
