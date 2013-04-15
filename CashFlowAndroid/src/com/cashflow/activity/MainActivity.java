package com.cashflow.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.cashflow.database.balance.Balance;
import com.cashflow.service.RecurringIncomeScheduler;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;

/**
 * Main activity.
 * @author Kornel_Refi
 */
public class MainActivity extends RoboSherlockActivity {
    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);

    @InjectView(R.id.textViewBalanceAmount)
    private TextView balanceText;
    @InjectView(R.id.textViewIncomeAmount)
    private TextView incomeText;
    @InjectView(R.id.textViewExpenseAmount)
    private TextView expenseText;

    @Inject
    private Balance balance;
    @Inject
    private RecurringIncomeScheduler scheduler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setActionBar();
        setTitle(getString(R.string.app_name));
        scheduler.schedule();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        createListMenuItem(menu);
        createAddMenuItem(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void createAddMenuItem(final Menu menu) {
        final MenuItem addItem = menu.add(0, 1, Menu.NONE, "Add");
        addItem.setIcon(android.R.drawable.ic_menu_set_as);
        addItem.setIntent(new Intent(this, ActionsActivity.class));
        addItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    private void createListMenuItem(final Menu menu) {
        final MenuItem listItem = menu.add(0, 0, Menu.NONE, "List");
        listItem.setIcon(android.R.drawable.ic_menu_search);
        listItem.setIntent(new Intent(this, ListActivity.class));
        listItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        LOG.debug("MainActivity's focus changed to: " + hasFocus);
        if (hasFocus) {
            balance.countBalance();
            final String value = String.valueOf(balance.getBalance());
            balanceText.setText(value);
            incomeText.setText(String.valueOf(balance.getIncomes().doubleValue()));
            expenseText.setText("- " + String.valueOf(balance.getExpenses().doubleValue()));
        }
    }

    private void setActionBar() {
        final Drawable drawable = getResources().getDrawable(R.drawable.main_header_selector);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(drawable);
    }

    private void setView() {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_main);
    }

}
