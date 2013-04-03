package com.cashflow.statement.activity;

import static android.view.View.VISIBLE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cashflow.R;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.statement.database.StatementType;
import com.google.inject.Inject;

/**
 * Add income fragment. Parent's class <code>setType</code> and <code>setRecurringSpinner</code> should called from onViewCreated method.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddIncomeFragment extends AddStatementFragment {
    private static final Logger LOG = LoggerFactory.getLogger(AddIncomeFragment.class);

    @Inject
    private RecurringCheckBoxOnClickListener checkBoxListener;
    @Inject
    private SpinnerAdapter spinnerAdapter;

    @InjectView(R.id.recurring_spinner)
    private Spinner recurringSpinner;
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @InjectView(R.id.recurring_checkbox_area_statement)
    private LinearLayout recurringCheckBoxArea;
    @InjectView(R.id.recurring_checkbox_statement)
    private CheckBox recurringCheckBox;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.add_income_statement_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LOG.debug("AddStatementFragment is creating...");

        setType(StatementType.Income);
        activateRecurringArea();
        setRecurringSpinner(recurringSpinner);

        LOG.debug("AddStatementFragment has created with type: " + getType());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recurringCheckBox.isChecked()) {
            recurringCheckBoxArea.setVisibility(VISIBLE);
        }
    }

    private void activateRecurringArea() {
        recurringCheckBox.setOnClickListener(checkBoxListener);
        recurringArea.setVisibility(VISIBLE);
        recurringSpinner.setAdapter(spinnerAdapter);
    }

}
