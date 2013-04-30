package com.cashflow.bill.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractBill.BILL_ID_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_ADDED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_NOTE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractBill.SELECT_BILL_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractBill.STATEMENT_INNER_JOINED_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractBill.TABLE_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.BillDAO;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.cashflow.exceptions.IllegalBillIdException;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AndroidBillDAO} test.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AndroidBillDAOTest {
    private static final String NOTE = "note";
    private static final String UNPAYED_ID = "2";
    private static final String PAYED_ID = "1";
    private static final String DATE = "03.04.2013";
    private static final String AMOUNT = "1234";
    private static final String CATEGORY_ID = "0";
    private static final String CATEGORY_NAME = "category";
    private static final String EQUALS = " = ?";
    private static final Category CATEGORY = Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build();
    private static final Bill PAYED_BILL = Bill.builder(AMOUNT, DATE, DATE).billId(PAYED_ID).category(CATEGORY).isPayed(true).note(NOTE)
            .payedDate(DATE).build();
    private static final Bill UNPAYED_BILL = Bill.builder(AMOUNT, DATE, DATE).billId(UNPAYED_ID).note(NOTE).category(CATEGORY).isPayed(false).build();

    private BillDAO underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase database;
    @Mock
    private Cursor cursorMock;
    @Mock
    private ContentValues values;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(database);
        when(provider.getReadableDb()).thenReturn(database);

        underTest = new AndroidBillDAO(provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        new AndroidBillDAO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenPayedBillThenShouldSaveToDatabase() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(1L);

        final boolean saved = underTest.save(PAYED_BILL);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(true));
    }

    @Test
    public void testSaveWhenUnPayedBillThenShouldSaveToDatabase() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(1L);

        final boolean saved = underTest.save(UNPAYED_BILL);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(true));
    }

    @Test
    public void testSaveWhenBillIsOkButSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(-1L);

        final boolean saved = underTest.save(PAYED_BILL);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIsNullThenShouldThrowException() {
        underTest.update(null, PAYED_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenIdIsEmptyThenShouldThrowException() {
        underTest.update(PAYED_BILL, "");
    }

    @Test
    public void testUpdateWhenBillIsOkThenShouldUpdateAndReturnTrue() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{PAYED_ID}))).thenReturn(1);

        final boolean updated = underTest.update(PAYED_BILL, PAYED_ID);

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(true));
    }

    @Test
    public void testUpdateWhenSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{PAYED_ID}))).thenReturn(-1);

        final boolean updated = underTest.update(PAYED_BILL, PAYED_ID);

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(false));
    }

    @Test
    public void testGetAllBillsWhenCalledThenReturnListOfBills() {
        when(database.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, null, null, null, null, null)).thenReturn(cursorMock);
        setupCursorMock();
        final List<Bill> list = new ArrayList<Bill>();
        list.add(PAYED_BILL);

        final List<Bill> bills = underTest.getAllBills();

        verify(provider).getReadableDb();
        verify(database).query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, null, null, null, null, null);
        assertThat(list, equalTo(bills));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBillByIdWhenIdIsEmptyThenShouldThrowException() {
        underTest.getBillById("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBillByIdWhenIdIsNullThenShouldThrowException() {
        underTest.getBillById(null);
    }

    @Test
    public void testGetBillByIdWhenIdIsOkThenShouldReturnOneBill() {
        setupCursorMock();
        when(cursorMock.getCount()).thenReturn(1);
        when(database.rawQuery(SELECT_BILL_BY_ID, new String[]{PAYED_ID})).thenReturn(cursorMock);

        final Bill bill = underTest.getBillById(PAYED_ID);

        assertThat(bill, equalTo(PAYED_BILL));
    }

    @Test(expected = IllegalBillIdException.class)
    public void testGetBillByIdWhenIdIsNotFoundThenShouldThrowException() {
        when(database.rawQuery(SELECT_BILL_BY_ID, new String[]{PAYED_ID})).thenReturn(cursorMock);
        when(cursorMock.getCount()).thenReturn(0);

        underTest.getBillById(PAYED_ID);
    }

    private void setupCursorMock() {
        when(cursorMock.moveToNext()).thenReturn(true, false);

        when(cursorMock.getColumnIndexOrThrow(BILL_ID_ALIAS)).thenReturn(0);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_AMOUNT)).thenReturn(1);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_DATE_ADDED)).thenReturn(2);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_DATE_DEADLINE)).thenReturn(3);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_IS_PAYED)).thenReturn(4);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_NOTE)).thenReturn(5);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_DATE_PAYED)).thenReturn(6);
        when(cursorMock.getColumnIndexOrThrow(AbstractCategory.COLUMN_NAME_CATEGORY_NAME)).thenReturn(7);
        when(cursorMock.getColumnIndexOrThrow(AbstractCategory.CATEGORY_ID_ALIAS)).thenReturn(8);

        when(cursorMock.getString(0)).thenReturn(PAYED_ID);
        when(cursorMock.getString(1)).thenReturn(AMOUNT);
        when(cursorMock.getString(2)).thenReturn(DATE);
        when(cursorMock.getString(3)).thenReturn(DATE);
        when(cursorMock.getString(4)).thenReturn("1");
        when(cursorMock.getString(5)).thenReturn(NOTE);
        when(cursorMock.getString(6)).thenReturn(DATE);
        when(cursorMock.getString(7)).thenReturn(CATEGORY_NAME);
        when(cursorMock.getString(8)).thenReturn(CATEGORY_ID);
    }

}
