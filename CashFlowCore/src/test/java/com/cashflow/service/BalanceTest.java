package com.cashflow.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

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
    private Balance underTest;
    @Mock
    private StatementDAO daoMock;
    @Mock
    private List<Statement> incomeList;
    @Mock
    private List<Statement> expenseList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(daoMock.getExpenses()).thenReturn(expenseList);
        when(daoMock.getIncomes()).thenReturn(incomeList);

        underTest = Balance.getInstance(daoMock);
    }

    @Test
    public void testGetBalanceShouldBeOne() {
        assertThat(underTest.getBalance(), equalTo(1D));
    }

}
