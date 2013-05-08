package com.cashflow.bill.activity.list;

import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TO_VIEWS;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.activity.components.AbstractListFragment;
import com.cashflow.bill.database.BillService;
import com.cashflow.domain.Bill;
import com.google.inject.Inject;

/**
 * List bill fragment.
 * @author Janos_Gyula_Meszaros
 */
public class ListBillFragment extends AbstractListFragment implements OnClickListener {
    private static final Logger LOG = LoggerFactory.getLogger(ListBillFragment.class);

    @Inject
    private BillService billService;
    @InjectView(R.id.list_statement)
    private ListView list;

    private CursorAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        createListView();
    }

    private void createListView() {
        final MatrixCursor cursor = createCursor();
        adapter = createAdapter(cursor);
        list.setAdapter(adapter);
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

    private MatrixCursor createCursor() {
        final List<Bill> billList = getDataFromDatabase();
        final MatrixCursor cursor = new MatrixCursor(PROJECTION);
        for (final Bill bill : billList) {
            cursor.addRow(new String[] { bill.getBillId(), bill.getAmount(), bill.getDate(), bill.getPayedDate(),
                bill.getCategory().getName(),
                bill.getDeadlineDate(),
                bill.getNote(), String.valueOf(bill.isPayed()) });
        }
        return cursor;
    }

    private List<Bill> getDataFromDatabase() {
        return billService.getAllBills();
    }

    private CursorAdapter createAdapter(final MatrixCursor cursor) {
        final BillCursorAdapter adapter =
                new BillCursorAdapter(getActivity(), R.layout.list_bill_row, cursor, PROJECTION, TO_VIEWS);
        adapter.setCheckboxListener(this);
        adapter.setPayButtonOnClickListener(this);
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

    @Override
    protected void deleteButtonOnClick() {
        deleteAllSelectedBill();

        refreshListView();
        showWarning();
    }

    private void showWarning() {
        Toast.makeText(getActivity(), "Warning! Deletion has no effect for the expenses which are associated with payed bills.",
                Toast.LENGTH_LONG)
                .show();
    }

    private void deleteAllSelectedBill() {
        for (final String id : getSelectedIds()) {
            billService.delete(id);
        }
    }

    @Override
    public void onClick(final View view) {
        LOG.debug("Pay button pressed!");
        final String billId = getSelectedBillId(view);
        billService.payBill(billId);
        refreshView();
    }

    private void refreshView() {
        this.onResume();
    }

    private String getSelectedBillId(final View view) {
        final View row = (View) view.getParent().getParent();
        final TextView billId = (TextView) row.findViewById(R.id.row_id);
        return billId.getText().toString();
    }

}
