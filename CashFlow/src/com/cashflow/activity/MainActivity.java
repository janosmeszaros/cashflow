package com.cashflow.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Inject;

/**
 * Main activity.
 * @author Kornel_Refi
 */
public class MainActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);

    @Inject
    private StatementPersistentService statementPersistentService;

    @InjectView(R.id.textViewBalanceAmount)
    private TextView balanceText;

    @Inject
    private Balance balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LOG.debug("MainActivity's focus changed to: " + hasFocus);
        if (hasFocus) {
            String value = String.valueOf(balance.getBalance());
            balanceText.setText(value);
        }
    }

    /**
     * Add new income onClick method.
     * @param view
     *            Required for onclick.
     */
    public void addIncome(View view) {
        Intent intent = new Intent(this, AddIncomeActivity.class);
        startActivity(intent);

    }

    /**
     * Add new Expense onClick method.
     * @param view
     *            Required for onclick.
     */
    public void addExpense(View view) {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        startActivity(intent);
    }

    /**
     * List incomes onClick method.
     * @param view
     *            Required for onclick.
     */
    public void listIncomes(View view) {
        Intent intent = new Intent(this, ListIncomesActivity.class);
        startActivity(intent);
    }

    /**
     * List expenses onClick method.
     * @param view
     *            Required for onclick.
     */
    public void listExpenses(View view) {
        Intent intent = new Intent(this, ListExpensesActivity.class);
        startActivity(intent);
    }

}