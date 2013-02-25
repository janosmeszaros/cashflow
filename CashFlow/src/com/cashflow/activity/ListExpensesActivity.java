package com.cashflow.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cashflow.R;
import com.cashflow.components.CustomCursorAdapter;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Class for the list expense activity.
 * @author Janos_Gyula_Meszaros
 */
public class ListExpensesActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListExpensesActivity.class);

    @Inject
    private StatementPersistentService service;
    @Inject
    private CustomCursorAdapter mAdapter;
    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_statements);
        System.out.println("Start creating view");

        Cursor cursor = service.getStatement(StatementType.Expense);
        mAdapter.changeCursor(cursor);

        list.setAdapter(mAdapter);
        System.out.println("Adapter count added to list: " + mAdapter.getCursor().getCount());
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
