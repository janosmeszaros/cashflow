package com.cashflow.bill.activity;

import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TO_VIEWS;

import java.util.List;

import roboguice.inject.InjectView;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.cashflow.R;
import com.cashflow.dao.BillDAO;
import com.cashflow.domain.Bill;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

/**
 * List bill fragment.
 * @author Janos_Gyula_Meszaros
 */
public class ListBillFragment extends RoboSherlockFragment {

    @Inject
    private BillDAO billDAO;
    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<Bill> data = getDataFromDatabase();
        fillUpListView(data);
    }

    private List<Bill> getDataFromDatabase() {
        return billDAO.getAllBills();
    }

    private void fillUpListView(final List<Bill> data) {
        final MatrixCursor cursor = fillUpCursor(data);
        final SimpleCursorAdapter adapter = createAdapter(cursor);
        list.setAdapter(adapter);
    }

    private MatrixCursor fillUpCursor(final List<Bill> billList) {
        final MatrixCursor cursor = new MatrixCursor(PROJECTION);
        for (final Bill bill : billList) {
            cursor.addRow(new String[] { bill.getBillId(), bill.getAmount(), bill.getDate(), bill.getPayedDate(),
                bill.getCategory().getName(),
                bill.getDeadlineDate(),
                bill.getNote(), String.valueOf(bill.isPayed()) });
        }
        return cursor;
    }

    private SimpleCursorAdapter createAdapter(final MatrixCursor cursor) {
        final SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(getActivity(), R.layout.list_bill_row, cursor, PROJECTION, TO_VIEWS);
        return adapter;
    }
}
