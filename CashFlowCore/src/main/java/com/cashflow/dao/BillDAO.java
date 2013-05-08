package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Bill;

/**
 * DAO for {@link Bill}.
 * @author Kornel_Refi
 */
public interface BillDAO {

    /**
     * Persists values to the database.
     * @param bill
     *            {@link Bill} to save.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    boolean save(Bill bill);

    /**
     * Updates a row with specified id.
     * @param bill
     *            updated {@link Bill}
     * @param billId
     *            id for the updateable {@link Bill}
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    boolean update(Bill bill, String billId);

    /**
     * Delete the given {@link Bill} from database.
     * @param bill
     *            {@link Bill} to delete.
     */
    void delete(Bill bill);

    /**
     * Returns all {@link Bill}.
     * @return {@link List} of all {@link Bill}.
     */
    List<Bill> getAllBills();

    /**
     * Return one {@link Bill} by it's id.
     * @param billId
     *            {@link Bill} id
     * @return {@link Bill}
     */
    Bill getBillById(String billId);

}