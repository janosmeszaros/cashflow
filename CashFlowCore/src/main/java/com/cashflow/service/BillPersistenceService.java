package com.cashflow.service;

import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_ADDED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_NOTE;

import org.apache.commons.lang.Validate;

import android.content.ContentValues;

import com.cashflow.domain.Bill;
import com.cashflow.service.AndroidBillDAO;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create {@link ContentValues} to {@link AndroidBillDAO}.
 * @author Janos_Gyula_Meszaros
 *
 */
@Singleton
public class BillPersistenceService {
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private final AndroidBillDAO dao;

    /**
     * Constructor which gets a dao to save {@link Bill}.
     * @param dao {@link AndroidBillDAO} class.
     */
    @Inject
    public BillPersistenceService(final AndroidBillDAO dao) {
        nullCheck(dao);
        this.dao = dao;
    }

    /**
     * Save the given bill to database.
     * @param bill {@link Bill} to save.
     * @throws IllegalArgumentException when {@link Bill} is null, or
     *  {@link Bill}'s <code>Amount</code>, <code>Date</code> or <code>Deadline date</code> is empty or null.
     * @return <code>true</code> if saving was successful. <code>false</code> otherwise.
     */
    public boolean saveBill(final Bill bill) {
        validateBill(bill);

        final ContentValues valuesToSave = createContentValues(bill);
        boolean isSuccessful = dao.save(valuesToSave);

        return isSuccessful;
    }

    private ContentValues createContentValues(final Bill bill) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_AMOUNT, bill.getAmount());
        values.put(COLUMN_NAME_DATE_ADDED, bill.getDate());
        values.put(COLUMN_NAME_DATE_PAYED, bill.getPayedDate());
        values.put(COLUMN_NAME_DATE_DEADLINE, bill.getDeadlineDate());
        values.put(COLUMN_NAME_NOTE, bill.getNote());
        values.put(COLUMN_NAME_CATEGORY, bill.getCategory().getId());
        values.put(COLUMN_NAME_IS_PAYED, bill.isPayed() ? TRUE : FALSE);
        values.put(COLUMN_NAME_INTERVAL, bill.getInterval().toString());

        return values;
    }

    private void validateBill(final Bill bill) {
        nullCheck(bill);
        validateStringsNotEmpty(bill.getAmount(), bill.getDate(), bill.getDeadlineDate());
    }

    private void validateStringsNotEmpty(String... params) {
        for (String string : params) {
            Validate.notEmpty(string);
        }
    }

    private void nullCheck(final Object obj) {
        Validate.notNull(obj);
    }
}
