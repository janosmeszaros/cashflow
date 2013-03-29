package com.cashflow.statement.database;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;

/**
 * Schedule recurring incomes.
 * @author Janos_Gyula_Meszaros
 *
 */
public class RecurringIncomeScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(RecurringIncomeScheduler.class);
    private final StatementPersistenceService statementPersistenceService;
    private final DateTimeFormatter formatter = DateTimeFormat.mediumDate().withLocale(Locale.getDefault());

    /**
     * Constructor.
     * @param statementPersistenceService service.
     */
    @Inject
    public RecurringIncomeScheduler(final StatementPersistenceService statementPersistenceService) {
        nullChecK(statementPersistenceService);
        this.statementPersistenceService = statementPersistenceService;
    }

    /**
     * Add actual statements to database if needed. It also update the recurring statement entry to the 
     * last occurred statement date to not get multiple statement on one date.  
     */
    public void schedule() {
        final List<Statement> list = getRecurringStatements();

        interateThrough(list);
    }

    private void interateThrough(final List<Statement> list) {
        for (Statement statement : list) {
            final int periods = countPeriods(statement);

            final String newDate = saveNewStatements(statement, periods);
            updateRecurringStatement(statement, newDate);

        }
    }

    private List<Statement> getRecurringStatements() {
        return statementPersistenceService.getRecurringIncomes();
    }

    private void updateRecurringStatement(final Statement recurringStatement, final String newDate) {
        if (!recurringStatement.getDate().equals(newDate)) {

            final Statement statement = new Statement.Builder(recurringStatement.getAmount(), newDate).setId(recurringStatement.getId())
                    .setNote(recurringStatement.getNote()).setCategory(recurringStatement.getCategory())
                    .setRecurringInterval(recurringStatement.getRecurringInterval()).setType(StatementType.Income).build();

            LOG.debug("Update recurring statement's date to " + newDate);
            statementPersistenceService.updateStatement(statement);
        }
    }

    private int countPeriods(final Statement statement) {
        final int periods = statement.getRecurringInterval().numOfPassedPeriods(formatter.parseDateTime(statement.getDate()));

        LOG.debug("Number of periods: " + periods);

        return periods;
    }

    private String saveNewStatements(final Statement recurringStatement, final int periods) {
        String result = recurringStatement.getDate();

        for (int i = 1; i <= periods; i++) {
            final Statement statement = createStatement(recurringStatement, i);
            statementPersistenceService.saveStatement(statement);
            result = statement.getDate();
        }

        return result;
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

    private Statement buildStatement(final Statement recurringStatement, final String dateTime) {
        return new Statement.Builder(recurringStatement.getAmount(), dateTime).setNote(recurringStatement.getNote())
                .setCategory(recurringStatement.getCategory()).setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build();

    }

    private void nullChecK(final StatementPersistenceService statementPersistenceService) {
        Validate.notNull(statementPersistenceService, "Constructor argument can't be null.");
    }
}
