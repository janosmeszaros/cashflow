package com.cashflow.bill.activity.list;

import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.components.CustomCursorAdapter;

/**
 * Bill cursor adapter.
 * @author Janos_Gyula_Meszaros
 */
public class BillCursorAdapter extends CustomCursorAdapter {

    private static final String IS_PAYED = "true";
    private OnClickListener payButtonOnClickListener;
    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());

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
    public BillCursorAdapter(final FragmentActivity activity, final int listStatementsRow, final Cursor cursor,
            final String[] projection, final int[] toViews) {
        super(activity, listStatementsRow, cursor, projection, toViews);
    }

    public void setPayButtonOnClickListener(final OnClickListener listener) {
        this.payButtonOnClickListener = listener;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        final Button payButton = (Button) view.findViewById(R.id.row_pay_button);
        payButton.setOnClickListener(payButtonOnClickListener);

        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final String isPayed = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IS_PAYED));

        if (IS_PAYED.equals(isPayed)) {
            setBackground(view, R.color.green);
            showPayedDate(view, cursor);
            hidePayButton(view);
        } else {
            final DateTime deadLineDate = getDeadlineDate(cursor);
            if (deadLineDate.isAfterNow()) {
                setBackground(view, R.color.orange);
            } else {
                setBackground(view, R.color.red);
            }

        }
    }

    private DateTime getDeadlineDate(final Cursor cursor) {
        final String deadlineDate = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_DEADLINE));
        return formatter.parseDateTime(deadlineDate);
    }

    private void setBackground(final View view, final int color) {
        view.setBackgroundColor(view.getResources().getColor(color));
    }

    private void hidePayButton(final View view) {
        final Button payButton = (Button) view.findViewById(R.id.row_pay_button);
        payButton.setVisibility(View.GONE);
    }

    private void showPayedDate(final View view, final Cursor cursor) {
        final TextView payedDate = (TextView) view.findViewById(R.id.row_payedDate);
        final String payedDateString = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE_PAYED));

        payedDate.setVisibility(View.VISIBLE);
        payedDate.setText(payedDateString);
    }
}
