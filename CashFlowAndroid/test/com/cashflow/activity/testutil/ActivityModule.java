package com.cashflow.activity.testutil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import roboguice.inject.RoboInjector;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import com.cashflow.AppModule;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;

/**
 * Injecting module for ListExpensesActivity test.
 * @author Kornel_Refi
 *
 */
public class ActivityModule extends AbstractModule {
    private final Map<Class<?>, Object> bindings;

    private final Provider provider;

    private final SparseArray<Object> viewBindings;

    /**
     * Creates a new Guice module for ListExpenseActivity testing.
     * @param provider provider to bind the activity.
     */
    public ActivityModule(final Provider provider) {
        super();
        bindings = new HashMap<Class<?>, Object>();
        viewBindings = new SparseArray<Object>();
        this.provider = provider;
    }

    /**
     * Before test.
     * @param testObject underTest
     * @param module Guice module
     */
    public static void setUp(final Object testObject, final ActivityModule module) {
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

    /**
     * Add binding.
     * @param type class name
     * @param object actual object
     */
    public void addBinding(final Class<?> type, final Object object) {
        bindings.put(type, object);
    }

    /**
     * Add view binding.
     * @param id view id.
     * @param object Object to bind.
     */
    public void addViewBinding(final int id, final Object object) {
        viewBindings.put(id, object);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        bind(Activity.class).toProvider(provider).in(ContextSingleton.class);
        final Set<Entry<Class<?>, Object>> entries = bindings.entrySet();
        for (final Entry<Class<?>, Object> entry : entries) {
            bind((Class<Object>) entry.getKey()).toInstance(entry.getValue());
        }
        bindListener(Matchers.any(), new ViewTypeListener());
    }

    private final class ViewTypeListener implements TypeListener {

        @Override
        public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
            for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass()) {
                for (final Field field : c.getDeclaredFields()) {
                    if (field.isAnnotationPresent(InjectView.class)) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            throw new UnsupportedOperationException("Views may not be statically injected");
                        } else if (!View.class.isAssignableFrom(field.getType())) {
                            throw new UnsupportedOperationException("You may only use @InjectView on fields descended from type View");
                        } else if (Context.class.isAssignableFrom(field.getDeclaringClass())
                                && !Activity.class.isAssignableFrom(field.getDeclaringClass())) {
                            throw new UnsupportedOperationException("You may only use @InjectView in Activity contexts");
                        } else {
                            typeEncounter.register(new TestViewMembersInjector<I>(viewBindings, field));
                        }
                    }
                }
            }
        }
    }
}
