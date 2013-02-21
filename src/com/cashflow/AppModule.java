package com.cashflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cashflow.database.DbHelperSQLiteDbProvider;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementDao;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * Roboguice configuration file.
 * @author Janos_Gyula_Meszaros
 */
public class AppModule implements Module {

    private static final Logger LOG = LoggerFactory.getLogger(AppModule.class);

    @Override
    public void configure(Binder binder) {
        binder.bind(StatementDao.class);
        binder.bind(StatementPersistentService.class);
        binder.bind(Balance.class).toProvider(BalanceProvider.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
    }

    private class BalanceProvider implements Provider<Balance> {
        private StatementPersistentService service;

        @Inject
        public BalanceProvider(StatementPersistentService service) {
            this.service = service;
        }

        @Override
        public Balance get() {
            LOG.debug("Creating balance");
            return Balance.getInstance(service);
        }
    }

}
