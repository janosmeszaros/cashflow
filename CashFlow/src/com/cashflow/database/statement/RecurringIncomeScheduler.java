package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private final Map<String, String> columnValues = new HashMap<String, String>();

    /**
     * Constructor.
     * @param statementPersistenceService service.
     */
    @Inject
    public RecurringIncomeScheduler(StatementPersistenceService statementPersistenceService) {
        nullChecK(statementPersistenceService);
        this.statementPersistenceService = statementPersistenceService;
    }

    /**
     * Add actual statements to database if needed. It also update the recurring statement entry to the 
     * last occurred statement date to not get multiple statement on one date.  
     */
    public void schedule() {
        List<Statement> list = getRecurringStatements();

        interateThrough(list);
    }

    private void interateThrough(List<Statement> list) {
        for (Statement statement : list) {
            int periods = countPeriods(statement.getRecurringInterval());

            String newDate = saveNewStatements(statement, periods);
            updateRecurringStatement(statement, newDate);

        }
    }

    private List<Statement> getRecurringStatements() {
        return statementPersistenceService.getRecurringIncomes();
    }

    private void updateRecurringStatement(Statement recurringStatement, String newDate) {
        if (!recurringStatement.getDate().equals(newDate)) {

            Statement statement = new Statement.Builder(columnValues.get(COLUMN_NAME_AMOUNT), newDate).setId(columnValues.get(_ID))
                    .setNote(columnValues.get(COLUMN_NAME_NOTE)).setRecurringInterval(recurringStatement.getRecurringInterval())
                    .setType(StatementType.Income).build();

            LOG.debug("Update recurring statement's date to " + newDate);
            statementPersistenceService.updateStatement(statement);
        }
    }

    private int countPeriods(RecurringInterval interval) {
        int periods = interval.numOfPassedPeriods(formatter.parseDateTime(columnValues.get(COLUMN_NAME_DATE)));

        LOG.debug("Number of periods: " + periods);

        return periods;
    }

    private String saveNewStatements(Statement recurringStatement, int periods) {
        String result = recurringStatement.getDate();

        if (periods > 0) {
            for (int i = 0; i <= periods; i++) {
                Statement statement = createStatement(recurringStatement, i);
                statementPersistenceService.saveStatement(statement);
                result = statement.getDate();
            }
        }

        return result;
    }

    private Statement createStatement(Statement recurringStatement, int periods) {
        DateTime dateTime = formatter.parseDateTime(recurringStatement.getDate());

        for (int i = 0; i < periods; i++) {
            dateTime = dateTime.plus(recurringStatement.getRecurringInterval().getPeriod());
        }

        return buildStatement(recurringStatement, dateTime.toString(formatter));
    }

    private Statement buildStatement(Statement recurringStatement, String dateTime) {
        return new Statement.Builder(recurringStatement.getAmount(), dateTime).setNote(recurringStatement.getNote())
                .setCategory(recurringStatement.getCategory()).setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build();

    }

    private void nullChecK(StatementPersistenceService statementPersistenceService) {
        Validate.notNull(statementPersistenceService, "Constructor argument can't be null.");
    }
}
