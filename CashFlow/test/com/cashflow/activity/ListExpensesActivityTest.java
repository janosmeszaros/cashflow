package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.MatrixCursor;
import android.view.View;
import android.widget.TextView;

import com.cashflow.R;
import com.cashflow.activity.util.ListExpensesModule;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowView;

@RunWith(RobolectricTestRunner.class)
public class ListExpensesActivityTest {

    @Mock
    StatementPersistentService statementPersistentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ListExpensesModule module = new ListExpensesModule();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE});
        matrixCursor.addRow(new Object[]{1234L, "2012"});
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(new MatrixCursor(new String[]{}));
        module.addBinding(StatementPersistentService.class, statementPersistentService);
        ListExpensesModule.setUp(this, module);
    }

    @After
    public void tearDown() {
        ListExpensesModule.tearDown();
    }

    @Test
    public void shouldContainList() {
        ListExpensesActivity activity = new ListExpensesActivity();
        activity.onCreate(null);

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);

        ShadowView list = shadowActivity.findViewById(R.layout.activity_list_statements);
        View row = list.findViewById(R.layout.list_statements_row);
        TextView bottom = (TextView) row.findViewById(R.id.bottomtext);
        TextView top = (TextView) row.findViewById(R.id.toptext);


        assertThat(bottom.getText().toString(), equalTo("1234"));
        assertThat(top.getText().toString(), equalTo("2012"));

    }
}
