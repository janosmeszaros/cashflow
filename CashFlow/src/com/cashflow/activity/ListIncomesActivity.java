package com.cashflow.activity;

import static com.cashflow.constants.EditConstants.AMOUNT_EXTRA;
import static com.cashflow.constants.EditConstants.DATE_EXTRA;
import static com.cashflow.constants.EditConstants.ID_EXTRA;
import static com.cashflow.constants.EditConstants.IS_EDITED_EXTRA;
import static com.cashflow.constants.EditConstants.NOTE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Basic class to list incomes.
 * @author Janos_Gyula_Meszaros
 */
public class ListIncomesActivity extends RoboListActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListIncomesActivity.class);
    private String[] fromColumns = { AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE };
    private int[] toViews = { R.id.row_id, R.id.row_amount, R.id.row_date, R.id.row_note };

    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistentService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_statements);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_incomes, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LOG.debug("ListIncomesActivity's focus changed to: " + hasFocus);
        if (hasFocus) {
            Cursor cursor = service.getStatement(StatementType.Income);

            mAdapter = new SimpleCursorAdapter(this,
                    R.layout.list_statements_row, cursor,
                    fromColumns, toViews, 0);
            setListAdapter(mAdapter);
        }
    }

    /**
     * Indicates the edit income interface
     * @param view
     *            Needed by onClick event.
     */
    public void onClick(View view) {
        LOG.debug("Edit button clicked");
        Intent intent = new Intent(this, EditIncomeActivity.class);
        addExtras((View) view.getParent(), intent);
        startActivity(intent);
    }

    private void addExtras(View view, Intent intent) {
        TextView id = (TextView) view.findViewById(R.id.row_id);
        TextView amount = (TextView) view.findViewById(R.id.row_amount);
        TextView note = (TextView) view.findViewById(R.id.row_note);
        TextView date = (TextView) view.findViewById(R.id.row_date);

        intent.putExtra(IS_EDITED_EXTRA, true);

        intent.putExtra(ID_EXTRA, id.getText());
        intent.putExtra(AMOUNT_EXTRA, amount.getText());
        intent.putExtra(NOTE_EXTRA, note.getText());
        intent.putExtra(DATE_EXTRA, date.getText());

    }
}
