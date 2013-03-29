package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TO_VIEWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.google.inject.Inject;

/**
 * Basic class to list statements. The type is setted from intent's <code>STATEMENT_TYPE_EXTRA</code> extra.
 * @author Janos_Gyula_Meszaros 
 */
public class ListStatementFragment extends RoboFragment {
    private static final Logger LOG = LoggerFactory.getLogger(ListStatementFragment.class);

    private StatementType type;
    private SimpleCursorAdapter mAdapter;
    @Inject
    private StatementPersistenceService statementService;

    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LOG.debug("ListStatementActivity is creating...");

        setStatementType();
        getDataFromDatabase();

        LOG.debug("ListStatementActivity has created with type: " + type);
    }

    /**
     * Starts the edit statement interface. Add actual values to the {@link EditStatementActivity}'s 
     * intent under the proper extra.
     * @param view
     *            Needed by onClick event.
     */
    public void editButtonOnClick(final View view) {
        LOG.debug("Edit button clicked");
        final Intent intent = new Intent(this.getActivity(), EditStatementActivity.class);
        addExtras((View) view.getParent(), intent);
        startActivityForResult(intent, EDIT_ACTIVITY_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.debug("ListStatementActivity's onActivityResult method called with params: \nrequestCode: " + requestCode + "\nresultCode: "
                + requestCode);
        if (isEditActivity(requestCode, resultCode)) {
            getDataFromDatabase();
        }
    }

    private void addExtras(final View view, final Intent intent) {
        final TextView id = (TextView) view.findViewById(R.id.row_id);

        intent.putExtra(ID_EXTRA, id.getText());
    }

    private boolean isEditActivity(final int requestCode, final int resultCode) {
        return resultCode == Activity.RESULT_OK && requestCode == EDIT_ACTIVITY_CODE;
    }

    @SuppressLint("NewApi")
    private void getDataFromDatabase() {
        LOG.debug("Starting query for type: " + type);

        final Cursor cursor = statementService.getStatement(type);

        mAdapter = new SimpleCursorAdapter(this.getActivity(), R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS);
        list.setAdapter(mAdapter);

        LOG.debug("Query has done.");
    }

    private void setStatementType() {
        final Bundle bundle = getArguments();
        type = StatementType.valueOf(bundle.getString(STATEMENT_TYPE_EXTRA));
    }
}
