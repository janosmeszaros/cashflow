package com.cashflow;

import android.widget.SpinnerAdapter;

import com.cashflow.activity.bill.AddBillOnClickListener;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.activity.listeners.RecurringCheckBoxOnClickListener;
import com.cashflow.database.DbHelperSQLiteDbProvider;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.bill.BillDao;
import com.cashflow.database.bill.BillPersistenceService;
import com.cashflow.database.category.CategoryDao;
import com.cashflow.database.category.CategoryPersistenceService;
import com.cashflow.database.statement.RecurringIncomeScheduler;
import com.cashflow.database.statement.StatementDao;
import com.cashflow.database.statement.StatementPersistenceService;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Roboguice configuration file.
 * @author Janos_Gyula_Meszaros
 */
public class AppModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(CategoryDao.class);
        binder.bind(StatementDao.class);
        binder.bind(BillDao.class);
        binder.bind(CategoryPersistenceService.class);
        binder.bind(StatementPersistenceService.class);
        binder.bind(BillPersistenceService.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
        binder.bind(DateButtonOnClickListener.class);
        binder.bind(AddBillOnClickListener.class);
        binder.bind(RecurringCheckBoxOnClickListener.class);
        binder.bind(SpinnerAdapter.class).toProvider(RecurringIntervalArrayAdapterProvider.class);
        binder.bind(RecurringIncomeScheduler.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    private Balance balanceProvider(StatementPersistenceService service) {
        return Balance.getInstance(service);
    }

}
