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
import com.cashflow.activity.components.AbstractListFragment;
import com.cashflow.activity.components.CustomCursorAdapter;
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

    private CursorAdapter adapter;

    protected abstract List<Statement> getDataFromDatabase();

    @Override
    protected void deleteButtonOnClick() {
        deleteAllSelectedStatement();
        refreshListView();
    }

    private void deleteAllSelectedStatement() {
        for (final String id : getSelectedIds()) {
            final Statement statementToDelete = statementDAO.getStatementById(id);
            statementDAO.delete(statementToDelete);
        }
    }

    @Override
    protected ListView getList() {
        return list;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createListView();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshListView();
    }

    private void refreshListView() {
        final MatrixCursor cursor = createCursor();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private void createListView() {
        final MatrixCursor cursor = createCursor();
        adapter = createAdapter(cursor);
        list.setAdapter(adapter);
    }

    private MatrixCursor createCursor() {
        final List<Statement> data = getDataFromDatabase();
        final MatrixCursor cursor = fillUpCursor(data);
        return cursor;
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

    private CursorAdapter createAdapter(final MatrixCursor cursor) {
        final CustomCursorAdapter adapter =
                new StatementCursorAdapter(getActivity(), R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS);
        adapter.setCheckboxListener(this);
        return adapter;
    }

    protected StatementDAO getStatementDAO() {
        return statementDAO;
    }

}
