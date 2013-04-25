package com.cashflow.statement.activity.edit;

import static android.view.View.VISIBLE;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.google.inject.Inject;

/**
 * Activity for modify incomes.
 * @author Janos_Gyula_Meszaros
 *
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        boolean isChanged = super.isValuesChanged();

        if (!isChanged && !interval.equals(getOriginalStatement().getRecurringInterval())) {
            isChanged = true;
        }
        return isChanged;
    }

    @Override
    protected void fillFieldsWithData() {
        super.fillFieldsWithData();

        setUpRecurringSpinner();
    }

    @Override
    protected Statement createStatement() {
        final Statement expense = super.createStatement();
        final Statement income = Statement.builder(expense.getAmount(), expense.getDate()).note(expense.getNote()).type(StatementType.Income)
                .statementId(expense.getStatementId()).category(expense.getCategory())
                .recurringInterval((RecurringInterval) recurringSpinner.getSelectedItem()).build();
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
        final RecurringInterval interval = getOriginalStatement().getRecurringInterval();
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
