package com.cashflow.activity.testutil;

import android.app.Activity;

import com.google.inject.Provider;

/**
 * Provider for {@link Activity} class.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ActivityProvider implements Provider<Activity> {

    @Override
    public Activity get() {
        return new Activity();
    }

}
