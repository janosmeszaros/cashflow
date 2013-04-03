package com.cashflow.activity.components;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RecurringCheckBoxOnClickListenerTest {

    @Inject
    private RecurringCheckBoxOnClickListener underTest;
    private final Activity activity = new Activity();

    @Mock
    private CheckBox checkBox;
    @Mock
    private LinearLayout checkBoxLayout;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpActivityModule();
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    private void setUpActivityModule() {
        ActivityModule module = new ActivityModule(new ActivityProvider());

        module.addViewBinding(R.id.recurring_checkbox_area_income, checkBoxLayout);

        ActivityModule.setUp(this, module);
    }

    @Test
    public void testOnClickCheckedShouldStartInAnimationForCheckboxArea() {
        when(checkBox.isChecked()).thenReturn(true);
        when(checkBox.getContext()).thenReturn(activity);

        underTest.onClick(checkBox);

        verify(checkBoxLayout).startLayoutAnimation();
    }

    @Test
    public void testOnClickCheckedShouldStartOutAnimationForCheckboxArea() {
        when(checkBox.isChecked()).thenReturn(false);
        when(checkBox.getContext()).thenReturn(activity);

        underTest.onClick(checkBox);

        verify(checkBoxLayout).startLayoutAnimation();
    }
}
