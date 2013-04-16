package com.cashflow.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Statement;

/**
 * Class to keep the actual balance.
 * @author Janos_Gyula_Meszaros
 */
public final class Balance {
    private static final Logger LOG = LoggerFactory.getLogger(Balance.class);
    private final StatementDAO dao;

    private BigDecimal amountBalance = BigDecimal.ZERO;
    private BigDecimal expenses;
    private BigDecimal incomes;

    private Balance(final StatementDAO dao) {
        Validate.notNull(dao);
        this.dao = dao;
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
     * Static factory method which gets the {@link StatementDAO} to help count the balance.
     * @param dao
     *            {@link StatementDAO}
     * @return the Balance instance.
     */
    public static Balance getInstance(final StatementDAO dao) {
        final Balance balance = new Balance(dao);
        balance.countBalance();

        return balance;
    }

    /**
     * Calculates the current balance.
     */
    public void countBalance() {
        expenses = countSumOfStatement(dao.getExpenses());
        incomes = countSumOfStatement(dao.getIncomes());

        amountBalance = incomes.subtract(expenses);
        LOG.debug("Balance is: " + amountBalance.doubleValue());
    }

    private BigDecimal countSumOfStatement(final List<Statement> statements) {
        BigDecimal amount = BigDecimal.ZERO;

        for (final Statement statement : statements) {
            final double value = Double.valueOf(statement.getAmount());
            amount = amount.add(BigDecimal.valueOf(value));
        }

        return amount;
    }

}
