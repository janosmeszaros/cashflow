package com.cashflow;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import roboguice.activity.RoboListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;

import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Class for the list expense activity.
 * @author Janos_Gyula_Meszaros
 */
public class ListExpenses extends RoboListActivity {
    private String[] fromColumns = { COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE };
    private int[] toViews = { R.id.toptext, R.id.bottomtext };

    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistentService service;

    // /**
    // * Simple constructor which takes provider to database connection.
    // * @param provider
    // * to takes database connection.
    // */
    // @Inject
    // public ListExpenses(SQLiteDbProvider provider) {
    // this.provider = provider;
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expenses);

        Cursor cursor = service.getStatement(StatementType.Expense);

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_expenses_row, cursor,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);
    }

}
