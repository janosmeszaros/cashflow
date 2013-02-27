package com.cashflow.activity.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.ContextSingleton;
import roboguice.inject.RoboInjector;
import android.app.Activity;
import android.app.Application;

import com.cashflow.AppModule;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;

/**
 * Injecting module for ListExpensesActivity test.
 * @author Kornel_Refi
 *
 */
public class ListExpensesModule extends AbstractModule {

    private Map<Class<?>, Object> bindings;

    public ListExpensesModule() {
        bindings = new HashMap<Class<?>, Object>();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        bind(Activity.class).toProvider(ListStatementActivityProvider.class).in(ContextSingleton.class);
        Set<Entry<Class<?>, Object>> entries = bindings.entrySet();
        for (Entry<Class<?>, Object> entry : entries) {
            bind((Class<Object>) entry.getKey()).toInstance(entry.getValue());
        }
    }

    public void addBinding(Class<?> type, Object object) {
        bindings.put(type, object);
    }

    /**
     * Before test.
     * @param testObject underTest
     * @param module Guice module
     */
    public static void setUp(Object testObject, ListExpensesModule module) {
        Module roboGuiceModule = RoboGuice.newDefaultRoboModule(Robolectric.application);
        Module productionModule = Modules.override(roboGuiceModule).with(new AppModule());
        Module testModule = Modules.override(productionModule).with(module);
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, testModule);
        RoboInjector injector = RoboGuice.getInjector(Robolectric.application);
        injector.injectMembers(testObject);
    }

    /**
     * After test.
     */
    public static void tearDown() {
        RoboGuice.util.reset();
        Application app = Robolectric.application;
        DefaultRoboModule defaultModule = RoboGuice.newDefaultRoboModule(app);
        RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, defaultModule);
    }
}
