package com.cashflow.statement.activity.edit;

import com.cashflow.R;
import com.cashflow.domain.Category;
import com.cashflow.domain.Statement;

/**
 * Edit expense activity.
 * @author Janos_Gyula_Meszaros
 */
public class EditExpenseActivity extends EditStatementActivity {

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
        final String amountStr = amountText.getText().toString();
        final String date = dateButton.getText().toString();
        final String note = notesText.getText().toString();
        final Category category = (Category) categorySpinner.getSelectedItem();
        final String statementId = originalStatement.getStatementId();

        return Statement.builder(amountStr, date).note(note).type(originalStatement.getType()).statementId(statementId).category(category)
                .build();
    }

}
