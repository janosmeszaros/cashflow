package com.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.cashflow.database.statement.StatementDao;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Inject;

/**
 * Main activity.
 * @author Kornel_Refi
 */
public class MainActivity extends Activity {
    @Inject
    private StatementDao statementDao;

    @Inject
    private StatementPersistentService statementPersistentService;

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

    }

    /**
     * List expenses onClick method.
     * @param view
     *            Required for onclick.
     */
    public void listExpenses(View view) {

    }

}
