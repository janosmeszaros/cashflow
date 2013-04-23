package com.cashflow.dao.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
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
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build())
            .recurringInterval(RecurringInterval.valueOf(INTERVAL_STR))
            .statementId(ID_STR).build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        underTest = new JPABasedStatementDAO(mapper, dao);
    }

    @Test
    public void testSaveWhenStatementIsOkThenShouldConvertToEntityThenSave() {
        when(dao.persist((StatementEntity) anyObject())).thenReturn(true);
        final StatementEntity statementEntity = createStatementEntity(incomeStatement);

        final boolean isSaved = underTest.save(incomeStatement);

        verify(dao).persist(statementEntity);
        assertThat(isSaved, equalTo(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenStatementIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenEntityAlreadyExistsThenShouldReturnFalse() {
        when(dao.persist((StatementEntity) anyObject())).thenReturn(false);

        final boolean isSaved = underTest.save(incomeStatement);

        assertThat(isSaved, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenStatementParamIsNullThenShouldThrowException() {
        underTest.update(null, "0");
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
