package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.statement.database.StatementType.Expense;
import static com.cashflow.statement.database.StatementType.Income;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.MatrixCursor;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ListStatementActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.database.balance.Balance;
import com.cashflow.statement.database.RecurringIncomeScheduler;
import com.cashflow.statement.database.StatementPersistenceService;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link MainActivity} test.
 * @author Kornel_Refi
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Mock
    private StatementPersistenceService service;
    @Mock
    private MatrixCursor matrixCursorMock;
    @Mock
    private RecurringIncomeScheduler scheduler;
    private Balance balance;

    @Before
    public void setUp() {
        //        ActionBarSherlock.registerImplementation(ActionBarSherlockRobolectric.class);
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new ListStatementActivityProvider());

        when(service.getStatement(Expense)).thenReturn(matrixCursorMock);
        when(service.getStatement(Income)).thenReturn(matrixCursorMock);

        when(matrixCursorMock.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(matrixCursorMock.isAfterLast()).thenReturn(false, true, false, true);
        when(matrixCursorMock.getLong(0)).thenReturn(1L, 2L);

        module.addBinding(StatementPersistenceService.class, service);
        balance = Balance.getInstance(service);
        module.addBinding(Balance.class, balance);
        ActivityModule.setUp(this, module);

    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    //    @Test
    //    public void addIncomeButtonClickShouldCreateAddIncomeActivity() {
    //        MainActivity activity = new MainActivity();
    //        activity.onCreate(null);
    //        View addIncome = activity.findViewById(R.id.submitButton);
    //
    //        addIncome.performClick();
    //        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
    //        Intent startedIntent = shadowActivity.getNextStartedActivity();
    //        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
    //        assertThat(shadowIntent.getComponent().getClassName(), equalTo(AddStatementFragment.class.getName()));
    //    }

    @Test
    public void testOnWindowFocusChangedWhenFocusIsChanged() {
        // Have to mock again because onWindowFocusChanged invoked by onCreate.
        when(matrixCursorMock.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(matrixCursorMock.isAfterLast()).thenReturn(false, true, false, true);
        when(matrixCursorMock.getDouble(0)).thenReturn(1D, 2D);
        final MainActivity activity = new MainActivity();
        activity.onCreate(null);
        final TextView balanceText = (TextView) activity.findViewById(R.id.textViewBalanceAmount);
        balanceText.setText("0");

        activity.onWindowFocusChanged(true);

        assertThat(balanceText.getText().toString(), equalTo("1.0"));
    }

    @Test
    public void testOnWindowFocusChangedWhenFocusIsNotChanged() {
        final MainActivity activity = new MainActivity();
        activity.onCreate(null);
        final TextView balanceText = (TextView) activity.findViewById(R.id.textViewBalanceAmount);
        final String initBalance = String.valueOf(balance.getBalance());
        balanceText.setText(initBalance);
        balance.countBalance();

        activity.onWindowFocusChanged(false);

        assertThat(balanceText.getText().toString(), equalTo(initBalance));
    }

}
