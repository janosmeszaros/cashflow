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
    private BigDecimal balance = new BigDecimal(0);
    private StatementPersistentService service;

    private Balance(StatementPersistentService service) {
        this.service = service;
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

    private void countBalance() {
        double expenses = countSumOfStatement(service.getStatement(StatementType.Expense));
        double incomes = countSumOfStatement(service.getStatement(StatementType.Income));

        balance = BigDecimal.valueOf(incomes - expenses);
        LOG.debug("starting balance is" + balance.doubleValue());
    }

    private double countSumOfStatement(Cursor statement) {
        int index = statement.getColumnIndex(COLUMN_NAME_AMOUNT);
        double amount = 0L;

        while (!statement.isAfterLast()) {
            amount += statement.getLong(index);
            statement.moveToNext();
        }

        return amount;
    }

    public double getBalance() {
        return balance.doubleValue();
    }

}
