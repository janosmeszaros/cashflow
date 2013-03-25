package com.cashflow.activity.listeners;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.cashflow.components.DatePickerFragment;
import com.google.inject.Inject;

/**
 * On click listener class for date button on add and edit statement dialog.
 * @author Janos_Gyula_Meszaros
 *
 */
public class DateButtonOnClickListener implements OnClickListener {
    private final DialogFragment datePickerFragment;

    /**
     * Create a {@link DateButtonOnClickListener}.
     * @param datePickerFragment {@link DatePickerFragment} can't be <code>null</code>
     */
    @Inject
    public DateButtonOnClickListener(DatePickerFragment datePickerFragment) {
        this.datePickerFragment = datePickerFragment;
    }

    @Override
    public void onClick(View view) {
        datePickerFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "datePicker");
    }

}
