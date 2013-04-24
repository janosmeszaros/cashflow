package com.cashflow.dao.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cashflow.dao.objects.StatementEntity;

public class GenericHibernateDAOTest {

    private static final Class<StatementEntity> PERSISTENT_CLASS = StatementEntity.class;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Criteria criteria;

    private final StatementEntity entity = new StatementEntity();

    private GenericHibernateDAO<StatementEntity> underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createCriteria(PERSISTENT_CLASS)).thenReturn(criteria);

        underTest = new GenericHibernateDAO<StatementEntity>(PERSISTENT_CLASS, sessionFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenPersistentClassIsNullThenShouldThrowException() {
        new GenericHibernateDAO<StatementEntity>(null, sessionFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenSessionFactoryIsNullThenShouldThrowException() {
        new GenericHibernateDAO<StatementEntity>(PERSISTENT_CLASS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothParamsAreNullThenShouldThrowException() {
        new GenericHibernateDAO<StatementEntity>(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersistWhenParamIsNullThenShouldThrowException() {
        underTest.persist(null);
    }

    @Test
    public void testPersistWhenParamIsOkThenShouldSaveToDbAndReturnTrue() {
        final boolean isPersisted = underTest.persist(entity);

        assertThat(isPersisted, equalTo(true));
    }

    @Test
    public void testPersistWhenHibernateExceptionThrownThenShouldReturnFalse() {
        doThrow(new HibernateException("")).when(session).persist(entity);

        final boolean isPersisted = underTest.persist(entity);

        assertThat(isPersisted, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeWhenParamIsNullThenShouldThrowException() {
        underTest.merge(null);
    }

    @Test
    public void testFindByIdWhenCalledShouldCallGet() {
        underTest.findById(1L);

        verify(session).get(PERSISTENT_CLASS, 1L);
    }

    @Test
    public void testMergeWhenParamIsOkThenShouldMergeToDbAndReturnTrue() {
        final boolean isPersisted = underTest.merge(entity);

        assertThat(isPersisted, equalTo(true));
    }

    @Test
    public void testMergeWhenHibernateExceptionThrownThenShouldReturnFalse() {
        doThrow(new HibernateException("")).when(session).merge(entity);

        final boolean isPersisted = underTest.merge(entity);

        assertThat(isPersisted, equalTo(false));
    }

    @Test
    public void testFindByCriteriaWhenCalledWithMultipleCriterionsThenShouldCreateCriteriaAndAndThemToIt() {
        final Criterion empty = Restrictions.isEmpty("");
        final Criterion notEmpty = Restrictions.isNotEmpty("");

        underTest.findByCriteria(empty, notEmpty);

        verify(session).createCriteria(PERSISTENT_CLASS);
        verify(criteria).add(notEmpty);
        verify(criteria).add(empty);
        verify(criteria).list();
    }

    @Test
    public void testFindByCriteriaWhenCalledWithZeroCriterionThenShouldCreateCriterionAndCallListOnIt() {
        underTest.findByCriteria();

        verify(session).createCriteria(PERSISTENT_CLASS);
        verify(criteria, times(0)).add((Criterion) anyObject());
        verify(criteria).list();
    }

}
