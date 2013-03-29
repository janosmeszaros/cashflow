package com.cashflow.statement.activity;

import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.category.database.CategoryPersistenceService;
import com.cashflow.constants.RecurringInterval;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.google.inject.Inject;

/**
 * Statement adding. It gets it's type in the intent in extra named by <code>STATEMENT_TYPE_EXTRA</code>.
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementActivity extends RoboFragmentActivity {

    private static final Logger LOG = LoggerFactory.getLogger(AddStatementActivity.class);

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
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_checkbox)
    private CheckBox recurringCheckBox;
    @Inject
    private DateButtonOnClickListener listener;
    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;
    private StatementType type;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("AddStatementActivity is creating...");

        setContentView(R.layout.activity_add_statement);
        setUpDateButton();
        setStatementType();
        setTitle();
        setCategorySpinner();

        LOG.debug("AddStatementActivity has created with type: " + type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCategorySpinner();
        categorySpinner.setSelection(categorySpinner.getAdapter().getCount() - 1);
    }

    private void setCategorySpinner() {
        final List<Category> list = categoryService.getCategories();

        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, list);
        categorySpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_expense, menu);
        return true;
    }

    /**
     * OnClick event for add statement button on add statement screen. Save the expense to database. If the save was successful then refresh the
     * balance else sets the result to canceled and close the activity. 
     * @param view
     *            Required for onClick.
     */
    public void submit(final View view) {
        final Statement statement = createStatement();

        if (statementService.saveStatement(statement)) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void setStatementType() {
        final Intent intent = getIntent();
        type = StatementType.valueOf(intent.getStringExtra(STATEMENT_TYPE_EXTRA));
        if (type.isIncome()) {
            activateRecurringArea();
        }
    }

    private void activateRecurringArea() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        recurringSpinner.setAdapter(spinnerAdapter);
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_add_income);
        } else {
            setTitle(R.string.title_activity_add_expense);
        }
    }

    private void setUpDateButton() {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));

        dateButton.setOnClickListener(listener);
    }

    private Statement createStatement() {
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();

        final Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setCategory(category);
        if (type.isIncome()) {
            builder.setRecurringInterval((RecurringInterval) recurringSpinner.getSelectedItem());
        }

        return builder.build();
    }

    /**
     * Create categories onClick method.
     * @param view
     *            Required for onClick.
     */
    public void createNewCategory(View view) {
        Intent intent = new Intent(this, CreateCategoryActivity.class);
        startActivity(intent);
    }

}
