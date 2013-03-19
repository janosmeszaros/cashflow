package com.cashflow.database.statement;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_INTERVAL;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_NOTE;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

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
    private final Map<String, Integer> columnNumbers = new HashMap<String, Integer>();

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
        Cursor cursor = getCursor();

        getColumnNumbers(cursor);

        interateThrough(cursor);
    }

    private void interateThrough(Cursor cursor) {
        while (!cursor.isAfterLast()) {
            Map<String, String> columnValues = getColumnValues(cursor);
            RecurringInterval interval = RecurringInterval.valueOf(columnValues.get(COLUMN_NAME_INTERVAL));

            int periods = countPeriods(columnValues, interval);

            String newDate = saveNewStatements(columnValues, interval, periods);
            updateRecurringStatement(columnValues, newDate, interval);

            cursor.moveToNext();
        }
    }

    private Cursor getCursor() {
        Cursor cursor = statementPersistenceService.getStatement(StatementType.RecurringIncome);
        cursor.moveToFirst();
        return cursor;
    }

    private void nullChecK(StatementPersistenceService statementPersistenceService) {
        Validate.notNull(statementPersistenceService, "Constructor argument can't be null.");
    }

    private void updateRecurringStatement(Map<String, String> columnValues, String newDate, RecurringInterval interval) {
        if (!columnValues.get(COLUMN_NAME_DATE).equals(newDate)) {

            Statement statement = new Statement.Builder(columnValues.get(COLUMN_NAME_AMOUNT), newDate).setId(columnValues.get(_ID))
                    .setNote(columnValues.get(COLUMN_NAME_NOTE)).setRecurringInterval(interval).setType(StatementType.RecurringIncome).build();

            LOG.debug("Update recurring statement's date to " + newDate);
            statementPersistenceService.updateStatement(statement);
        }
    }

    private int countPeriods(Map<String, String> columnValues, RecurringInterval interval) {
        int periods = interval.numOfPassedPeriods(formatter.parseDateTime(columnValues.get(COLUMN_NAME_DATE)));
        LOG.debug("Number of periods: " + periods + " for " + columnValues.get(COLUMN_NAME_NOTE) + " on " + columnValues.get(COLUMN_NAME_DATE)
                + " with " + interval.toString() + " interval");

        return periods;
    }

    private String saveNewStatements(Map<String, String> columnValues, RecurringInterval interval, int periods) {
        String result = columnValues.get(COLUMN_NAME_DATE);

        if (periods > 0) {
            for (int i = 0; i <= periods; i++) {
                Statement statement = createStatement(columnValues, i, interval);
                statementPersistenceService.saveStatement(statement);
                result = statement.getDate();
            }
        }

        return result;
    }

    private Statement createStatement(Map<String, String> values, int periods, RecurringInterval interval) {
        DateTime dateTime = formatter.parseDateTime(values.get(COLUMN_NAME_DATE));

        for (int i = 0; i < periods; i++) {
            dateTime = dateTime.plus(interval.getPeriod());
        }

        return buildStatement(values, dateTime.toString(formatter));
    }

    private Statement buildStatement(Map<String, String> values, String dateTime) {
        return new Statement.Builder(values.get(COLUMN_NAME_AMOUNT), dateTime).setNote(values.get(COLUMN_NAME_NOTE))
                .setRecurringInterval(RecurringInterval.none).setType(StatementType.Income).build();

    }

    private Map<String, String> getColumnValues(Cursor cursor) {
        Map<String, String> values = new HashMap<String, String>();
        values.put(COLUMN_NAME_INTERVAL, cursor.getString(columnNumbers.get(COLUMN_NAME_INTERVAL)));
        values.put(COLUMN_NAME_AMOUNT, cursor.getString(columnNumbers.get(COLUMN_NAME_AMOUNT)));
        values.put(COLUMN_NAME_DATE, cursor.getString(columnNumbers.get(COLUMN_NAME_DATE)));
        values.put(COLUMN_NAME_NOTE, cursor.getString(columnNumbers.get(COLUMN_NAME_NOTE)));
        values.put(_ID, cursor.getString(columnNumbers.get(_ID)));

        return values;
    }

    private void getColumnNumbers(Cursor cursor) {
        columnNumbers.put(COLUMN_NAME_DATE, cursor.getColumnIndex(COLUMN_NAME_DATE));
        columnNumbers.put(COLUMN_NAME_AMOUNT, cursor.getColumnIndex(COLUMN_NAME_AMOUNT));
        columnNumbers.put(COLUMN_NAME_INTERVAL, cursor.getColumnIndex(COLUMN_NAME_INTERVAL));
        columnNumbers.put(_ID, cursor.getColumnIndex(_ID));
        columnNumbers.put(COLUMN_NAME_NOTE, cursor.getColumnIndex(COLUMN_NAME_NOTE));
    }
}
