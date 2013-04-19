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
import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.Statement.Builder;
import com.cashflow.domain.StatementType;
import com.google.inject.Inject;

/**
 * AddStatement Base class. Default type is Expense.
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementFragment extends RoboFragment {

    private static final Logger LOG = LoggerFactory.getLogger(AddStatementFragment.class);
    private static final int CREATE_CATEGORY_ACTIVITY_ID = 1;

    @Inject
    private CategoryDAO categoryDAO;
    @Inject
    private DateButtonOnClickListener dateListener;
    @Inject
    private StatementDAO statementDAO;

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
        LOG.debug("AddStatementFragment is creating...");
        return inflater.inflate(R.layout.add_expense_statement_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDateButton();
        setCategorySpinner();
        setUpButtons();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if ((requestCode == CREATE_CATEGORY_ACTIVITY_ID) && (resultCode == Activity.RESULT_OK)) {
            setCategorySpinner();
            categorySpinner.setSelection(categorySpinner.getAdapter().getCount() - 1);
        }
    }

    private void setUpButtons() {
        submit.setOnClickListener(new SubmitButtonOnClickListener());
        createCategory.setOnClickListener(new CreateCategoryOnClickListener());
    }

    private void setCategorySpinner() {
        final List<Category> list = categoryDAO.getAllCategories();

        final ArrayAdapter<Category> adapter =
                new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
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

        final Builder builder = Statement.builder(amountStr, date);
        builder.note(note).type(type).category(category);

        return builder.build();
    }

    class CreateCategoryOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
            startActivityForResult(intent, CREATE_CATEGORY_ACTIVITY_ID);
        }
    }

    /**
     * On click listener for submit button on AddStatement.
     * @author Janos_Gyula_Meszaros
     */
    class SubmitButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(final View view) {
            final Statement statement = createStatement();
            final Activity activity = getActivity();

            try {
                if (statementDAO.save(statement)) {
                    LOG.debug("Statement saved: " + statement.toString());
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                } else {
                    showToast(activity, activity.getString(R.string.database_error));
                }
            } catch (final IllegalArgumentException e) {
                showToast(activity, e.getMessage());
            }

        }

        private void showToast(final Activity activity, final String msg) {
            final Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
