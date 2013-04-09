package com.cashflow.activity.testutil;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;

import android.os.Build;

import com.cashflow.activity.testutil.shadows.RoboSherlockActivityShadow;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * Our extended Robolectric test runner.
 * @author Kornel_Refi
 *
 */
public class ExtendedRobolectricTestRunner extends RobolectricTestRunner {

    private static final int SDK_INT = Build.VERSION.SDK_INT;

    /**
     * Constructor.
     * @param <T> class type
     * @param testClass underTest
     * @throws InitializationError error
     */
    public <T> ExtendedRobolectricTestRunner(final Class<T> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected void bindShadowClasses() {
        super.bindShadowClasses();
        Robolectric.bindShadowClass(RoboSherlockActivityShadow.class);
    }

    @Override
    public void beforeTest(final Method method) {
        final int targetSdkVersion = robolectricConfig.getSdkVersion();
        //        setStaticValue(Build.VERSION.class, "SDK_INT", targetSdkVersion);
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", targetSdkVersion);
    }

    @Override
    public void afterTest(final Method method) {
        resetStaticState();
    }

    @Override
    public void resetStaticState() {
        //        setStaticValue(Build.VERSION.class, "SDK_INT", SDK_INT);
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", SDK_INT);
    }
}
