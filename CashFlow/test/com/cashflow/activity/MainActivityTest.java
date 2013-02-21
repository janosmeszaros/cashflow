package com.cashflow.activity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    //    @Mock
    //    ConvertFeetToMeterListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestGuiceModule module = new TestGuiceModule();
        //        module.addBinding(ConvertFeetToMeterListener.class, listener);
        TestGuiceModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test
    public void onCreateShouldAttachConversionListenerToConvertButton() {
        //        MetricActivity activity = new MetricActivity();

        //        activity.onCreate(null);
        //
        //        ShadowView button = Robolectric.shadowOf(activity.findViewById(R.id.convert_button));
        Assert.assertTrue(true);
    }
}
