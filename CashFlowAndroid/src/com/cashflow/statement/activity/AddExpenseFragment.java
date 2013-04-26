package com.cashflow.statement.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cashflow.R;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

/**
 * Add expense fragment.
 * @author Janos_Gyula_Meszaros
 */
public class AddExpenseFragment extends AddStatementFragment {
    private static final Logger LOG = LoggerFactory.getLogger(AddExpenseFragment.class);

    @InjectView(R.id.expenseDateButton)
    private Button dateButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        LOG.debug("AddExpenseFragment is creating...");
        return inflater.inflate(R.layout.add_expense_statement_fragment, container, false);
    }

    @Override
    protected Button getDateButton() {
        return dateButton;
    }

    @Override
    protected void activateRecurringArea() {
    }

    @Override
    protected Statement createStatement() {
        final String amountStr = getAmountText().getText().toString();
        final String date = dateButton.getText().toString();
        final String note = getNotesText().getText().toString();
        final Category category = (Category) getCategorySpinner().getSelectedItem();

        return Statement.builder(amountStr, date).note(note).type(StatementType.Expense).category(category).build();
    }

}
