package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.statement.database.StatementType.Expense;
import static com.cashflow.statement.database.StatementType.Income;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.database.MatrixCursor;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ListStatementActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.activity.testutil.shadows.ActionBarSherlockRobolectric;
import com.cashflow.database.balance.Balance;
import com.cashflow.statement.database.StatementPersistenceService;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link MainActivity} test.
 * @author Kornel_Refi
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private static final String LIST_MENU_LABEL = "List";
    private static final String ADD_MENU_LABEL = "Add";
    @Mock
    private StatementPersistenceService service;
    @Mock
    private MatrixCursor matrixCursorMock;
    @Mock
    private Menu menu;
    @Mock
    private MenuItem listMenuItem;
    @Mock
    private MenuItem addMenuItem;

    private Balance balance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ActionBarSherlock.registerImplementation(ActionBarSherlockRobolectric.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockCompat.class);

        final ActivityModule module = new ActivityModule(new ListStatementActivityProvider());

        setUpMocks();

        module.addBinding(StatementPersistenceService.class, service);
        balance = Balance.getInstance(service);
        module.addBinding(Balance.class, balance);
        ActivityModule.setUp(this, module);

    }

    @After
    public void tearDown() {
        ActionBarSherlock.registerImplementation(ActionBarSherlockCompat.class);
        ActionBarSherlock.registerImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockRobolectric.class);
        TestGuiceModule.tearDown();
    }

    @Test
    public void testOnCreateOptionsMenuWhenCalledShouldAddListMenuItemAndSetUpAnIntentToIt() {
        final MainActivity activity = new MainActivity();
        activity.onCreate(null);

        activity.onCreateOptionsMenu(menu);

        verify(menu).add(0, 0, Menu.NONE, LIST_MENU_LABEL);
        verify(listMenuItem).setIcon(android.R.drawable.ic_menu_search);
        verify(listMenuItem).setIntent(new Intent(activity, ListActivity.class));
        verify(listMenuItem).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Test
    public void testOnCreateOptionsMenuWhenCalledShouldAddAddMenuItemAndSetUpAnIntentToIt() {
        final MainActivity activity = new MainActivity();
        activity.onCreate(null);

        activity.onCreateOptionsMenu(menu);

        verify(menu).add(0, 1, Menu.NONE, ADD_MENU_LABEL);
        verify(addMenuItem).setIcon(android.R.drawable.ic_menu_set_as);
        verify(addMenuItem).setIntent(new Intent(activity, ActionsActivity.class));
        verify(addMenuItem).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

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

    private void setUpMocks() {
        when(service.getStatement(Expense)).thenReturn(matrixCursorMock);
        when(service.getStatement(Income)).thenReturn(matrixCursorMock);

        when(matrixCursorMock.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(matrixCursorMock.isAfterLast()).thenReturn(false, true, false, true);
        when(matrixCursorMock.getLong(0)).thenReturn(1L, 2L);

        when(menu.add(anyInt(), anyInt(), anyInt(), eq(LIST_MENU_LABEL))).thenReturn(listMenuItem);
        when(menu.add(anyInt(), anyInt(), anyInt(), eq(ADD_MENU_LABEL))).thenReturn(addMenuItem);
    }
}
