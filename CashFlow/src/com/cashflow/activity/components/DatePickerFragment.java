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

/**
 * DatePicker fragment.
 * @author Kornel_Refi
 */
public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        final int year = c.get(YEAR);
        final int month = c.get(MONTH);
        final int day = c.get(DAY_OF_MONTH);

        OnDateSetListener dateSetListener = new OnDateSetListenerService(getActivity());

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

}
