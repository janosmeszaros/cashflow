package com.cashflow.statement.activity.list;

import static com.cashflow.constants.Constants.ID_EXTRA;

import java.util.List;

import android.content.Intent;

import com.cashflow.domain.Statement;
import com.cashflow.statement.activity.EditIncomeActivity;

public class ListRecurringIncomeFragment extends ListStatementFragment {

    @Override
    protected void editButtonOnClick() {
        final Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
        intent.putExtra(ID_EXTRA, selectedIds.get(0));
        startActivity(intent);
    }

    @Override
    protected List<Statement> getDataFromDatabase() {
        return statementDAO.getRecurringIncomes();
    }

}
