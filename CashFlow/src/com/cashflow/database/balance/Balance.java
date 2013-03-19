package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;

/**
 * Class to keep the actual balance. Thread safe access to the balance field.
 * @author Janos_Gyula_Meszaros
 */
public final class Balance {
    private static final Logger LOG = LoggerFactory.getLogger(Balance.class);
    private volatile BigDecimal amountBalance = BigDecimal.ZERO;
    private final StatementPersistenceService service;

    private Balance(StatementPersistenceService service) {
        this.service = service;
    }

    public double getBalance() {
        return amountBalance.doubleValue();
    }

    /**
     * Static factory method which gets the {@link StatementPersistenceService} to help count the balance.
     * @param service
     *            {@link StatementPersistenceService}
     * @return the Balance instance.
     */
    public static Balance getInstance(StatementPersistenceService service) {
        Balance balance = new Balance(service);
        balance.countBalance();

        return balance;
    }

    /**
     * Calculates the current balance.
     */
    public void countBalance() {
        double expenses = countSumOfStatement(service.getStatement(StatementType.Expense));
        double incomes = countSumOfStatement(service.getStatement(StatementType.Income));

        amountBalance = BigDecimal.valueOf(incomes - expenses);
        LOG.debug("Balance is: " + amountBalance.doubleValue());
    }

    private double countSumOfStatement(Cursor cursor) {
        int index = cursor.getColumnIndex(COLUMN_NAME_AMOUNT);
        double amount = 0L;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            amount += cursor.getLong(index);
            cursor.moveToNext();
        }

        return amount;
    }

}
