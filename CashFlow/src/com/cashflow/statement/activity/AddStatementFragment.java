package com.cashflow.statement.activity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.category.activity.CreateCategoryActivity;
import com.cashflow.category.database.CategoryPersistenceService;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.google.inject.Inject;

/**
 * AddStatement Base class. Default type is Expense.
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementFragment extends RoboFragment {

    private static final Logger LOG = LoggerFactory.getLogger(AddStatementFragment.class);

    @Inject
    private CategoryPersistenceService categoryService;
    @Inject
    private DateButtonOnClickListener dateListener;
    @Inject
    private StatementPersistenceService statementService;

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.categorySpinner)
    private Spinner categorySpinner;
    @InjectView(R.id.createCategoryButton)
    private ImageButton createCategory;
    @InjectView(R.id.submitButton)
    private Button submit;

    private final StatementType type = StatementType.Expense;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.add_expense_statement_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LOG.debug("AddStatementFragment is creating...");

        setUpDateButton();
        setCategorySpinner();
        setUpButtons();

        LOG.debug("AddStatementFragment has created with type: " + type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setCategorySpinner();
        categorySpinner.setSelection(categorySpinner.getAdapter().getCount() - 1);
    }

    private void setUpButtons() {
        submit.setOnClickListener(new SubmitButtonOnClickListener());
        createCategory.setOnClickListener(new CreateCategoryOnClickListener());
    }

    private void setCategorySpinner() {
        final List<Category> list = categoryService.getCategories();

        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
        categorySpinner.setAdapter(adapter);
    }

    private void setUpDateButton() {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));

        dateButton.setOnClickListener(dateListener);
    }

    protected Statement createStatement() {
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();

        final Builder builder = new Statement.Builder(amountStr, date);
        builder.setNote(note).setType(type).setCategory(category);

        return builder.build();
    }

    protected class CreateCategoryOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
            startActivityForResult(intent, 1);
        }

    }

    /**
     * On click listener for submit button on AddStatement. 
     * @author Janos_Gyula_Meszaros
     *
     */
    public class SubmitButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Statement statement = createStatement();
            final Activity activity = getActivity();

            try {
                if (statementService.saveStatement(statement)) {
                    LOG.debug("Statement saved: " + statement.toString());
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                } else {
                    showToast(activity, activity.getString(R.string.database_error));
                }
            } catch (IllegalArgumentException e) {
                showToast(activity, e.getMessage());
            }

        }

        private void showToast(final Activity activity, String msg) {
            Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
