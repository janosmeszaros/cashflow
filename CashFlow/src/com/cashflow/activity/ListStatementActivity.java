package com.cashflow.activity;

import static com.cashflow.constants.Constants.AMOUNT_EXTRA;
import static com.cashflow.constants.Constants.DATE_EXTRA;
import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.EXPENSE_EXTRA;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.NOTE_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

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
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.google.inject.Inject;

/**
 * Basic class to list incomes.
 * @author Janos_Gyula_Meszaros
 */
public class ListStatementActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ListStatementActivity.class);
    private String[] fromColumns = {AbstractStatement._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE, COLUMN_NAME_NOTE};
    private int[] toViews = {R.id.row_id, R.id.row_amount, R.id.row_date, R.id.row_note};

    private StatementType type;
    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistentService service;
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
     * Indicates the edit income interface
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

        intent.putExtra(ID_EXTRA, id.getText());
        intent.putExtra(AMOUNT_EXTRA, amount.getText());
        intent.putExtra(NOTE_EXTRA, note.getText());
        intent.putExtra(DATE_EXTRA, date.getText());
        intent.putExtra(STATEMENT_TYPE_EXTRA, statementType());
    }

    private String statementType() {
        return type.equals(StatementType.Income) ? INCOME_EXTRA : EXPENSE_EXTRA;
    }

    private boolean isEditActivity(int requestCode, int resultCode) {
        return resultCode == RESULT_OK && requestCode == EDIT_ACTIVITY_CODE;
    }

    @SuppressLint("NewApi")
    private void getDataFromDatabase() {
        LOG.debug("Starting query for type: " + type);

        Cursor cursor = service.getStatement(type);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_statements_row, cursor, fromColumns, toViews);
        list.setAdapter(mAdapter);

        LOG.debug("Query has done.");
    }

    private void setTitle() {
        if (type.equals(StatementType.Income)) {
            setTitle(R.string.title_activity_list_incomes);
        } else {
            setTitle(R.string.title_activity_list_expenses);
        }
    }

    private void setStatementType() {
        if (getIntent().getStringExtra(STATEMENT_TYPE_EXTRA).equals(INCOME_EXTRA)) {
            type = StatementType.Income;
        } else {
            type = StatementType.Expense;
        }
    }
}
