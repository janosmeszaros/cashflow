package com.cashflow.activity.components;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
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
import com.cashflow.activity.testutil.AddIncomeStatementActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAnimation;

@RunWith(RobolectricTestRunner.class)
public class RecurringCheckBoxOnClickListenerTest {

    @Inject
    private RecurringCheckBoxOnClickListener underTest;
    @Inject
    private Activity activity;

    @Mock
    private CheckBox checkBox;
    @Mock
    private LinearLayout checkBoxLayout;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpActivityModule();
        underTest = new RecurringCheckBoxOnClickListener();
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    private void setUpActivityModule() {
        final ActivityModule module = new ActivityModule(new AddIncomeStatementActivityProvider());
        module.addViewBinding(R.id.recurring_checkbox_area_income, checkBoxLayout);

        ActivityModule.setUp(this, module);
    }

    @Test
    public void testOnClickCheckedShouldStartInAnimationForCheckboxArea() throws InterruptedException {
        when(checkBox.isChecked()).thenReturn(true);
        when(checkBox.getContext()).thenReturn(activity);

        underTest.onClick(checkBox);

        final LinearLayout layout = (LinearLayout) activity.findViewById(R.id.recurring_checkbox_area_income);
        Assert.assertThat(layout.getVisibility(), equalTo(INVISIBLE));

        final ShadowAnimation shadowAnimation = Robolectric.shadowOf(layout.getAnimation());
        shadowAnimation.invokeEnd();

        Assert.assertThat(layout.getVisibility(), equalTo(VISIBLE));
    }

    @Test
    public void testOnClickCheckedShouldStartOutAnimationForCheckboxArea() {
        final LinearLayout layout = (LinearLayout) activity.findViewById(R.id.recurring_checkbox_area_income);
        layout.setVisibility(VISIBLE);
        when(checkBox.isChecked()).thenReturn(false);
        when(checkBox.getContext()).thenReturn(activity);

        underTest.onClick(checkBox);

        Assert.assertThat(layout.getVisibility(), equalTo(VISIBLE));

        final ShadowAnimation shadowAnimation = Robolectric.shadowOf(layout.getAnimation());
        shadowAnimation.invokeEnd();

        Assert.assertThat(layout.getVisibility(), equalTo(INVISIBLE));
    }
}
