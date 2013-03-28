package com.cashflow.activity.components;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private void setUpActivityModule() {
        ActivityModule module = new ActivityModule(new ActivityProvider());

        module.addViewBinding(R.id.recurring_checkbox_area, checkBoxLayout);

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
