package com.cashflow.database.balance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cashflow.activity.util.ListExpensesModule;
import com.cashflow.database.statement.StatementPersistentService;

/**
 * {@link Balance} test.
 * @author Kornel_Refi
 *
 */
public class BalanceTest {

    private Balance underTest;
    @Mock
    private StatementPersistentService statementPersistentServiceMock;

    @Before
    public void setup() {
        underTest = Balance.getInstance(statementPersistentServiceMock);
    }

    @After
    public void tearDown() {
        ListExpensesModule.tearDown();
    }

    @Test
    public void test() {
        System.out.println("Balance: " + underTest);
        Assert.assertTrue(true);
    }
}
