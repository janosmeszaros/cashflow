package com.cashflow.statement.database;

import static com.cashflow.database.DatabaseContracts.AbstractStatement.EXPENSE_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION_WITH_ALIAS;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.RECURRING_INCOME_SELECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.SELECT_STATEMENT_BY_ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.STATEMENT_INNER_JOINED_CATEGORY;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.StatementDAO;
import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.AndroidParentDAO;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic DAO for the statements.
 * @author Kornel_Refi
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class AndroidStatementDAO extends AndroidParentDAO implements StatementDAO {
    private final SQLiteDbProvider provider;

    /**
     * Default constructor which gets a Provider. Can't be <code>null</code>.
     * @throws IllegalArgumentException when provider is <code>null</code>.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public AndroidStatementDAO(final SQLiteDbProvider provider) {
        super(provider, new AbstractStatement());
        nullCheck(provider);
        this.provider = provider;
    }

    @Override
    public boolean save(final Statement statement) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(final Statement statement, final String id) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Returns all the {@link Statement}.
     * @return {@link List} of {@link Statement} which contains the data.
     */
    @Override
    public List<Statement> getAllStatements() {
        final SQLiteDatabase database = provider.getReadableDb();
        final Cursor query = database.query(tableName, PROJECTION, null, null, null, null, null);

        final List<Statement> statements = new ArrayList<Statement>();
        return statements;
    }

    @Override
    public List<Statement> getExpenses() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, EXPENSE_SELECTION, null, null, null, null);
    }

    @Override
    public List<Statement> getIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION, INCOME_SELECTION, null, null, null, null);
    }

    @Override
    public List<Statement> getRecurringIncomes() {
        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.query(STATEMENT_INNER_JOINED_CATEGORY, PROJECTION_WITH_ALIAS, RECURRING_INCOME_SELECTION, null, null, null, null);
    }

    @Override
    public Statement getStatementById(final String statementId) {
        idCheck(statementId);

        final SQLiteDatabase dataBase = provider.getReadableDb();
        return dataBase.rawQuery(SELECT_STATEMENT_BY_ID, new String[]{statementId});
    }

    private void idCheck(final String statementId) {
        Validate.notEmpty(statementId);
    }

    private void nullCheck(final Object object) {
        Validate.notNull(object);
    }

}
