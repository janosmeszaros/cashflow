package com.cashflow.activity;

import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.statement.database.StatementType.Expense;
import static com.cashflow.statement.database.StatementType.Income;

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
import com.cashflow.bill.activity.AddBillFragment;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.database.balance.Balance;
import com.cashflow.statement.activity.ListStatementActivity;
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
        findViewById(R.id.header_list_button).setVisibility(View.VISIBLE);
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

    /**
     * Add bill onClick method.
     * @param view
     *            Required for onclick.
     */
    public void addBill(View view) {
        Intent intent = new Intent(this, AddBillFragment.class);
        startActivity(intent);
    }

}
