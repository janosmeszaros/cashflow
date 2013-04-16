package com.cashflow.service;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

/**
 * Schedule recurring incomes.
 * @author Janos_Gyula_Meszaros
 */
public class RecurringIncomeScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(RecurringIncomeScheduler.class);
    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());
    private final StatementDAO dao;

    /**
     * Constructor.
     * @param dao
     *            {@link StatementDAO}.
     */
    public RecurringIncomeScheduler(final StatementDAO dao) {
        nullChecK(dao);
        this.dao = dao;
    }

    /**
     * Add actual statements to database if needed. It also update the recurring statement entry to the last occurred statement date to not
     * get multiple statement on one date.
     */
    public void schedule() {
        final List<Statement> list = getRecurringStatements();
        interateThrough(list);
    }

    private List<Statement> getRecurringStatements() {
        return dao.getRecurringIncomes();
    }

    private void interateThrough(final List<Statement> list) {
        for (final Statement statement : list) {
            final int periods = countPeriods(statement);

            final String newDate = saveNewStatements(statement, periods);
            updateRecurringStatement(statement, newDate);

        }
    }

    private Statement buildStatement(final Statement recurringStatement, final String dateTime) {
        return Statement.builder(recurringStatement.getAmount(), dateTime).note(recurringStatement.getNote())
                .category(recurringStatement.getCategory()).recurringInterval(RecurringInterval.none).type(StatementType.Income)
                .build();

    }

    private int countPeriods(final Statement statement) {
        final int periods = statement.getRecurringInterval().numOfPassedPeriods(formatter.parseDateTime(statement.getDate()));

        LOG.debug("Number of periods: " + periods);

        return periods;
    }

    private Statement createStatement(final Statement recurringStatement, final int periods) {
        DateTime dateTime = formatter.parseDateTime(recurringStatement.getDate());

        switch (recurringStatement.getRecurringInterval()) {
        case annually:
            dateTime = dateTime.plusYears(periods);
            break;
        case monthly:
            dateTime = dateTime.plusMonths(periods);
            break;
        case biweekly:
            dateTime = dateTime.plusWeeks(periods * 2);
            break;
        case weekly:
            dateTime = dateTime.plusWeeks(periods);
            break;
        case daily:
            dateTime = dateTime.plusDays(periods);
            break;
        default:
        }

        return buildStatement(recurringStatement, dateTime.toString(formatter));
    }

    private void nullChecK(final StatementDAO dao) {
        Validate.notNull(dao, "Constructor argument can't be null.");
    }

    private String saveNewStatements(final Statement recurringStatement, final int periods) {
        String result = recurringStatement.getDate();

        for (int i = 1; i <= periods; i++) {
            final Statement statement = createStatement(recurringStatement, i);
            dao.save(statement);
            result = statement.getDate();
        }

        return result;
    }

    private void updateRecurringStatement(final Statement recurringStatement, final String newDate) {
        if (!recurringStatement.getDate().equals(newDate)) {

            final Statement statement = Statement.builder(recurringStatement.getAmount(), newDate).statementId(recurringStatement.getId())
                    .note(recurringStatement.getNote()).category(recurringStatement.getCategory())
                    .recurringInterval(recurringStatement.getRecurringInterval()).type(StatementType.Income).build();

            LOG.debug("Update recurring statement's date to " + newDate);
            dao.update(statement, statement.getId());
        }
    }
}
