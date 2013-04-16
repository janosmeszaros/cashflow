package com.cashflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

public class RecurringIncomeSchedulerTest {

    private static final String INCOME_ID = "1";
    private static final String NOTE = "fizu";
    private static final String AMOUNT = "1234";
    private static final DateTime FOUR_WEEKS_BEFORE = DateTime.now().minusWeeks(4);
    private static final DateTime ONE_MONTH_BEFORE = DateTime.now().minusMonths(1);
    private static final DateTime THREE_MONTHS_BEFORE = DateTime.now().minusMonths(3);
    private static final DateTime ONE_YEAR_BEFORE = DateTime.now().minusYears(1);
    private static final Category CATEGORY = new Category("2", "cat");

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
        final RecurringInterval interval = RecurringInterval.biweekly;
        final Statement statement = createStatement(FOUR_WEEKS_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plusWeeks(4))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none)
                        .setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsMonthlyThenShouldSaveOneStatement() {
        final RecurringInterval interval = RecurringInterval.monthly;
        final Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsAnnuallyThenShouldSaveNoneStatement() {
        final RecurringInterval interval = RecurringInterval.annually;
        final Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service, times(0)).saveStatement(
                Statement.builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldUpdateCurrentStatement() {
        final RecurringInterval interval = RecurringInterval.biweekly;
        final Statement statement = createStatement(FOUR_WEEKS_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).updateStatement(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plusWeeks(4))).setId(INCOME_ID).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.biweekly).setCategory(CATEGORY).setType(StatementType.Income).build());

    }

    @Test
    public void testScheduleWhenOneYearAndTheIntervalIsannuallyThenShouldSaveOneStatement() {
        final RecurringInterval interval = RecurringInterval.annually;
        final Statement statement = createStatement(ONE_YEAR_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(DateTime.now())).setCategory(CATEGORY).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenThreeMonthAndTheIntervalIsMonthlyThenShouldSaveThreeStatement() {
        final RecurringInterval interval = RecurringInterval.monthly;
        final Statement statement = createStatement(THREE_MONTHS_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(1))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(2))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
        verify(service).saveStatement(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(3))).setNote(NOTE)
                        .setRecurringInterval(RecurringInterval.none).setCategory(CATEGORY).setType(StatementType.Income).build());
    }

    private Statement createStatement(final DateTime time, final RecurringInterval interval) {
        return Statement.builder(AMOUNT, formatter.print(time)).setRecurringInterval(interval).setType(StatementType.Income).setNote(NOTE)
                .setCategory(CATEGORY).setId(INCOME_ID).build();
    }
}