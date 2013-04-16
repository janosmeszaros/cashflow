package com.cashflow.bill.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_ADDED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TABLE_NAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.BillDAO;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Bill;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DAO based on Android for {@link Bill}.
 * @author Janos_Gyula_Meszaros
 * @author Kornel_Refi
 */
@Singleton
public class AndroidBillDAO implements BillDAO {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidBillDAO.class);
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private static final String EQUALS = " = ?";
    private final SQLiteDbProvider provider;

    /**
     * Constructor which gets a provider.    
     * @param provider {@link SQLiteDbProvider} to get database.
     */
    @Inject
    public AndroidBillDAO(final SQLiteDbProvider provider) {
        Validate.notNull(provider);
        this.provider = provider;
    }

    @Override
    public boolean save(final Bill bill) {
        Validate.notNull(bill);

        boolean isSuccessful;

        final long newRowId = provider.getWritableDb().insert(TABLE_NAME, null, createContentValues(bill));

        if (newRowId >= 0) {
            isSuccessful = true;
            LOG.debug("New row created with row ID: " + newRowId);
        } else {
            isSuccessful = false;
        }

        return isSuccessful;
    }

    @Override
    public boolean update(final Bill bill, final String billId) {
        Validate.notNull(bill);
        Validate.notEmpty(billId);

        final ContentValues values = createContentValues(bill);
        values.put(_ID, billId);

        boolean isSuccessful;
        final int update = provider.getWritableDb().update(TABLE_NAME, values, _ID + EQUALS, new String[]{billId});

        if (update > 0) {
            isSuccessful = true;
        } else {
            isSuccessful = false;
        }

        LOG.debug("Num of rows updated: " + update);
        return isSuccessful;
    }

    @Override
    public List<Bill> getAllBills() {

        final SQLiteDatabase database = provider.getReadableDb();
        //TODO Bill PROJECTION has too few column.
        final Cursor query = database.query(TABLE_NAME, PROJECTION, null, null, null, null, null);

        return createListFromCursor(query);
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

    private List<Bill> createListFromCursor(final Cursor cursor) {
        final List<Bill> result = new ArrayList<Bill>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            final String billId = cursor.getString(cursor.getColumnIndexOrThrow(_ID));
            final String amount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT));
            final String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_ADDED));
            final String deadline = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_DEADLINE));
            final String isPayedString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_IS_PAYED));
            boolean isPayed;
            if (TRUE.equals(isPayedString)) {
                isPayed = true;
            } else {
                isPayed = false;
            }
            final String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NOTE));

            final Bill bill = Bill.builder(amount, date, deadline).isPayed(isPayed).note(note).billId(billId).build();
            result.add(bill);
        }
        return result;
    }
}
