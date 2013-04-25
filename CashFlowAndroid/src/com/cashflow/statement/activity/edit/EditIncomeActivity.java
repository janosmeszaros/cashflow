package com.cashflow.statement.activity.edit;

import static android.view.View.VISIBLE;
import roboguice.inject.InjectView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.google.inject.Inject;

/**
 * Activity for modify incomes.
 * @author Janos_Gyula_Meszaros
 */
public class EditIncomeActivity extends EditStatementActivity {

    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_checkbox_income)
    private CheckBox recurringCheckBox;
    @InjectView(R.id.recurring_checkbox_area_income)
    private LinearLayout recurringCheckBoxArea;

    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;

    @Override
    protected void setContent() {
        setContentView(R.layout.add_income_statement_fragment);
    }

    @Override
    protected void setTitle() {
        setTitle(R.string.title_activity_edit_incomes);
    }

    @Override
    protected boolean isValuesChanged() {
        final RecurringInterval interval = (RecurringInterval) recurringSpinner.getSelectedItem();

        final boolean valuesChanged = super.isValuesChanged();

        final boolean recurringCheckBoxChanged = isRecurringCheckBoxChanged();
        final boolean changed = valuesChanged || (isIntervalChanged(interval) || recurringCheckBoxChanged);
        return changed;
    }

    private boolean isIntervalChanged(final RecurringInterval interval) {
        return !interval.equals(originalStatement.getRecurringInterval());
    }

    private boolean isRecurringCheckBoxChanged() {
        return originalStatement.getRecurringInterval().equals(RecurringInterval.none) == recurringCheckBox.isChecked();
    }

    @Override
    protected void fillFieldsWithData() {
        super.fillFieldsWithData();

        setUpRecurringSpinner();
    }

    @Override
    protected Statement createStatement() {
        RecurringInterval interval;
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final String statementId = originalStatement.getStatementId();

        if (recurringCheckBox.isChecked()) {
            interval = (RecurringInterval) recurringSpinner.getSelectedItem();
        } else {
            interval = RecurringInterval.none;
        }

        final Statement income =
                Statement.builder(amountStr, date).note(note).type(StatementType.Income)
                        .statementId(statementId).category(category)
                        .recurringInterval(interval).build();
        return income;
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

}
