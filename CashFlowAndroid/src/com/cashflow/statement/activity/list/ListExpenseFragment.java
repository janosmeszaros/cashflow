package com.cashflow.statement.activity.list;

import static com.cashflow.constants.Constants.ID_EXTRA;

import java.util.List;

import android.content.Intent;

import com.cashflow.domain.Statement;
import com.cashflow.statement.activity.edit.EditExpenseActivity;

/**
 * List expense fragment.
 * @author Janos_Gyula_Meszaros
 */
public class ListExpenseFragment extends ListStatementFragment {

    @Override
    protected List<Statement> getDataFromDatabase() {
        return getStatementDAO().getExpenses();
    }

    @Override
    protected void editButtonOnClick() {
        final Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
        intent.putExtra(ID_EXTRA, getSelectedIds().get(0));
        startActivity(intent);
    }

}
