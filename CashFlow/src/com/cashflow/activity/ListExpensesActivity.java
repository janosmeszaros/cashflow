package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.cashflow.R;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Class for the list expense activity.
 * @author Janos_Gyula_Meszaros
 */
public class ListExpensesActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListExpensesActivity.class);
    private String[] fromColumns = {COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE};
    private int[] toViews = {R.id.toptext, R.id.bottomtext};

    private SimpleCursorAdapter mAdapter;

    @Inject
    private StatementPersistentService service;

    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_statements);

        Cursor cursor = service.getStatement(StatementType.Expense);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_statements_row, cursor, fromColumns, toViews, 0);

        list.setAdapter(mAdapter);
    }

    /**
     * Edit button onClick method.
     * @param view
     *            {@link View}
     */
    public void onClick(View view) {
        LOG.debug("Edit button clicked");
    }
}
