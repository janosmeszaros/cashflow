package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Bill;

/**
 * DAO for {@link Bill}.
 * @author Kornel_Refi
 *
 */
public interface BillDAO {

    /**
     * Persists values to the database.
     * @param bill {@link Bill} to save.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(Bill bill);

    /**
     * Updates a row with specified id.
     * @param bill
     * @param billId
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(Bill bill, String billId);

    /**
     * Returns all {@link Bill}.
     * @return {@link List} of all {@link Bill}.
     */
    public List<Bill> getAllBills();

}