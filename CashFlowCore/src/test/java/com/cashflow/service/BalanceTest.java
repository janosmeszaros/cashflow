package com.cashflow.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Statement;

/**
 * {@link Balance} test.
 * @author Kornel_Refi
 */
public class BalanceTest {
    private static final String FIVE_HUNDRED = "500";
    private static final String THOUSAND = "1000";
    private static final String DATE = "123";
    private Balance underTest;
    @Mock
    private StatementDAO daoMock;

    private List<Statement> incomeList;
    private List<Statement> expenseList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        incomeList = new ArrayList<Statement>();
        expenseList = new ArrayList<Statement>();

        when(daoMock.getExpenses()).thenReturn(expenseList);
        when(daoMock.getIncomes()).thenReturn(incomeList);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWhenStatementDaoIsNullThenShouldThrowException() {
        Balance.getInstance(null);
    }

    @Test
    public void testGetBalanceShouldBe500() {
        incomeList.add(Statement.builder(THOUSAND, DATE).build());
        expenseList.add(Statement.builder(FIVE_HUNDRED, DATE).build());

        underTest = Balance.getInstance(daoMock);

        assertThat(underTest.getBalance(), equalTo(500D));
    }

    @Test
    public void testGetBalanceShouldBeNegativ500() {
        incomeList.add(Statement.builder(FIVE_HUNDRED, DATE).build());
        expenseList.add(Statement.builder(THOUSAND, DATE).build());

        underTest = Balance.getInstance(daoMock);

        assertThat(underTest.getBalance(), equalTo(-500D));
    }

    @Test
    public void testGetBalanceShouldBeZero() {
        incomeList.add(Statement.builder(THOUSAND, DATE).build());
        expenseList.add(Statement.builder(THOUSAND, DATE).build());

        underTest = Balance.getInstance(daoMock);

        assertThat(underTest.getBalance(), equalTo(0D));
    }

}
