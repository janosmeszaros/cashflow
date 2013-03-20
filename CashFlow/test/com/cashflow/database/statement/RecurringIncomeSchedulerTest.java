package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.Cursor;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Statement;

public class RecurringIncomeSchedulerTest {

    private static final String ID = "1";
    private static final String NOTE = "fizu";
    private static final String AMOUNT = "1234";
    private static final Integer DATE_NUMBER = 0;
    private static final Integer AMOUNT_NUMBER = 1;
    private static final Integer ID_NUMBER = 2;
    private static final Integer INTERVAL_NUMBER = 3;
    private static final Integer NOTE_NUMBER = 4;
    private static final DateTime ONE_MONTH_BEFORE = DateTime.now().minusMonths(1);
    private static final DateTime THREE_MONTH_BEFORE = DateTime.now().minusMonths(3);
    private static final DateTime ONE_YEAR_BEFORE = DateTime.now().minusYears(1);

    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());

    @Mock
    private StatementPersistenceService service;
    @Mock
    private Cursor cursor;
    private RecurringIncomeScheduler underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpMocks();

        underTest = new RecurringIncomeScheduler(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArgIsNullThenThrowException() {
        underTest = new RecurringIncomeScheduler(null);
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldSaveTwoStatement() {
        RecurringInterval interval = RecurringInterval.biweekly;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(ONE_MONTH_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plusMonths(1))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsMonthlyThenShouldSaveOneStatement() {
        RecurringInterval interval = RecurringInterval.monthly;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(ONE_MONTH_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsAnnuallyThenShouldSaveNoneStatement() {
        RecurringInterval interval = RecurringInterval.annually;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(ONE_MONTH_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service, times(0)).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldUpdateCurrentStatement() {
        RecurringInterval interval = RecurringInterval.biweekly;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(ONE_MONTH_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service).updateStatement(
                new Statement.Builder(AMOUNT, formatter.print(DateTime.now())).setId(ID).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.biweekly).setType(StatementType.Income).build());

    }

    @Test
    public void testScheduleWhenOneYearAndTheIntervalIsannuallyThenShouldSaveOneStatement() {
        RecurringInterval interval = RecurringInterval.annually;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(ONE_YEAR_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(DateTime.now())).setNote(NOTE).setRecurringInterval(RecurringInterval.none)
                        .setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenThreeMonthAndTheIntervalIsMonthlyThenShouldSaveThreeStatement() {
        RecurringInterval interval = RecurringInterval.monthly;

        when(cursor.getString(DATE_NUMBER)).thenReturn(formatter.print(THREE_MONTH_BEFORE));
        when(cursor.getString(AMOUNT_NUMBER)).thenReturn(AMOUNT);
        when(cursor.getString(INTERVAL_NUMBER)).thenReturn(interval.toString());
        when(cursor.getString(ID_NUMBER)).thenReturn(ID);
        when(cursor.getString(NOTE_NUMBER)).thenReturn(NOTE);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(1))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(2))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(3))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    private void setUpMocks() {
        when(service.getRecurringIncomes()).thenReturn(cursor);

        when(cursor.getColumnIndex(COLUMN_NAME_DATE)).thenReturn(DATE_NUMBER);
        when(cursor.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(AMOUNT_NUMBER);
        when(cursor.getColumnIndex(COLUMN_NAME_INTERVAL)).thenReturn(INTERVAL_NUMBER);
        when(cursor.getColumnIndex(_ID)).thenReturn(ID_NUMBER);
        when(cursor.getColumnIndex(COLUMN_NAME_NOTE)).thenReturn(NOTE_NUMBER);
        when(cursor.isAfterLast()).thenReturn(false, true);

    }
}
