package com.cashflow.constants;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Enum for the recurring income intervals.
 * @author Janos_Gyula_Meszaros
 *
 */
public enum RecurringInterval {
    none(Period.ZERO) {
        @Override
        public int numOfPassedPeriods(DateTime date) {
            return 0;
        }
    },
    daily(Period.days(1)), weekly(Period.weeks(1)), biweekly(Period.weeks(2)), monthly(Period.months(1)), annually(Period.years(1));

    private final Period period;

    private RecurringInterval(Period period) {
        this.period = period;
    }

    private int countPassedPeriods(DateTime date) {
        DateTime time = date.plus(getPeriod());
        int result = 0;

        while (time.isBeforeNow()) {
            result++;
            time = time.plus(getPeriod());
        }
        return result;
    }

    /**
     * Returns the number of passed periods between the given date and now.
     * @param date checkable date.
     * @return number of periods.
     */
    public int numOfPassedPeriods(DateTime date) {
        return countPassedPeriods(date);
    }

    public Period getPeriod() {
        return period;
    }

}