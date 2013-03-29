package com.cashflow.statement.database;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;

public class RecurringIncomeSchedulerTest {

    private static final String ID = "1";
    private static final String NOTE = "fizu";
    private static final String AMOUNT = "1234";
    private static final DateTime ONE_MONTH_BEFORE = DateTime.now().minusMonths(1);
    private static final DateTime THREE_MONTH_BEFORE = DateTime.now().minusMonths(3);
    private static final DateTime ONE_YEAR_BEFORE = DateTime.now().minusYears(1);
    private static final Category CATEGORY = new Category("1", "cat");

    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());

    @Mock
    private StatementPersistenceService service;
    private List<Statement> statements;

    private RecurringIncomeScheduler underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        statements = new ArrayList<Statement>();
        when(service.getRecurringIncomes()).thenReturn(statements);

        underTest = new RecurringIncomeScheduler(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArgIsNullThenThrowException() {
        underTest = new RecurringIncomeScheduler(null);
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldSaveTwoNewStatement() {
        RecurringInterval interval = RecurringInterval.biweekly;
        Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plusMonths(1))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsMonthlyThenShouldSaveOneStatement() {
        RecurringInterval interval = RecurringInterval.monthly;
        Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsAnnuallyThenShouldSaveNoneStatement() {
        RecurringInterval interval = RecurringInterval.annually;
        Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service, times(0)).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldUpdateCurrentStatement() {
        RecurringInterval interval = RecurringInterval.biweekly;
        Statement statement = createStatement(DateTime.now().minusWeeks(4), interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).updateStatement(
                new Statement.Builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plusWeeks(4))).setId(ID).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.biweekly).setCategory(CATEGORY).setType(StatementType.Income).build());

    }

    @Test
    public void testScheduleWhenOneYearAndTheIntervalIsannuallyThenShouldSaveOneStatement() {
        RecurringInterval interval = RecurringInterval.annually;
        Statement statement = createStatement(ONE_YEAR_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(DateTime.now())).setCategory(CATEGORY).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenThreeMonthAndTheIntervalIsMonthlyThenShouldSaveThreeStatement() {
        RecurringInterval interval = RecurringInterval.monthly;
        Statement statement = createStatement(THREE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(1))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(2))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                new Statement.Builder(AMOUNT, formatter.print(THREE_MONTH_BEFORE.plusMonths(3))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    private Statement createStatement(DateTime time, RecurringInterval interval) {
        Statement statement = new Statement.Builder(AMOUNT, formatter.print(time)).setRecurringInterval(interval).setType(StatementType.Income)
                .setNote(NOTE).setCategory(CATEGORY).setId(ID).build();
        return statement;
    }
}