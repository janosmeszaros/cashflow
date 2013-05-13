package com.cashflow.dao.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cashflow.dao.objects.CategoryEntity;
import com.cashflow.dao.objects.StatementEntity;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

public class JPABasedStatementDaoTest {
    private static final long STATEMENT_ID = 1L;
    private static final long FAILED = -1L;
    private static final String ID_STR = "0";
    private static final String AMOUNT_STR = "1234";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "cat";
    private static final String NOTE = "note";
    private static final String DATE_STR = "2012.01.01";
    private static final String INTERVAL_STR = "daily";

    @Mock
    private GenericHibernateDAO<StatementEntity> dao;

    private JPABasedStatementDAO underTest;
    private final Mapper mapper = new DozerBeanMapper();
    private final Statement incomeStatement = Statement.builder(AMOUNT_STR, DATE_STR).note(NOTE).type(StatementType.Income)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).recurringInterval(RecurringInterval.valueOf(INTERVAL_STR))
            .statementId(ID_STR).build();
    private final StatementEntity statementEntity = createStatementEntity(incomeStatement);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        initMocks();

        underTest = new JPABasedStatementDAO(mapper, dao);
    }

    private void initMocks() {
        final List<StatementEntity> returnableList = new ArrayList<StatementEntity>();
        returnableList.add(statementEntity);
        when(dao.findByCriteria((Criterion[]) anyObject())).thenReturn(returnableList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenMapperIsNullThenShouldThrowException() {
        new JPABasedStatementDAO(null, dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenDAOIsNullThenShouldThrowException() {
        new JPABasedStatementDAO(mapper, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothArgsAreNullThenShouldThrowException() {
        new JPABasedStatementDAO(null, null);
    }

    @Test
    public void testSaveWhenStatementIsOkThenShouldConvertToEntityThenSave() {
        final StatementEntity savedStatement = new StatementEntity();
        savedStatement.setStatementId(STATEMENT_ID);
        when(dao.persist((StatementEntity) anyObject())).thenReturn(savedStatement);
        final StatementEntity statementEntity = createStatementEntity(incomeStatement);

        final long savedStatementId = underTest.save(incomeStatement);

        verify(dao).persist(statementEntity);
        assertThat(savedStatementId, equalTo(STATEMENT_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenStatementIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenEntityAlreadyExistsThenShouldReturnFalse() {
        final StatementEntity savedStatement = new StatementEntity();
        savedStatement.setStatementId(FAILED);
        when(dao.persist((StatementEntity) anyObject())).thenReturn(savedStatement);

        final long savedStatementId = underTest.save(incomeStatement);

        assertThat(savedStatementId, equalTo(FAILED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenStatementParamIsNullThenShouldThrowException() {
        underTest.update(null, ID_STR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenStatementIdParamIsEmptyThenShouldThrowException() {
        underTest.update(incomeStatement, "");
    }

    @Test
    public void testUpdateWhenStatementIsOkThenShouldUpdateStatement() {
        when(dao.merge((StatementEntity) anyObject())).thenReturn(true);
        final StatementEntity statementEntity = createStatementEntity(incomeStatement);

        final boolean isUpdated = underTest.update(incomeStatement, incomeStatement.getStatementId());

        verify(dao).merge(statementEntity);
        assertThat(isUpdated, equalTo(true));
    }

    @Test
    public void testUpdateWhenStatementIsOkButExceptionIsOccuredDuringSaveingThenShouldReturnFalse() {
        when(dao.merge((StatementEntity) anyObject())).thenReturn(false);

        final boolean isUpdated = underTest.update(incomeStatement, incomeStatement.getStatementId());

        assertThat(isUpdated, equalTo(false));
    }

    @Test
    public void testGetAllStatementsWhenCalledShouldReturnListOfStatements() {
        final List<StatementEntity> returnableList = new ArrayList<StatementEntity>();
        returnableList.add(statementEntity);
        when(dao.findByCriteria()).thenReturn(returnableList);

        final List<Statement> returnedList = underTest.getAllStatements();

        assertThat(returnedList, contains(incomeStatement));
    }

    @Test
    public void testGetExpensesWhenCalledShouldReturnListOfStatement() {
        final List<Statement> returnedList = underTest.getExpenses();

        assertThat(returnedList, contains(incomeStatement));
    }

    @Test
    public void testGetIncomesWhenCalledShouldReturnListOfStatement() {
        final List<Statement> returnedList = underTest.getIncomes();

        assertThat(returnedList, contains(incomeStatement));
    }

    @Test
    public void testGetRecurringIncomesWhenCalledShouldReturnListOfStatement() {
        final List<Statement> returnedList = underTest.getRecurringIncomes();

        assertThat(returnedList, contains(incomeStatement));
    }

    @Test
    public void testGetStatementByIdWhenCalledShouldReturnStatement() {
        when(dao.findById(Long.valueOf(ID_STR))).thenReturn(statementEntity);

        final Statement returnedStatement = underTest.getStatementById(ID_STR);

        assertThat(returnedStatement, equalTo(incomeStatement));
    }

    private StatementEntity createStatementEntity(final Statement statement) {
        final Category category = statement.getCategory();
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setCategoryId(Integer.parseInt(category.getCategoryId()));

        final StatementEntity entity = new StatementEntity();
        entity.setAmount(statement.getAmount());
        entity.setCategory(categoryEntity);
        entity.setDate(statement.getDate());
        entity.setNote(statement.getNote());
        entity.setRecurringInterval(statement.getRecurringInterval());
        entity.setStatementId(Integer.parseInt(statement.getStatementId()));
        entity.setType(statement.getType());

        return entity;
    }
}
