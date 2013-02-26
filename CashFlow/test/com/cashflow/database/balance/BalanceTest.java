package com.cashflow.database.balance;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.MatrixCursor;

import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;

/**
 * {@link Balance} test.
 * @author Kornel_Refi
 *
 */
public class BalanceTest {
    private static final Logger LOG = LoggerFactory.getLogger(BalanceTest.class);
    private Balance underTest;
    @Mock
    private StatementPersistentService statementPersistentServiceMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{COLUMN_NAME_AMOUNT});
        matrixCursor.addRow(new Object[]{1234L});
        when(statementPersistentServiceMock.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentServiceMock.getStatement(StatementType.Income)).thenReturn(matrixCursor);

        underTest = Balance.getInstance(statementPersistentServiceMock);
    }

    @Test
    public void test() {
        LOG.debug("Balance: " + underTest);
        Assert.assertTrue(true);
    }
}
