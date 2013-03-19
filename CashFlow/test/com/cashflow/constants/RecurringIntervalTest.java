package com.cashflow.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.joda.time.DateTime;
import org.junit.Test;

public class RecurringIntervalTest {

    private RecurringInterval underTest;

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsNoneThenShouldReturnZero() {
        DateTime date = DateTime.now().minusDays(2);
        underTest = RecurringInterval.none;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalAndDateIsTodayIsNoneThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.none;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsNoneAndDateIsAfterTwoDaysThenShouldReturnZero() {
        DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.none;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTwoDayBeforeThenShouldReturnTwo() {
        DateTime date = DateTime.now().minusDays(3);
        underTest = RecurringInterval.daily;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTwoDayAfterThenShouldReturnZero() {
        DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.daily;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsDailyAndDateIsTodayThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.daily;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTwoWeekBeforeThenShouldReturnTwo() {
        DateTime date = DateTime.now().minusWeeks(2).minusDays(1);
        underTest = RecurringInterval.weekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTwoDayAfterThenShouldReturnZero() {
        DateTime date = DateTime.now().plusDays(2);
        underTest = RecurringInterval.weekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsWeeklyAndDateIsTodayThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.weekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsOneMonthBeforeThenShouldReturnTwo() {
        DateTime date = DateTime.now().minusMonths(1).minusDays(1);
        underTest = RecurringInterval.biweekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsOneMonthAfterThenShouldReturnZero() {
        DateTime date = DateTime.now().plusMonths(1);
        underTest = RecurringInterval.biweekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsBiWeeklyAndDateIsTodayThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.biweekly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTwoMonthBeforeThenShouldReturnTwo() {
        DateTime date = DateTime.now().minusMonths(2).minusDays(1);
        underTest = RecurringInterval.monthly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTwoMonthAfterThenShouldReturnZero() {
        DateTime date = DateTime.now().plusMonths(2);
        underTest = RecurringInterval.monthly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsMonthlyAndDateIsTodayThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.monthly;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTwoYearBeforeThenShouldReturnTwo() {
        DateTime date = DateTime.now().minusYears(2).minusDays(1);
        underTest = RecurringInterval.annually;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(2));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTwoYearAfterThenShouldReturnZero() {
        DateTime date = DateTime.now().plusYears(2);
        underTest = RecurringInterval.annually;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

    @Test
    public void testNumOfPassedPeriodsWhenIntervalIsAnnualyAndDateIsTodayThenShouldReturnZero() {
        DateTime date = DateTime.now();
        underTest = RecurringInterval.annually;

        int periods = underTest.numOfPassedPeriods(date);

        assertThat(periods, equalTo(0));
    }

}
