package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;

import com.cashflow.R;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Basic class to list incomes.
 * @author Janos_Gyula_Meszaros
 */
public class ListIncomesActivity extends RoboListActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListIncomesActivity.class);
    private String[] fromColumns = { COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE };
    private int[] toViews = { R.id.toptext, R.id.bottomtext };

    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistentService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_statements);

        Cursor cursor = service.getStatement(StatementType.Income);

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_statements_row, cursor,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_incomes, menu);
        return true;
    }

    /**
     * anyád
     * @param view
     *            anyád
     */
    public void onClick(View view) {
        LOG.debug("Edit button clicked");
    }

}
