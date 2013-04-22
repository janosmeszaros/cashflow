package com.cashflow.dao.jpa;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cashflow.dao.StatementDAO;
import com.cashflow.dao.objects.StatementEntity;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

/**
 * Jpa based implementation for {@link StatementDAO}.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedStatementDAO implements StatementDAO {
    private static final String TYPE = "type";
    private static final String GET_STATEMENTS_BY_TYPE = "Statement.getStatementsByType";
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
    @Autowired
    public JPABasedStatementDAO(final SessionFactory sessionFactory, final Mapper mapper) {
        super();
        this.sessionFactory = sessionFactory;
        this.mapper = mapper;
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<Statement> getAllStatements() {
        return sessionFactory.getCurrentSession().getNamedQuery("Statement.getAllStatements").list();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Statement> getExpenses() {
        return sessionFactory.getCurrentSession().getNamedQuery(GET_STATEMENTS_BY_TYPE)
                .setString(TYPE, StatementType.Expense.toString()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Statement> getIncomes() {
        return sessionFactory.getCurrentSession().getNamedQuery(GET_STATEMENTS_BY_TYPE)
                .setString(TYPE, StatementType.Income.toString()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Statement> getRecurringIncomes() {
        return sessionFactory.getCurrentSession().getNamedQuery("Statement.getRecurringStatements").list();
    }

    @Override
    @Transactional
    public Statement getStatementById(final String statementId) {
        return (Statement) sessionFactory.getCurrentSession().getNamedQuery("Statement.getStatementsById")
                .setInteger("id", Integer.parseInt(statementId)).uniqueResult();
    }

}
