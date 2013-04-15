package com.cashflow;

import android.widget.SpinnerAdapter;

import com.cashflow.activity.components.DateButtonOnClickListener;
import com.cashflow.activity.components.DatePickerFragment;
import com.cashflow.activity.components.RecurringCheckBoxOnClickListener;
import com.cashflow.bill.database.AndroidBillDAO;
import com.cashflow.category.database.AndroidCategoryDAO;
import com.cashflow.database.DbHelperSQLiteDbProvider;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.balance.Balance;
import com.cashflow.service.BillPersistenceService;
import com.cashflow.service.CategoryPersistenceService;
import com.cashflow.service.RecurringIncomeScheduler;
import com.cashflow.service.StatementPersistenceService;
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
        binder.bind(AndroidBillDAO.class);
        binder.bind(AndroidCategoryDAO.class);
        binder.bind(AndroidStatementDAO.class);
        binder.bind(BillPersistenceService.class);
        binder.bind(CategoryPersistenceService.class);
        binder.bind(DateButtonOnClickListener.class);
        binder.bind(DatePickerFragment.class);
        binder.bind(RecurringCheckBoxOnClickListener.class);
        binder.bind(RecurringIncomeScheduler.class).in(Singleton.class);
        binder.bind(SpinnerAdapter.class).toProvider(RecurringIntervalArrayAdapterProvider.class);
        binder.bind(StatementPersistenceService.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
    }

    @Provides
    @Singleton
    private Balance balanceProvider(final StatementPersistenceService service) {
        return Balance.getInstance(service);
    }

}
