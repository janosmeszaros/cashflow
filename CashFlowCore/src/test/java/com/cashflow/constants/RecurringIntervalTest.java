package com.cashflow.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
import org.junit.Test;

public class RecurringIntervalTest {

    private RecurringInterval underTest;

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsNoneThenShouldReturnZero() {
        final DateTime date = DateTime.now().minusDays(2);
        underTest = RecurringInterval.none;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalAndDateIsTodayIsNoneThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.none;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsNoneAndDateIsAfterTwoDaysThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.none;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTwoDayBeforeThenShouldReturnTwo() {
        final DateTime date = DateTime.now().minusDays(2).minusHours(1);
        underTest = RecurringInterval.daily;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTwoDayAfterThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.daily;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTodayThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.daily;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTwoWeekBeforeThenShouldReturnTwo() {
        final DateTime date = DateTime.now().minusWeeks(2).minusDays(1);
        underTest = RecurringInterval.weekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTwoDayAfterThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.weekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTodayThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.weekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsOneMonthBeforeThenShouldReturnTwo() {
        final DateTime date = DateTime.now().minusMonths(1).minusDays(1);
        underTest = RecurringInterval.biweekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsOneMonthAfterThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusMonths(1);
        underTest = RecurringInterval.biweekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsTodayThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.biweekly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTwoMonthBeforeThenShouldReturnTwo() {
        final DateTime date = DateTime.now().minusMonths(2).minusDays(1);
        underTest = RecurringInterval.monthly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTwoMonthAfterThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusMonths(2);
        underTest = RecurringInterval.monthly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTodayThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.monthly;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTwoYearBeforeThenShouldReturnTwo() {
        final DateTime date = DateTime.now().minusYears(2).minusDays(1);
        underTest = RecurringInterval.annually;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTwoYearAfterThenShouldReturnZero() {
        final DateTime date = DateTime.now().plusYears(2);
        underTest = RecurringInterval.annually;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTodayThenShouldReturnZero() {
        final DateTime date = DateTime.now();
        underTest = RecurringInterval.annually;

        final int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

}
