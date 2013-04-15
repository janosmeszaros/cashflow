package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

import com.cashflow.domain.StatementType;
import com.cashflow.service.StatementPersistenceService;

/**
 * Class to keep the actual balance. Thread safe access to the balance field.
 * @author Janos_Gyula_Meszaros
 */
public final class Balance {
    private static final Logger LOG = LoggerFactory.getLogger(Balance.class);
    private BigDecimal amountBalance = BigDecimal.ZERO;
    private final StatementPersistenceService service;
    private BigDecimal expenses;
    private BigDecimal incomes;

    private Balance(final StatementPersistenceService service) {
        this.service = service;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getIncomes() {
        return incomes;
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
    public static Balance getInstance(final StatementPersistenceService service) {
        final Balance balance = new Balance(service);
        balance.countBalance();

        return balance;
    }

    /**
     * Calculates the current balance.
     */
    public void countBalance() {
        expenses = countSumOfStatement(service.getStatement(StatementType.Expense));
        incomes = countSumOfStatement(service.getStatement(StatementType.Income));

        amountBalance = incomes.subtract(expenses);
        LOG.debug("Balance is: " + amountBalance.doubleValue());
    }

    private BigDecimal countSumOfStatement(final Cursor cursor) {
        final int index = cursor.getColumnIndex(COLUMN_NAME_AMOUNT);
        BigDecimal amount = BigDecimal.ZERO;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final double value = cursor.getDouble(index);
            amount = amount.add(BigDecimal.valueOf(value));
            cursor.moveToNext();
        }

        return amount;
    }

}
