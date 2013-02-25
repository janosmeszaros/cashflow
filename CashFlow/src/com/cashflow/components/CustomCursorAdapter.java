package com.cashflow.components;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import android.annotation.SuppressLint;
import android.app.Application;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.widget.SimpleCursorAdapter;

import com.cashflow.R;
import com.google.inject.Inject;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    @SuppressLint("NewApi")
    @Inject
    public CustomCursorAdapter(Application app) {
        super(app, R.layout.list_statements_row, new MatrixCursor(new String[] { BaseColumns._ID, COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE }),
                new String[] { COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE }, new int[] { R.id.toptext, R.id.bottomtext }, 0);
    }
}
