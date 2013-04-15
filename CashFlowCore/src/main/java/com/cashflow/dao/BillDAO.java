package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Bill;

public interface BillDAO {

    /**
     * Persists values to the database.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(Bill bill);

    /**
     * Updates a row with specified id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(Bill bill, String id);

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    public List<Bill> getAllBills();

}