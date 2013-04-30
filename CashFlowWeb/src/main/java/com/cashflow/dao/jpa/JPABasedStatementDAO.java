package com.cashflow.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.dozer.Mapper;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cashflow.dao.StatementDAO;
import com.cashflow.dao.objects.StatementEntity;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

/**
 * Jpa based implementation for {@link StatementDAO}.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedStatementDAO implements StatementDAO {
    private static final String RECURRING_INTERVAL = "recurringInterval";
    private static final String TYPE = "type";
    private final Mapper mapper;
    private final GenericHibernateDAO<StatementEntity> dao;

    /**
     * Constructor.
     * @param mapper
     *            Dozer mapper.
     * @param dao
     *            dao.
     */
    @Autowired
    public JPABasedStatementDAO(final Mapper mapper, @Qualifier("statementGenericDAO") final GenericHibernateDAO<StatementEntity> dao) {
        Validate.notNull(mapper);
        Validate.notNull(dao);

        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public boolean save(final Statement statement) {
        Validate.notNull(statement);

        final StatementEntity entity = generateEntity(statement);
        return dao.persist(entity);
    }

    private StatementEntity generateEntity(final Statement statement) {
        return mapper.map(statement, StatementEntity.class);
    }

    @Override
    public boolean update(final Statement statement, final String statementId) {
        Validate.notNull(statement);
        Validate.notEmpty(statementId);

        final StatementEntity entity = generateEntity(statement);
        return dao.merge(entity);
    }

    @Override
    public List<Statement> getAllStatements() {
        final List<StatementEntity> entityList = dao.findByCriteria();
        return convertToStatementList(entityList);
    }

    private List<Statement> convertToStatementList(final List<StatementEntity> entityList) {
        final List<Statement> statementList = new ArrayList<Statement>();

        for (final StatementEntity entity : entityList) {
            final Statement statement = convertToStatement(entity);
            statementList.add(statement);
        }

        return statementList;
    }

    private Statement convertToStatement(final StatementEntity entity) {
        final Category category = Category.builder(entity.getCategory().getName()).categoryId(String.valueOf(entity.getCategory().getCategoryId()))
                .build();

        return Statement.builder(entity.getAmount(), entity.getDate()).category(category).note(entity.getNote())
                .recurringInterval(entity.getRecurringInterval()).statementId(String.valueOf(entity.getStatementId())).type(entity.getType()).build();
    }

    @Override
    public List<Statement> getExpenses() {
        final LogicalExpression expression = createNonRecurringStatementExpressionByType(StatementType.Expense);

        final List<StatementEntity> entityList = dao.findByCriteria(expression);
        return convertToStatementList(entityList);
    }

    @Override
    public List<Statement> getIncomes() {
        final LogicalExpression expression = createNonRecurringStatementExpressionByType(StatementType.Income);

        final List<StatementEntity> entityList = dao.findByCriteria(expression);
        return convertToStatementList(entityList);
    }

    private LogicalExpression createNonRecurringStatementExpressionByType(final StatementType statementType) {
        final SimpleExpression type = Restrictions.eq(TYPE, statementType);
        final SimpleExpression interval = Restrictions.eq(RECURRING_INTERVAL, RecurringInterval.none);
        return Restrictions.and(type, interval);
    }

    @Override
    public List<Statement> getRecurringIncomes() {
        final SimpleExpression expression = Restrictions.ne(RECURRING_INTERVAL, RecurringInterval.none);
        final List<StatementEntity> entityList = dao.findByCriteria(expression);
        return convertToStatementList(entityList);
    }

    @Override
    public Statement getStatementById(final String statementId) {
        final StatementEntity entity = dao.findById(Long.parseLong(statementId));
        return convertToStatement(entity);
    }
}
