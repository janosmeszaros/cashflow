package com.cashflow.statement.activity;

import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.ID_EXTRA;

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
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.category.database.CategoryPersistenceService;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
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
    @InjectView(R.id.recurring_checkbox_income)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.recurring_checkbox_area_income)
    private LinearLayout recurringCheckBoxArea;
    @Inject
    private StatementPersistenceService statementService;
    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;

    private Statement originalStatement;

    private StatementType type;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("EditStatementActivity is creating...");
        setContentView(R.layout.add_expense_statement_fragment);

        setListenerForDateButton();
        getOriginalData();
        fillFieldsWithData();
        setTitle();

        LOG.debug("EditStatementActivity has created with type: " + type);
    }

    /**
     * Submit onClick method. Save the statement to database. If the save was successful then refresh the balance 
     * and set the result to <code>RESULT_OK</code> then close the activity.
     * @param view
     *            Required for onClick.
     */
    public void submit(final View view) {
        final Statement statement = createStatement();

        if (isValuesChanged() && statementService.updateStatement(statement)) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private boolean isValuesChanged() {
        boolean result = true;
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();

        if (amountStr.equals(originalStatement.getAmount())) {
            if (date.equals(originalStatement.getDate())) {
                if (interval.equals(originalStatement.getRecurringInterval())) {
                    if (note.equals(originalStatement.getNote())) {
                        if (category.equals(originalStatement.getCategory())) {
                            result = false;
                        }
                    }
                }
            }
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
        type = originalStatement.getType();

        if (type.isIncome()) {
            setUpRecurringSpinner();
        }

        final List<Category> list = categoryService.getCategories();
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);
        final int position = adapter.getPosition(originalStatement.getCategory());

        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(position);
    }

    private void setUpRecurringSpinner() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        bindValuesToSpinner();
        setSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void setSelectedItem() {
        final RecurringInterval interval = originalStatement.getRecurringInterval();
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
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final String id = originalStatement.getId();

        final Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setId(id).setCategory(category);

        if (type.isIncome()) {
            final RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();
            builder.setRecurringInterval(interval);
        }

        return builder.build();
    }

    private void setListenerForDateButton() {
        dateButton.setOnClickListener(listener);
    }

    private void getOriginalData() {
        final Intent intent = getIntent();
        final String id = intent.getStringExtra(ID_EXTRA);
        originalStatement = statementService.getStatementById(id);
    }
}
