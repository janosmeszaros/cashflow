package com.cashflow.bill.list;

import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TO_VIEWS;

import org.junit.Before;
import org.mockito.Mock;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;

import com.cashflow.R;
import com.cashflow.bill.activity.list.BillCursorAdapter;

/**
 * Test for {@link BillCursorAdapter}
 * @author Janos_Gyula_Meszaros
 *
 */
public class BillCursorAdapterTest {

    private BillCursorAdapter underTest;
    @Mock
    private FragmentActivity parent;
    @Mock
    private Cursor cursor;

    @Before
    public void setUp() {
        underTest = new BillCursorAdapter(parent, R.layout.list_bill_row, cursor, PROJECTION, TO_VIEWS);
    }
}
