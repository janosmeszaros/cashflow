package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.ID_EXTRA;

import java.util.List;

import android.content.Intent;

import com.cashflow.domain.Statement;

public class ListExpenseFragment extends ListStatementFragment {

    @Override
    protected List<Statement> getDataFromDatabase() {
        return statementDAO.getExpenses();
    }

    @Override
    protected void editButtonOnClick() {
        final Intent intent = new Intent(getActivity(), EditStatementActivity.class);
        intent.putExtra(ID_EXTRA, selectedIds.get(0));
        startActivity(intent);
    }

}
