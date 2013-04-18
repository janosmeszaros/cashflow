package com.cashflow.bill.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractBill.BILL_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_ADDED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractBill.STATEMENT_INNER_JOINED_CATEGORY;
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
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
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
     * @param provider
     *            {@link SQLiteDbProvider} to get database.
     */
    @Inject
    public AndroidBillDAO(final SQLiteDbProvider provider) {
        Validate.notNull(provider);
        this.provider = provider;
    }

    @Override
    public boolean save(final Bill bill) {
        Validate.notNull(bill);
        final ContentValues valuesToSave = createContentValues(bill);
        return persistBill(valuesToSave);
    }

    private boolean persistBill(final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final long newRowId = database.insert(TABLE_NAME, null, values);
        LOG.debug("New row created with row ID: " + newRowId);
        return isSuccesfulSave(newRowId);
    }

    private boolean isSuccesfulSave(final long newRowId) {
        return newRowId >= 0;
    }

    @Override
    public boolean update(final Bill bill, final String billId) {
        Validate.notNull(bill);
        Validate.notEmpty(billId);

        final ContentValues values = createContentValues(bill);
        return persistUpdate(billId, values);
    }

    private boolean persistUpdate(final String billId, final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final int updatedRows = database.update(TABLE_NAME, values, _ID + EQUALS, new String[] { billId });
        LOG.debug("Num of rows updated: " + updatedRows);
        return isSuccessfulUpdate(updatedRows);
    }

    private boolean isSuccessfulUpdate(final int updatedRows) {
        return updatedRows > 0;
    }

    @Override
    public List<Bill> getAllBills() {
        final SQLiteDatabase database = provider.getReadableDb();
        // TODO Bill PROJECTION has too few column.
        final Cursor query = database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, null, null, null, null, null);
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
        final int idIndex = cursor.getColumnIndexOrThrow(BILL_ID_ALIAS);
        final int amountIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT);
        final int addedDateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_ADDED);
        final int deadLineDateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_DEADLINE);
        final int isPayedIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_IS_PAYED);
        final int payedDateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_PAYED);
        final int noteIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_NOTE);
        final int categoryNameIndex = cursor.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME);
        final int categoryIdIndex = cursor.getColumnIndexOrThrow(AbstractCategory.CATEGORY_ID_ALIAS);

        while (cursor.moveToNext()) {
            final String billId = cursor.getString(idIndex);
            final String amount = cursor.getString(amountIndex);
            final String date = cursor.getString(addedDateIndex);
            final String deadline = cursor.getString(deadLineDateIndex);
            final String isPayedString = cursor.getString(isPayedIndex);
            final String note = cursor.getString(noteIndex);
            final String payedDate = cursor.getString(payedDateIndex);
            final String categoryId = cursor.getString(categoryIdIndex);
            final String categoryName = cursor.getString(categoryNameIndex);

            final boolean isPayed = TRUE.equals(isPayedString);
            final Category cat = Category.builder(categoryName).categoryId(categoryId).build();

            final Bill bill =
                    Bill.builder(amount, date, deadline).isPayed(isPayed).payedDate(payedDate).category(cat).note(note).billId(billId)
                            .build();
            result.add(bill);
        }
        return result;
    }
}
