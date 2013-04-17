package com.cashflow.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Intent;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.ActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.activity.testutil.shadows.ActionBarSherlockRobolectric;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.cashflow.service.Balance;
import com.cashflow.service.RecurringIncomeScheduler;
import com.cashflow.statement.database.AndroidStatementDAO;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

/**
 * {@link MainActivity} test.
 * @author Janos_Gyula_Meszaros
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private static final String LIST_MENU_LABEL = "List";
    private static final String ADD_MENU_LABEL = "Add";
    private static final String ID_STR = "0";
    private static final String AMOUNT_STR = "1234";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "cat";
    private static final String NOTE = "note";
    private static final String DATE_STR = "2012.01.01";
    private static final String INTERVAL_STR = "daily";

    private final Statement expenseStatement = Statement.builder(AMOUNT_STR, DATE_STR).note(NOTE).type(StatementType.Expense)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).id(ID_STR).build();
    private final Statement incomeStatement = Statement.builder(AMOUNT_STR, DATE_STR).note(NOTE).type(StatementType.Income)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).id(ID_STR)
            .recurringInterval(RecurringInterval.valueOf(INTERVAL_STR)).build();

    private final List<Statement> expenseList = new ArrayList<Statement>() {
        {
            add(expenseStatement);
        }
    };

    private final List<Statement> incomeList = new ArrayList<Statement>() {
        {
            add(incomeStatement);
        }
    };

    @Mock
    private AndroidStatementDAO satementDao;
    @Mock
    private RecurringIncomeScheduler scheduler;
    @Mock
    private Menu menu;
    @Mock
    private MenuItem listMenuItem;
    @Mock
    private MenuItem addMenuItem;

    private Balance balance;
    private MainActivity underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ActionBarSherlock.registerImplementation(ActionBarSherlockRobolectric.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockCompat.class);

        final ActivityModule module = new ActivityModule(new ActivityProvider());

        setUpMocks();

        module.addBinding(StatementDAO.class, satementDao);
        balance = Balance.getInstance(satementDao);
        module.addBinding(Balance.class, balance);
        module.addBinding(RecurringIncomeScheduler.class, scheduler);
        ActivityModule.setUp(this, module);

        underTest = new MainActivity();

    }

    @After
    public void tearDown() {
        ActionBarSherlock.registerImplementation(ActionBarSherlockCompat.class);
        ActionBarSherlock.registerImplementation(ActionBarSherlockNative.class);
        ActionBarSherlock.unregisterImplementation(ActionBarSherlockRobolectric.class);
        TestGuiceModule.tearDown();
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetContentViewToMainActivity() {
        underTest.onCreate(null);

        final ShadowActivity shadowActivity = Robolectric.shadowOf(underTest);
        assertThat(shadowActivity.getContentView().getId(), equalTo(R.id.main_activity_layout));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetTitleToAppName() {
        underTest.onCreate(null);

        assertThat(underTest.getTitle().toString(), equalTo(underTest.getString(R.string.app_name)));
    }

    @Test
    public void testOnCreateWhenCalledThenShouldCallRecurringIncomeSchedulersScheduleMethod() {
        underTest.onCreate(null);

        verify(scheduler).schedule();
    }

    @Test
    public void testOnCreateOptionsMenuWhenCalledShouldAddListMenuItemAndSetUpAnIntentToIt() {
        underTest.onCreate(null);

        underTest.onCreateOptionsMenu(menu);

        verify(menu).add(0, 0, Menu.NONE, LIST_MENU_LABEL);
        verify(listMenuItem).setIcon(android.R.drawable.ic_menu_search);
        verify(listMenuItem).setIntent(new Intent(underTest, ListActivity.class));
        verify(listMenuItem).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Test
    public void testOnCreateOptionsMenuWhenCalledShouldAddAddMenuItemAndSetUpAnIntentToIt() {
        underTest.onCreate(null);

        underTest.onCreateOptionsMenu(menu);

        verify(menu).add(0, 1, Menu.NONE, ADD_MENU_LABEL);
        verify(addMenuItem).setIcon(android.R.drawable.ic_menu_set_as);
        verify(addMenuItem).setIntent(new Intent(underTest, ActionsActivity.class));
        verify(addMenuItem).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Test
    public void testOnWindowFocusChangedWhenFocusIsChanged() {
        underTest.onCreate(null);
        final TextView balanceText = (TextView) underTest.findViewById(R.id.textViewBalanceAmount);
        balanceText.setText("0");

        underTest.onWindowFocusChanged(true);

        assertThat(balanceText.getText().toString(), equalTo("0.0"));
    }

    @Test
    public void testOnWindowFocusChangedWhenFocusIsNotChanged() {
        underTest.onCreate(null);
        final TextView balanceText = (TextView) underTest.findViewById(R.id.textViewBalanceAmount);
        final String initBalance = String.valueOf(balance.getBalance());
        balanceText.setText(initBalance);
        balance.countBalance();

        underTest.onWindowFocusChanged(false);

        assertThat(balanceText.getText().toString(), equalTo(initBalance));
    }

    private void setUpMocks() {
        when(satementDao.getExpenses()).thenReturn(expenseList);
        when(satementDao.getIncomes()).thenReturn(incomeList);

        when(menu.add(anyInt(), anyInt(), anyInt(), eq(LIST_MENU_LABEL))).thenReturn(listMenuItem);
        when(menu.add(anyInt(), anyInt(), anyInt(), eq(ADD_MENU_LABEL))).thenReturn(addMenuItem);
    }
}
