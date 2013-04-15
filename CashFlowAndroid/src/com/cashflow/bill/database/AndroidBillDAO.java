package com.cashflow.bill.database;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.BillDAO;
import com.cashflow.database.DatabaseContracts.AbstractBill;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.AndroidParentDAO;
import com.cashflow.domain.Bill;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DAO class for bills.
 * @author Janos_Gyula_Meszaros
 *
 */
@Singleton
public class AndroidBillDAO extends AndroidParentDAO implements BillDAO {

    /**
     * Constructor which gets a provider.    
     * @param provider Provider to get database.
     */
    @Inject
    public AndroidBillDAO(final SQLiteDbProvider provider) {
        super(provider, new AbstractBill());
    }

    @Override
    public List<Bill> getAllBills() {
        final SQLiteDatabase database = provider.getReadableDb();
        return database.query(tableName, projection, null, null, null, null, null);
    }

    @Override
    public boolean update(final Bill bill, final String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean save(final Bill bill) {
        // TODO Auto-generated method stub
        return false;
    }

}
