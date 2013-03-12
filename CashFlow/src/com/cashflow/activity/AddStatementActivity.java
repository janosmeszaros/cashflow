package com.cashflow.activity;

import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.cashflow.R;
import com.cashflow.components.DatePickerFragment;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.category.CategoryPersistenceService;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;

/**
 * Statement adding. It gets it's type in the intent in extra named by <code>STATEMENT_TYPE_EXTRA</code>
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementActivity extends RoboFragmentActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AddStatementActivity.class);

    private final String[] fromColumns = {COLUMN_NAME_CATEGORY_NAME};
    private final int[] toViews = {android.R.id.text1};

    @Inject
    private StatementPersistenceService statementService;
    @Inject
    private CategoryPersistenceService categoryService;

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @Inject
    private Balance balance;
    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("AddStatementActivity is creating...");

        setContentView(R.layout.activity_add_statement);
        setDateButtonText();
        setStatementType();
        setTitle();

        Cursor cursor = categoryService.getCategories();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursor, fromColumns, toViews);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelected(false);

        LOG.debug("AddStatementActivity has created with type: " + type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_expense, menu);
        return true;
    }

    /**
     * Onclick event for add statement button on add statement screen. Save the expense to database. If the save was successful then refresh the
     * balance else sets the result to canceled and close the activity. 
     * @param view
     *            Required for onClick.
     */
    public void submit(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        Statement statement = createStatement(amountStr, date, note, type);

        if (statementService.saveStatement(statement)) {
            refreshBalance(amountStr);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    /**
     * Show the date picker. DateButton on click method.
     * @param view
     *            Required for onclick.
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void refreshBalance(String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);

        if (type.isIncome()) {
            balance.add(amount);
        } else {
            balance.subtract(amount);
        }

    }

    private void setStatementType() {
        String statementType = getIntent().getStringExtra(STATEMENT_TYPE_EXTRA);
        if (isIncome(statementType)) {
            type = StatementType.Income;
        } else {
            type = StatementType.Expense;
        }
    }

    private boolean isIncome(String type) {
        return type.equals(INCOME_EXTRA);
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_add_income);
        } else {
            setTitle(R.string.title_activity_add_expense);
        }
    }

    private void setDateButtonText() {
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));
    }

    private Statement createStatement(String amountStr, String date, String note, StatementType type) {
        return new Statement.Builder(amountStr, date).setNote(note).setType(type).build();
    }
}
