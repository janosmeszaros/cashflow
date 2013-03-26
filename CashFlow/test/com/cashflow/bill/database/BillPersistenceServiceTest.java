package com.cashflow.bill.database;

import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_CATEGORY;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_ADDED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_DEADLINE;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_DATE_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_IS_PAYED;
import static com.cashflow.database.DatabaseContracts.AbstractBill.COLUMN_NAME_NOTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link BillPersistenceService} class's test.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class BillPersistenceServiceTest {
    private static final Category CATEGORY = new Category("2", "category");
    private BillPersistenceService underTest;
    @Mock
    private BillDao dao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        underTest = new BillPersistenceService(dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBillIsNullThenShouldThrowException() {
        underTest = new BillPersistenceService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveBillWhenBillIsNullThenShouldThrowException() {
        underTest.saveBill(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveBillWhenBillsAmountIsNullThenShouldThrowException() {
        Bill billToSave = new Bill(null, null, null);

        underTest.saveBill(billToSave);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveBillWhenBillsDateIsEmptyThenShouldThrowException() {
        Bill billToSave = new Bill("1233", "", null);

        underTest.saveBill(billToSave);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveBillWhenBillsDeadlineDateIsNullThenShouldThrowException() {
        Bill billToSave = new Bill("1231", "12.12.12", null);

        underTest.saveBill(billToSave);
    }

    @Test
    public void testSaveBillWhenBillIsPayedThenShouldCallDaosSaveMethodAndReturnTrueIfItWasSuccessful() {
        Bill billToSave = createBillToSave(true);
        when(dao.save((ContentValues) anyObject())).thenReturn(true);

        boolean isSaved = underTest.saveBill(billToSave);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).save(argument.capture());
        verifySaveParameters(billToSave, argument);

        assertThat(isSaved, equalTo(true));
    }

    @Test
    public void testSaveBillWhenBillIsNotPayedButSomeThingHappensOnSaveThenShouldCallDaosSaveMethodAndReturnFalse() {
        Bill billToSave = createBillToSave(false);
        when(dao.save((ContentValues) anyObject())).thenReturn(false);

        boolean isSaved = underTest.saveBill(billToSave);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).save(argument.capture());
        verifySaveParameters(billToSave, argument);

        assertThat(isSaved, equalTo(false));
    }

    private void verifySaveParameters(Bill billToSave, ArgumentCaptor<ContentValues> argument) {
        assertThat((String) argument.getValue().get(COLUMN_NAME_AMOUNT), equalTo(billToSave.getAmount()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE_ADDED), equalTo(billToSave.getDate()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE_PAYED), equalTo(billToSave.getPayedDate()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_DATE_DEADLINE), equalTo(billToSave.getDeadlineDate()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_NOTE), equalTo(billToSave.getNote()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY), equalTo(billToSave.getCategory().getId()));
        assertThat((String) argument.getValue().get(COLUMN_NAME_IS_PAYED), equalTo(billToSave.isPayed() ? "1" : "0"));
        assertThat((String) argument.getValue().get(COLUMN_NAME_INTERVAL), equalTo(billToSave.getInterval().toString()));
    }

    private Bill createBillToSave(boolean isSaved) {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Calendar calendar = Calendar.getInstance();

        Bill billToSave = new Bill("123", dateFormatter.format(calendar.getTime()), dateFormatter.format(calendar.getTime()));
        billToSave.setCategory(CATEGORY);
        billToSave.setInterval(RecurringInterval.annually);
        billToSave.setNote("note");
        billToSave.setPayed(isSaved);
        billToSave.setPayedDate("");

        return billToSave;
    }
}
