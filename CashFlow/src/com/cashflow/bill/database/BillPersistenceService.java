package com.cashflow.bill.database;

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
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create {@link ContentValues} to {@link BillDao}.
 * @author Janos_Gyula_Meszaros
 *
 */
@Singleton
public class BillPersistenceService {
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private BillDao dao;

    /**
     * Constructor which gets a dao to save {@link Bill}.
     * @param dao {@link BillDao} class.
     */
    @Inject
    public BillPersistenceService(BillDao dao) {
        this.dao = dao;
    }

    /**
     * Save the given bill to database.
     * @param bill {@link Bill} to save.
     * @throws IllegalArgumentException when {@link Bill} is null.
     * @return <code>true</code> if saving was successful. <code>false</code> otherwise.
     */
    public boolean saveBill(Bill bill) {
        nullCheck(bill);
        boolean isSuccessful = false;

        ContentValues valuesToSave = createContentValues(bill);
        if (dao.save(valuesToSave)) {
            isSuccessful = true;
        }

        return isSuccessful;
    }

    private ContentValues createContentValues(Bill bill) {
        ContentValues values = new ContentValues();

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

    private void nullCheck(Bill bill) {
        Validate.notNull(bill);
    }
}
