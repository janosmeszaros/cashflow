package com.cashflow.activity;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_AMOUNT;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.COLUMN_NAME_DATE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import android.app.Application;
import android.database.MatrixCursor;
import android.widget.Adapter;
import android.widget.ListView;

import com.cashflow.R;
import com.cashflow.activity.util.ListExpensesModule;
import com.cashflow.components.CustomCursorAdapter;
import com.cashflow.database.statement.StatementPersistentService;
import com.cashflow.database.statement.StatementType;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowListView;

@RunWith(RobolectricTestRunner.class)
public class ListExpensesActivityTest {

    @Spy
    Application app = new Application();
    @Mock
    StatementPersistentService statementPersistentService;
    @Spy
    CustomCursorAdapter adapter = new CustomCursorAdapter(app);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ListExpensesModule module = new ListExpensesModule();
        MatrixCursor matrixCursor =
                new MatrixCursor(new String[] { COLUMN_NAME_AMOUNT, COLUMN_NAME_DATE });
        matrixCursor.addRow(new Object[] { 1234L, "2012" });
        when(statementPersistentService.getStatement(StatementType.Expense)).thenReturn(matrixCursor);
        when(statementPersistentService.getStatement(StatementType.Income)).thenReturn(new MatrixCursor(new String[] {}));

        module.addBinding(StatementPersistentService.class, statementPersistentService);
        module.addBinding(CustomCursorAdapter.class, adapter);
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

        // ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        ListView list = (ListView) activity.findViewById(R.id.list_statement);
        ShadowListView shadowList = Robolectric.shadowOf(list);
        Adapter adapter = shadowList.getAdapter();
        // View bottom = shadowList.findViewById(R.layout.list_statements_row);
        assertThat(adapter.getCount(), equalTo(0));
        // assertThat(top.getText().toString(), equalTo("2012"));

    }
}
