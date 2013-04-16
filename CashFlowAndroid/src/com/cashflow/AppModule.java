package com.cashflow;

import android.widget.SpinnerAdapter;

import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.DatePickerFragment;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.bill.database.AndroidBillDAO;
import com.cashflow.category.database.AndroidCategoryDAO;
import com.cashflow.dao.BillDAO;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.database.DbHelperSQLiteDbProvider;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.service.Balance;
import com.cashflow.service.RecurringIncomeScheduler;
import com.cashflow.statement.database.AndroidStatementDAO;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * RoboGuice configuration file.
 * @author Janos_Gyula_Meszaros
 */
public class AppModule implements Module {

    @Override
    public void configure(final Binder binder) {
        binder.bind(BillDAO.class).to(AndroidBillDAO.class);
        binder.bind(CategoryDAO.class).to(AndroidCategoryDAO.class);
        binder.bind(StatementDAO.class).to(AndroidStatementDAO.class);
        binder.bind(DateButtonOnClickListener.class);
        binder.bind(DatePickerFragment.class);
        binder.bind(RecurringCheckBoxOnClickListener.class);
        binder.bind(SpinnerAdapter.class).toProvider(RecurringIntervalArrayAdapterProvider.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
    }

    @Provides
    @Singleton
    private Balance balanceProvider(final StatementDAO dao) {
        return Balance.getInstance(dao);
    }

    @Provides
    @Singleton
    private RecurringIncomeScheduler scheduler(final StatementDAO dao) {
        return new RecurringIncomeScheduler(dao);
    }

}
