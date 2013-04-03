package com.cashflow.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.cashflow.database.balance.Balance;
import com.cashflow.statement.database.RecurringIncomeScheduler;
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

    @Inject
    private Balance balance;
    @Inject
    private RecurringIncomeScheduler scheduler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.main_header_selector));
        setTitle(getString(R.string.app_name));
        scheduler.schedule();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, Menu.NONE, "List").setIcon(android.R.drawable.ic_menu_search).setIntent(new Intent(this, ListActivity.class))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, 1, Menu.NONE, "Add").setIcon(android.R.drawable.ic_menu_set_as).setIntent(new Intent(this, ActionsActivity.class))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        LOG.debug("MainActivity's focus changed to: " + hasFocus);
        if (hasFocus) {
            balance.countBalance();
            final String value = String.valueOf(balance.getBalance());
            balanceText.setText(value);
        }
    }

}
