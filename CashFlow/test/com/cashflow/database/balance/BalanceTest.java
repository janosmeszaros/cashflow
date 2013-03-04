package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.MatrixCursor;

import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link Balance} test.
 * @author Kornel_Refi
 */
@RunWith(RobolectricTestRunner.class)
public class BalanceTest {
    private Balance underTest;
    @Mock
    private StatementPersistentService statementPersistentServiceMock;
    @Mock
    private MatrixCursor matrixCursorMock;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        when(statementPersistentServiceMock.getStatement(StatementType.Expense)).thenReturn(matrixCursorMock);
        when(statementPersistentServiceMock.getStatement(StatementType.Income)).thenReturn(matrixCursorMock);

        when(matrixCursorMock.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(matrixCursorMock.isAfterLast()).thenReturn(false, true, false, true);
        when(matrixCursorMock.getLong(0)).thenReturn(1L, 2L);

        underTest = Balance.getInstance(statementPersistentServiceMock);
    }

    @Test
    public void testGetBalanceShouldBeOne() {
        assertThat(underTest.getBalance(), equalTo(1D));
    }

    @Test
    public void testSubtractResultShouldBeZero() {
        underTest.subtract(BigDecimal.ONE);
        assertThat(underTest.getBalance(), equalTo(0D));
    }

    @Test
    public void testAddResultShouldBeTwo() {
        underTest.add(BigDecimal.ONE);
        assertThat(underTest.getBalance(), equalTo(2D));
    }
}
