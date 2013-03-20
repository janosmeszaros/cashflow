package com.cashflow.activity;

import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.category.CategoryPersistenceService;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.google.inject.Inject;

/**
 * Activity for editing statements. It gets the previous values through the intent. 
 * 
 * <p>
 * Extras' names used to get original values: <br/>
 *  <ul> 
 *      <li>ID:      <code>ID_EXTRA</code></li>
 *  </ul>
 * </p>
 * 
 * @author Janos_Gyula_Meszaros
 */
public class EditStatementActivity extends RoboFragmentActivity {
    private static final Logger LOG = LoggerFactory.getLogger(EditStatementActivity.class);
    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_checkbox)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.recurring_checkbox_area)
    private LinearLayout recurringCheckBoxArea;
    @Inject
    private StatementPersistenceService statementService;
    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private Balance balance;
    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;

    private Statement originalStatement;
    private String originalId;

    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("EditStatementActivity is creating...");
        setContentView(R.layout.activity_add_statement);

        setListenerForDateButton();
        getOriginalData();
        fillFieldsWithData();
        setStatementType();
        setTitle();

        LOG.debug("EditStatementActivity has created with type: " + type);
    }

    /**
     * Submit onClick method. Save the statement to database. If the save was successful then refresh the balance 
     * and set the result to <code>RESULT_OK</code> then close the activity.
     * @param view
     *            Required for onclick.
     */
    public void submit(View view) {
        Statement statement = createStatement();

        if (isValuesChanged() && statementService.updateStatement(statement)) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private boolean isValuesChanged() {
        boolean result = true;
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        Category category = (Category) categorySpinner.getSelectedItem();
        RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();

        if (amountStr.equals(originalStatement.getAmount()) && date.equals(originalStatement.getDate()) && note.equals(originalStatement.getNote())
                && interval.equals(originalStatement.getRecurringInterval()) && category.equals(originalStatement.getCategory())) {
            result = false;
        }
        return result;
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_edit_incomes);
        } else {
            setTitle(R.string.title_activity_edit_expenses);
        }
    }

    private void fillFieldsWithData() {
        amountText.setText(originalStatement.getAmount());
        notesText.setText(originalStatement.getNote());
        dateButton.setText(originalStatement.getDate());

        List<Category> list = categoryService.getCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);
        int position = adapter.getPosition(originalStatement.getCategory());

        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(position);
    }

    private void setStatementType() {
        Intent intent = getIntent();
        type = StatementType.valueOf(intent.getStringExtra(STATEMENT_TYPE_EXTRA));

        if (type.isIncome()) {
            setUpRecurringSpinner();
        }
    }

    private void setUpRecurringSpinner() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        bindValuesToSpinner();
        setSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void setSelectedItem() {
        RecurringInterval interval = originalStatement.getRecurringInterval();
        if (!RecurringInterval.none.equals(interval)) {
            recurringCheckBox.setChecked(true);
            recurringCheckBoxArea.setVisibility(VISIBLE);
            recurringSpinner.setSelection(((ArrayAdapter<RecurringInterval>) spinnerAdapter).getPosition(interval));
        }
    }

    private void bindValuesToSpinner() {
        recurringSpinner.setAdapter(spinnerAdapter);
    }

    private Statement createStatement() {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        Category category = (Category) categorySpinner.getSelectedItem();
        String id = originalId;

        Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setId(id).setCategory(category);

        if (type.isIncome()) {
            RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();
            builder.setRecurringInterval(interval);
        }

        return builder.build();
    }

    private void setListenerForDateButton() {
        dateButton.setOnClickListener(listener);
    }

    private void getOriginalData() {
        Intent intent = getIntent();
        originalId = intent.getStringExtra(ID_EXTRA);

        originalStatement = statementService.getStatementById(originalId);
    }
}
