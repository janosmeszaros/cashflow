package com.cashflow.activity.listeners;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.cashflow.components.DatePickerFragment;

/**
 * On click listener class for date button on add and edit statement dialog.
 * @author Janos_Gyula_Meszaros
 *
 */
public class DateButtonOnClickListener implements OnClickListener {

    @Override
    public void onClick(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "datePicker");

    }

}
