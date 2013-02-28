package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;

/**
 * Class to keep the actual balance. Thread safe access to the balance field.
 * @author Janos_Gyula_Meszaros
 */
public final class Balance {
    private static final Logger LOG = LoggerFactory.getLogger(Balance.class);
    private volatile BigDecimal amountBalance = BigDecimal.ZERO;
    private StatementPersistentService service;

    private Balance(StatementPersistentService service) {
        this.service = service;
    }

    public double getBalance() {
        return amountBalance.doubleValue();
    }

    /**
     * Static factory method which gets the {@link StatementPersistentService} to help count the balance.
     * @param service
     *            {@link StatementPersistentService}
     * @return the Balance instance.
     */
    public static Balance getInstance(StatementPersistentService service) {
        Balance balance = new Balance(service);
        balance.countBalance();

        return balance;
    }

    /**
     * Subtract the amount from the current stored balance.
     * @param amount
     *            amount to subtract.
     */
    public void subtract(BigDecimal amount) {
        LOG.debug("Subtracting " + amount.doubleValue() + " from " + amountBalance.doubleValue());
        amountBalance = amountBalance.subtract(amount);
        LOG.debug("The new balance is after subtracting: " + amountBalance.doubleValue());
    }

    /**
     * Add the amount from the current stored balance.
     * @param amount
     *            amount to add.
     */
    public void add(BigDecimal amount) {
        LOG.debug("Adding " + amount.doubleValue() + " to " + amountBalance.doubleValue());
        amountBalance = amountBalance.add(amount);
        LOG.debug("The new balance is after adding: " + amountBalance.doubleValue());
    }

    private void countBalance() {
        double expenses = countSumOfStatement(service.getStatement(StatementType.Expense));
        double incomes = countSumOfStatement(service.getStatement(StatementType.Income));

        amountBalance = BigDecimal.valueOf(incomes - expenses);
        LOG.debug("Starting balance is: " + amountBalance.doubleValue());
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
