package com.cashflow.bill.activity;

import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TO_VIEWS;

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
import com.cashflow.dao.BillDAO;
import com.cashflow.domain.Bill;
import com.google.inject.Inject;

/**
 * List bill fragment.
 * @author Janos_Gyula_Meszaros
 */
public class ListBillFragment extends AbstractListFragment {

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
        final CursorAdapter adapter = createAdapter(cursor);
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

    private CursorAdapter createAdapter(final MatrixCursor cursor) {
        final CustomCursorAdapter adapter =
                new CustomCursorAdapter(getActivity(), R.layout.list_bill_row, cursor, PROJECTION, TO_VIEWS);
        adapter.setListener(this);
        return adapter;
    }

    @Override
    protected ListView getList() {
        return list;
    }

    @Override
    protected void editButtonOnClick() {
        // TODO Auto-generated method stub

    }
}
