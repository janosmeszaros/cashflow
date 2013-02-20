package com.cashflow;

import com.cashflow.database.statement.DbHelperSQLiteDbProvider;
import com.cashflow.database.statement.SQLiteDbProvider;
import com.cashflow.database.statement.StatementDao;
import com.cashflow.database.statement.StatementPersistentService;
import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Roboguice configuration file.
 * @author Janos_Gyula_Meszaros
 */
public class AppModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(StatementDao.class);
        binder.bind(StatementPersistentService.class);
        binder.bind(SQLiteDbProvider.class).to(DbHelperSQLiteDbProvider.class);
    }

}
