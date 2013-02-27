package com.cashflow.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.view.View;

import com.cashflow.R;
import com.cashflow.activity.util.TestGuiceModule;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

/**
 * {@link MainActivity} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    // @Mock
    // ConvertFeetToMeterListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestGuiceModule module = new TestGuiceModule();
        // module.addBinding(ConvertFeetToMeterListener.class, listener);
        TestGuiceModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test
    public void addIncomeButtonClickShouldCreateAddIncomeActivity() {
        MainActivity activity = new MainActivity();

        activity.onCreate(null);

        View addIncome = activity.findViewById(R.id.submitButton);

        addIncome.performClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(AddIncomeActivity.class.getName()));

    }

    @Test
    public void addExpenseButtonClickShouldCreateAddExpenseeActivity() {
        MainActivity activity = new MainActivity();

        activity.onCreate(null);

        View addExpense = activity.findViewById(R.id.addExpenseButton);

        addExpense.performClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(AddExpenseActivity.class.getName()));

    }

    @Test
    public void listExpensesButtonClickShouldCreateListExpensesActivity() {
        MainActivity activity = new MainActivity();

        activity.onCreate(null);

        View listExpenses = activity.findViewById(R.id.listExpensesButton);

        listExpenses.performClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ListExpensesActivity.class.getName()));

    }

    @Test
    public void listIncomesButtonClickShouldCreateListIncomesActivity() {
        MainActivity activity = new MainActivity();

        activity.onCreate(null);

        View listIncomes = activity.findViewById(R.id.listIncomesButton);

        listIncomes.performClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ListIncomesActivity.class.getName()));

    }
}
