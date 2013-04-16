package com.cashflow.activity.testutil;

import java.lang.reflect.Field;

import roboguice.inject.InjectView;
import android.util.SparseArray;

import com.google.inject.MembersInjector;

/**
 * View injector class for {@link ActivityModule}.
 * @author Janos_Gyula_Meszaros
 *
 * @param <T>
 */
public class TestViewMembersInjector<T> implements MembersInjector<T> {
    private final SparseArray<Object> viewBindings;
    private final Field field;

    /**
     * Constructor.
     * @param viewBindings view bindings.
     * @param field field to bind to.
     */
    public TestViewMembersInjector(final SparseArray<Object> viewBindings, final Field field) {
        this.viewBindings = viewBindings;
        this.field = field;
    }

    @Override
    public void injectMembers(final T instance) {
        final InjectView injectView = field.getAnnotation(InjectView.class);
        final int viewId = injectView.value();
        field.setAccessible(true);
        final Object view = viewBindings.get(viewId);
        if (view != null) {
            try {
                field.set(instance, view);
            } catch (final Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
