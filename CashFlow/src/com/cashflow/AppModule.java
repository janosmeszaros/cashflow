package com.cashflow;

import android.app.Application;

import com.cashflow.components.CustomCursorAdapter;
import com.cashflow.database.DbHelperSQLiteDbProvider;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementDao;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Roboguice configuration file.
 * @author Janos_Gyula_Meszaros
 */
public class AppModule implements Module {
    @Inject
    private Application application;

    @Override
    public void configure(Binder binder) {
        binder.bind(StatementDao.class);
        binder.bind(StatementPersistentService.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
        binder.bind(CustomCursorAdapter.class);
    }

    @Provides
    @Singleton
    private Balance balanceProvider(StatementPersistentService service) {
        return Balance.getInstance(service);
    }

}
