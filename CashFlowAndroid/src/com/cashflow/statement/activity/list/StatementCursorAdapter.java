package com.cashflow.statement.activity.list;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.components.CustomCursorAdapter;
import com.cashflow.domain.RecurringInterval;

/**
 * Cursor adapter for statements.
 * @author Janos_Gyula_Meszaros
 */
public class StatementCursorAdapter extends CustomCursorAdapter {

    /**
     * Constructor
     * @param activity
     *            activity
     * @param listStatementsRow
     *            listStatementsRow
     * @param cursor
     *            cursor
     * @param projection
     *            projection
     * @param toViews
     *            toViews
     */
    public StatementCursorAdapter(final FragmentActivity activity, final int listStatementsRow, final Cursor cursor,
            final String[] projection, final int[] toViews) {
        super(activity, listStatementsRow, cursor, projection, toViews);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        final Cursor cursor = getCursor();
        if (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_INTERVAL)) != RecurringInterval.none.toString()) {
            final TextView interval = (TextView) view.findViewById(R.id.row_interval);
            interval.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
