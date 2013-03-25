package com.cashflow.bill.database;

import com.cashflow.database.DatabaseContracts.AbstractBill;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.DaoParent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Dao class for bills.
 * @author Janos_Gyula_Meszaros
 *
 */
@Singleton
public class BillDao extends DaoParent {

    /**
     * Constructor which gets a provider.    
     * @param provider Provider to get database.
     */
    @Inject
    public BillDao(SQLiteDbProvider provider) {
        super(provider, AbstractBill.class);
    }

}
