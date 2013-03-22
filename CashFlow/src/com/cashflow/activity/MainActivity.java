package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.statement.StatementType.Expense;
import static com.cashflow.database.statement.StatementType.Income;

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
import com.cashflow.database.statement.RecurringIncomeScheduler;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduler.schedule();
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
            balance.countBalance();
            String value = String.valueOf(balance.getBalance());
            balanceText.setText(value);
        }
    }

    /**
     * Add new income onClick method.
     * @param view
     *            Required for onClick.
     */
    public void addIncome(View view) {
        Intent intent = new Intent(this, AddStatementActivity.class);
        intent.putExtra(STATEMENT_TYPE_EXTRA, Income.toString());
        startActivity(intent);

    }

    /**
     * Add new Expense onClick method.
     * @param view
     *            Required for onClick.
     */
    public void addExpense(View view) {
        Intent intent = new Intent(this, AddStatementActivity.class);
        intent.putExtra(STATEMENT_TYPE_EXTRA, Expense.toString());
        startActivity(intent);
    }

    /**
     * List incomes onClick method.
     * @param view
     *            Required for onClick.
     */
    public void listIncomes(View view) {
        Intent intent = new Intent(this, ListStatementActivity.class);
        intent.putExtra(STATEMENT_TYPE_EXTRA, Income.toString());
        startActivity(intent);
    }

    /**
     * List expenses onClick method.
     * @param view
     *            Required for onClick.
     */
    public void listExpenses(View view) {
        Intent intent = new Intent(this, ListStatementActivity.class);
        intent.putExtra(STATEMENT_TYPE_EXTRA, Expense.toString());
        startActivity(intent);
    }

    /**
     * Create categories onClick method.
     * @param view
     *            Required for onClick.
     */
    public void createCategories(View view) {
        Intent intent = new Intent(this, CreateCategoryActivity.class);
        startActivity(intent);
    }

}
