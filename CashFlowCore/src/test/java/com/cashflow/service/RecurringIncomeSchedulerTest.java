package com.cashflow.service;

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
import com.cashflow.dao.StatementDAO;
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
    private static final Category CATEGORY = Category.builder("cat").categoryId("2").build();

    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());

    @Mock
    private StatementDAO dao;
    private List<Statement> statements;

    private RecurringIncomeScheduler underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        statements = new ArrayList<Statement>();
        when(dao.getRecurringIncomes()).thenReturn(statements);

        underTest = new RecurringIncomeScheduler(dao);
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

        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plus(interval.getPeriod()))).note(NOTE)
                        .recurringInterval(RecurringInterval.none).category(CATEGORY).type(StatementType.Income).build());
        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plusWeeks(4))).note(NOTE).recurringInterval(RecurringInterval.none)
                        .category(CATEGORY).type(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsMonthlyThenShouldSaveOneStatement() {
        final RecurringInterval interval = RecurringInterval.monthly;
        final Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).note(NOTE)
                        .recurringInterval(RecurringInterval.none).category(CATEGORY).type(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsAnnuallyThenShouldSaveNoneStatement() {
        final RecurringInterval interval = RecurringInterval.annually;
        final Statement statement = createStatement(ONE_MONTH_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(dao, times(0)).save(
                Statement.builder(AMOUNT, formatter.print(ONE_MONTH_BEFORE.plus(interval.getPeriod()))).note(NOTE)
                        .recurringInterval(RecurringInterval.none).category(CATEGORY).type(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenOneMonthAndTheIntervalIsBeWeeklyThenShouldUpdateCurrentStatement() {
        final RecurringInterval interval = RecurringInterval.biweekly;
        final Statement statement = createStatement(FOUR_WEEKS_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(dao).update(
                Statement.builder(AMOUNT, formatter.print(FOUR_WEEKS_BEFORE.plusWeeks(4))).id(INCOME_ID).note(NOTE)
                        .recurringInterval(RecurringInterval.biweekly).category(CATEGORY).type(StatementType.Income).build(), INCOME_ID);

    }

    @Test
    public void testScheduleWhenOneYearAndTheIntervalIsannuallyThenShouldSaveOneStatement() {
        final RecurringInterval interval = RecurringInterval.annually;
        final Statement statement = createStatement(ONE_YEAR_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(DateTime.now())).category(CATEGORY).note(NOTE).recurringInterval(RecurringInterval.none)
                        .type(StatementType.Income).build());
    }

    @Test
    public void testScheduleWhenThreeMonthAndTheIntervalIsMonthlyThenShouldSaveThreeStatement() {
        final RecurringInterval interval = RecurringInterval.monthly;
        final Statement statement = createStatement(THREE_MONTHS_BEFORE, interval);
        statements.add(statement);

        underTest.schedule();

        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(1))).note(NOTE).recurringInterval(RecurringInterval.none)
                        .category(CATEGORY).type(StatementType.Income).build());
        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(2))).note(NOTE).recurringInterval(RecurringInterval.none)
                        .category(CATEGORY).type(StatementType.Income).build());
        verify(dao).save(
                Statement.builder(AMOUNT, formatter.print(THREE_MONTHS_BEFORE.plusMonths(3))).note(NOTE).recurringInterval(RecurringInterval.none)
                        .category(CATEGORY).type(StatementType.Income).build());
    }

    private Statement createStatement(final DateTime time, final RecurringInterval interval) {
        return Statement.builder(AMOUNT, formatter.print(time)).recurringInterval(interval).type(StatementType.Income).note(NOTE).category(CATEGORY)
                .id(INCOME_ID).build();
    }
}