package com.cashflow;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.cashflow.constants.RecurringInterval;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * RoboGuice provider for filling up recurring spinners with recurring intervals from {@link RecurringInterval}'s values.
 * Context will be always the current activity.
 * @author Janos_Gyula_Meszaros
 *
 */
public class RecurringIntervalArrayAdapterProvider implements Provider<SpinnerAdapter> {

    @Inject
    private Context context;

    @Override
    public SpinnerAdapter get() {
        return new ArrayAdapter<RecurringInterval>(context, android.R.layout.simple_spinner_dropdown_item, RecurringInterval.values());
    }
}
