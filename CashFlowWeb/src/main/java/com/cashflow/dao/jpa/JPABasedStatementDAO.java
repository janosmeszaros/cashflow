package com.cashflow.dao.jpa;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cashflow.dao.StatementDAO;
import com.cashflow.dao.objects.StatementEntity;
import com.cashflow.domain.Statement;

/**
 * Jpa based implementation for {@link StatementDAO}.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedStatementDAO implements StatementDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPABasedStatementDAO.class);
    private final SessionFactory sessionFactory;
    private final Mapper mapper;

    /**
     * Constructor.
     * @param sessionFactory
     *            sessionFactory.
     * @param mapper
     *            Dozer mapper.
     */
    public JPABasedStatementDAO(final SessionFactory sessionFactory, final Mapper mapper) {
        super();
        this.sessionFactory = sessionFactory;
        this.mapper = mapper;
    }

    @Override
    public boolean save(final Statement statement) {
        Validate.notNull(statement);

        final StatementEntity entity = generateEntity(statement);
        return persistStatementEntity(entity);
    }

    private boolean persistStatementEntity(final StatementEntity entity) {
        boolean isSuccess = false;
        try {
            sessionFactory.getCurrentSession().persist(entity);
            isSuccess = true;
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
        }
        return isSuccess;
    }

    private StatementEntity generateEntity(final Statement statement) {
        return mapper.map(statement, StatementEntity.class);
    }

    @Override
    public boolean update(final Statement statement, final String statementId) {
        Validate.notNull(statement);
        Validate.notEmpty(statementId);

        final StatementEntity entity = generateEntity(statement);
        return mergeStatement(entity);
    }

    private boolean mergeStatement(final StatementEntity entity) {
        boolean isSuccess = false;
        try {
            sessionFactory.getCurrentSession().merge(entity);
            isSuccess = true;
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
        }
        return isSuccess;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Statement> getAllStatements() {
        return sessionFactory.getCurrentSession().getNamedQuery("Statement.getAllStatements").list();
    }

    @Override
    public List<Statement> getExpenses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Statement> getIncomes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Statement> getRecurringIncomes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Statement getStatementById(final String statementId) {
        // TODO Auto-generated method stub
        return null;
    }

}
