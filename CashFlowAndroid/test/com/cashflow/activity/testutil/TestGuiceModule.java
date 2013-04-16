package com.cashflow.activity.testutil;

import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.RoboInjector;
import android.app.Application;

import com.cashflow.AppModule;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;

/**
 * Guice module for tests.
 * @author Kornel_Refi
 *
 */
public class TestGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    /**
     * Before test.
     * @param testObject underTest
     * @param module Guice module
     */
    public static void setUp(final Object testObject, final TestGuiceModule module) {
        final Module roboGuiceModule = RoboGuice.newDefaultRoboModule(Robolectric.application);
        final Module productionModule = Modules.override(roboGuiceModule).with(new AppModule());
        final Module testModule = Modules.override(productionModule).with(module);
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, testModule);
        final RoboInjector injector = RoboGuice.getInjector(Robolectric.application);
        injector.injectMembers(testObject);
    }

    /**
     * After test.
     */
    public static void tearDown() {
        RoboGuice.util.reset();
        final Application app = Robolectric.application;
        final DefaultRoboModule defaultModule = RoboGuice.newDefaultRoboModule(app);
        RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, defaultModule);
    }

}
