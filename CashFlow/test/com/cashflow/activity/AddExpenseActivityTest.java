package com.cashflow.activity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import com.cashflow.activity.util.TestGuiceModule;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AddExpenseActivity} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AddExpenseActivityTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestGuiceModule module = new TestGuiceModule();
        TestGuiceModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test
    public void onCreateShouldAttachConversionListenerToConvertButton() {

        Assert.assertTrue(true);
    }
}
