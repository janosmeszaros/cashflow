package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.ID_EXTRA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.category.database.CategoryPersistenceService;
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
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.submitButton)
    private Button submit;
    @Inject
    private StatementPersistenceService statementService;
    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private DateButtonOnClickListener listener;

    private StatementType type = StatementType.Expense;
    private Statement originalStatement;

    protected Statement getOriginalStatement() {
        return originalStatement;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("EditStatementActivity is creating...");

        setContent();
        setSubmitButton();
        setListenerForDateButton();
        getOriginalData();
        fillFieldsWithData();
        setTitle();
    }

    private void setSubmitButton() {
        submit.setOnClickListener(new SubmitButtonOnClickListener());
    }

    protected void setContent() {
        setContentView(R.layout.add_expense_statement_fragment);
    }

    protected boolean isValuesChanged() {
        boolean result = true;
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();

        if (amountStr.equals(originalStatement.getAmount())) {
            if (date.equals(originalStatement.getDate())) {
                if (note.equals(originalStatement.getNote())) {
                    if (category.equals(originalStatement.getCategory())) {
                        result = false;
                    }
                }
            }
        }

        return result;
    }

    protected void setTitle() {
        setTitle(R.string.title_activity_edit_expenses);
    }

    protected void fillFieldsWithData() {
        amountText.setText(originalStatement.getAmount());
        notesText.setText(originalStatement.getNote());
        dateButton.setText(originalStatement.getDate());
        type = originalStatement.getType();

        final List<Category> list = categoryService.getCategories();
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);
        final int position = adapter.getPosition(originalStatement.getCategory());

        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(position);
    }

    protected Statement createStatement() {
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final String id = originalStatement.getId();

        final Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setId(id).setCategory(category);

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

    /**
     * Submit onClick method. Save the statement to database. If the save was successful then refresh the balance 
     * and set the result to <code>RESULT_OK</code> then close the activity.
     * @author Janos_Gyula_Meszaros
     *
     */
    protected class SubmitButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Statement statement = createStatement();

            try {
                if (!isValuesChanged()) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                } else if (statementService.updateStatement(statement)) {
                    LOG.debug("Statement saved: " + statement.toString());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    showToast(getString(R.string.database_error));
                }
            } catch (IllegalArgumentException e) {
                showToast(e.getMessage());
            }

        }

        private void showToast(final String msg) {
            Toast toast = Toast.makeText(EditStatementActivity.this, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
