package com.cashflow.statement.activity.edit;

import roboguice.inject.InjectView;
import android.widget.Button;

import com.cashflow.R;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;

/**
 * Edit expense activity.
 * @author Janos_Gyula_Meszaros
 */
public class EditExpenseActivity extends EditStatementActivity {

    @InjectView(R.id.expenseDateButton)
    private Button dateButton;

    @Override
    protected void setTitle() {
        setTitle(R.string.title_activity_edit_expenses);
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.add_expense_statement_fragment);
    }

    @Override
    protected Statement createStatement() {
        final String amountStr = getAmountText().getText().toString();
        final String date = dateButton.getText().toString();
        final String note = getNotesText().getText().toString();
        final Category category = (Category) getCategorySpinner().getSelectedItem();
        final String statementId = getOriginalStatement().getStatementId();

        return Statement.builder(amountStr, date).note(note).type(getOriginalStatement().getType()).statementId(statementId)
                .category(category)
                .build();
    }

    @Override
    protected Button getDateButton() {
        return dateButton;
    }

}
