package com.cashflow.statement.activity.list;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TO_VIEWS;

import java.util.List;

import roboguice.inject.InjectView;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cashflow.R;
import com.cashflow.activity.components.CustomCursorAdapter;
import com.cashflow.activity.components.AbstractListFragment;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;

/**
 * Basic class to list statements.
 * @author Janos_Gyula_Meszaros
 */
public abstract class ListStatementFragment extends AbstractListFragment {

    @Inject
    private StatementDAO statementDAO;
    @InjectView(R.id.list_statement)
    private ListView list;

    protected abstract List<Statement> getDataFromDatabase();

    @Override
    protected ListView getList() {
        return list;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    private void fillUpListView(final List<Statement> data) {
        final MatrixCursor cursor = fillUpCursor(data);
        final CursorAdapter adapter = createAdapter(cursor);
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<Statement> data = getDataFromDatabase();
        fillUpListView(data);
    }

    private MatrixCursor fillUpCursor(final List<Statement> statementList) {
        final MatrixCursor cursor = new MatrixCursor(PROJECTION);
        for (final Statement statement : statementList) {
            cursor.addRow(new String[] { statement.getStatementId(), statement.getAmount(), statement.getCategory().getName(),
                statement.getDate(),
                statement.getNote(), statement.getRecurringInterval().toString() });
        }
        return cursor;
    }

    protected StatementDAO getStatementDAO() {
        return statementDAO;
    }

    private CursorAdapter createAdapter(final MatrixCursor cursor) {
        final CustomCursorAdapter adapter =
                new CustomCursorAdapter(getActivity(), R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS);
        adapter.setListener(this);
        return adapter;
    }

}
