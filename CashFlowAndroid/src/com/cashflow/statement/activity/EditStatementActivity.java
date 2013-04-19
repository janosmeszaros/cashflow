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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.google.inject.Inject;

/**
 * Activity for editing statements. It gets the previous values through the intent.
 * <p>
 * Extras' names used to get original values: <br/>
 * <ul>
 * <li>ID: <code>ID_EXTRA</code></li>
 * </ul>
 * </p>
 * @author Janos_Gyula_Meszaros
 */
public class EditStatementActivity extends RoboFragmentActivity {
    private static final int REQUEST_CODE_FOR_CATEGORY = 1;

    private static final Logger LOG = LoggerFactory.getLogger(EditStatementActivity.class);

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.createCategoryButton)
    private ImageButton createCategoryButton;
    @InjectView(R.id.submitButton)
    private Button submit;
    @Inject
    private StatementDAO statementDAO;
    @Inject
    private CategoryDAO categoryDAO;
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
        setUpButtons();
        getOriginalData();
        fillFieldsWithData();
        setTitle();
    }

    private void setUpButtons() {
        createCategoryButton.setOnClickListener(new CreateCategoryOnClickListener());
        submit.setOnClickListener(new SubmitButtonOnClickListener());
        dateButton.setOnClickListener(listener);
    }

    protected void setContent() {
        setContentView(R.layout.add_expense_statement_fragment);
    }

    protected boolean isValuesChanged() {
        final String amount = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();

        return isSomeThingChanged(amount, date, note, category);
    }

    private boolean isSomeThingChanged(final String amount, final String date, final String note, final Category category) {
        return !amount.equals(originalStatement.getAmount()) || !date.equals(originalStatement.getDate())
                || !note.equals(originalStatement.getNote())
                || !category.equals(originalStatement.getCategory());
    }

    protected void setTitle() {
        setTitle(R.string.title_activity_edit_expenses);
    }

    protected void fillFieldsWithData() {
        amountText.setText(originalStatement.getAmount());
        notesText.setText(originalStatement.getNote());
        dateButton.setText(originalStatement.getDate());
        type = originalStatement.getType();

        final ArrayAdapter<Category> adapter = setCategorySpinner();
        final int position = adapter.getPosition(originalStatement.getCategory());
        categorySpinner.setSelection(position);
    }

    private ArrayAdapter<Category> setCategorySpinner() {
        final List<Category> list = categoryDAO.getAllCategories();
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);

        categorySpinner.setAdapter(adapter);
        return adapter;
    }

    protected Statement createStatement() {
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final String statementId = originalStatement.getId();

        return Statement.builder(amountStr, date).note(note).type(type).statementId(statementId).category(category).build();
    }

    private void getOriginalData() {
        final Intent intent = getIntent();
        final String statementId = intent.getStringExtra(ID_EXTRA);
        originalStatement = statementDAO.getStatementById(statementId);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if ((requestCode == REQUEST_CODE_FOR_CATEGORY) && (resultCode == RESULT_OK)) {
            final ArrayAdapter<Category> adapter = setCategorySpinner();
            categorySpinner.setSelection(adapter.getCount() - 1);
        }
    }

    protected class CreateCategoryOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent(EditStatementActivity.this, CreateCategoryActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FOR_CATEGORY);
        }

    }

    /**
     * Submit onClick method. Save the statement to database. If the save was successful then refresh the balance and set the result to
     * <code>RESULT_OK</code> then close the activity.
     * @author Janos_Gyula_Meszaros
     */
    protected class SubmitButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            try {
                if (isValuesChanged()) {
                    final Statement statement = createStatement();

                    if (statementDAO.update(statement, originalStatement.getId())) {
                        LOG.debug("Statement saved: " + statement.toString());
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        showToast(getString(R.string.database_error));
                    }
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            } catch (final IllegalArgumentException e) {
                showToast(e.getMessage());
            }
        }

        private void showToast(final String msg) {
            final Toast toast = Toast.makeText(EditStatementActivity.this, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
