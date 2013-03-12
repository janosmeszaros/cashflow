package com.cashflow.activity;

import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.INTERVAL_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TO_VIEWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Basic class to list statements. The type is setted from intent's <code>STATEMENT_TYPE_EXTRA</code> extra.
 * @author Janos_Gyula_Meszaros 
 */
public class ListStatementActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListStatementActivity.class);

    private StatementType type;
    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistenceService service;
    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("ListStatementActivity is creating...");

        setContentView(R.layout.activity_list_statements);
        setStatementType();
        setTitle();
        getDataFromDatabase();

        LOG.debug("ListStatementActivity has created with type: " + type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_incomes, menu);
        return true;
    }

    /**
     * Starts the edit statement interface. Add actual values to the {@link EditStatementActivity}'s 
     * intent under the proper extra.
     * @param view
     *            Needed by onClick event.
     */
    public void onClick(View view) {
        LOG.debug("Edit button clicked");
        Intent intent = new Intent(this, EditStatementActivity.class);
        addExtras((View) view.getParent(), intent);
        startActivityForResult(intent, EDIT_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.debug("ListStatementActivity's onActivityResult method called with params: \nrequestCode: " + requestCode + "\nresultCode: "
                + requestCode);
        if (isEditActivity(requestCode, resultCode)) {
            getDataFromDatabase();
        }
    }

    private void addExtras(View view, Intent intent) {
        TextView id = (TextView) view.findViewById(R.id.row_id);
        TextView amount = (TextView) view.findViewById(R.id.row_amount);
        TextView note = (TextView) view.findViewById(R.id.row_note);
        TextView date = (TextView) view.findViewById(R.id.row_date);
        TextView interval = (TextView) view.findViewById(R.id.row_interval);

        intent.putExtra(ID_EXTRA, id.getText());
        intent.putExtra(AMOUNT_EXTRA, amount.getText());
        intent.putExtra(NOTE_EXTRA, note.getText());
        intent.putExtra(DATE_EXTRA, date.getText());
        intent.putExtra(INTERVAL_EXTRA, interval.getText());
        intent.putExtra(STATEMENT_TYPE_EXTRA, statementType());
    }

    private String statementType() {
        return type.isIncome() ? INCOME_EXTRA : EXPENSE_EXTRA;
    }

    private boolean isEditActivity(int requestCode, int resultCode) {
        return resultCode == RESULT_OK && requestCode == EDIT_ACTIVITY_CODE;
    }

    @SuppressLint("NewApi")
    private void getDataFromDatabase() {
        LOG.debug("Starting query for type: " + type);

        Cursor cursor = service.getStatement(type);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS);
        list.setAdapter(mAdapter);

        LOG.debug("Query has done.");
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_list_incomes);
        } else {
            setTitle(R.string.title_activity_list_expenses);
        }
    }

    private void setStatementType() {
        Intent intent = getIntent();
        type = StatementType.valueOf(intent.getStringExtra(STATEMENT_TYPE_EXTRA));
    }
}
