package com.cashflow.activity.components;

import android.database.MatrixCursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cashflow.R;

/**
 * Cursor adapter for chekbox on clicking. User have to set listener.
 * @author Janos_Gyula_Meszaros
 */
public class CustomCursorAdapter extends SimpleCursorAdapter {
    private OnCheckedChangeListener checkBoxListener;

    /**
     * Constructor.
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
    public CustomCursorAdapter(final FragmentActivity activity, final int listStatementsRow, final MatrixCursor cursor,
            final String[] projection, final int[] toViews) {
        super(activity, listStatementsRow, cursor, projection, toViews);
    }

    public void setCheckboxListener(final OnCheckedChangeListener listener) {
        this.checkBoxListener = listener;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.selectedCheckbox);
        checkBox.setOnCheckedChangeListener(checkBoxListener);

        return view;
    }

}
