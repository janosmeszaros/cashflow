package com.cashflow.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.database.balance.Balance;
import com.cashflow.statement.database.RecurringIncomeScheduler;
import com.google.inject.Inject;

/**
 * Main activity.
 * @author Kornel_Refi
 */
public class MainActivity extends RoboActivity {
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
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title_bar);

        scheduler.schedule();

        findViewById(R.id.header_add_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ActionsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.header_list_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivity(intent);
            }
        });

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
