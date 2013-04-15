package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.MatrixCursor;

import com.cashflow.domain.StatementType;
import com.cashflow.service.StatementPersistenceService;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link Balance} test.
 * @author Kornel_Refi
 */
@RunWith(RobolectricTestRunner.class)
public class BalanceTest {
    private Balance underTest;
    @Mock
    private StatementPersistenceService statementPersistentServiceMock;
    @Mock
    private MatrixCursor matrixCursorMock;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        when(statementPersistentServiceMock.getStatement(StatementType.Expense)).thenReturn(matrixCursorMock);
        when(statementPersistentServiceMock.getStatement(StatementType.Income)).thenReturn(matrixCursorMock);

        when(matrixCursorMock.getColumnIndex(COLUMN_NAME_AMOUNT)).thenReturn(0);
        when(matrixCursorMock.isAfterLast()).thenReturn(false, true, false, true);
        when(matrixCursorMock.getDouble(0)).thenReturn(1D, 2D);

        underTest = Balance.getInstance(statementPersistentServiceMock);
    }

    @Test
    public void testGetBalanceShouldBeOne() {
        assertThat(underTest.getBalance(), equalTo(1D));
    }

}
