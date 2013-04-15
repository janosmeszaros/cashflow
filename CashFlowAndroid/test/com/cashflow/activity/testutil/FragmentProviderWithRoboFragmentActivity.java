package com.cashflow.activity.testutil;

import roboguice.activity.RoboFragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.inject.Provider;

/**
 * Provider for.
 * @author Janos_Gyula_Meszaros
 *
 */
public class FragmentProviderWithRoboFragmentActivity implements Provider<RoboFragmentActivity> {

    private final Fragment fragment;

    /**
     * Constructor witch gets a fragment what will be in actionsactivity.    
     * @param fragment fragment
     */
    public FragmentProviderWithRoboFragmentActivity(final Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public RoboFragmentActivity get() {
        final RoboFragmentActivity activity = new RoboFragmentActivity();

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, "Fragment");
        fragmentTransaction.commit();

        return activity;
    }

}
