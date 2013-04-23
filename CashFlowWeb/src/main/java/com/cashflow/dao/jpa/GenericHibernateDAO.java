package com.cashflow.dao.jpa;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements basic dao features with hibernate.
 * @author Janos_Gyula_Meszaros
 * @param <T>
 */
public class GenericHibernateDAO<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericHibernateDAO.class);

    private SessionFactory sessionFactory;
    private Class<T> persistentClass;

    /**
     * Default constructor. Needed because spring aop.
     */
    public GenericHibernateDAO() {
    }

    /**
     * Constructor.
     * @param sessionFactory
     *            sessionFactory
     * @param persistentClass
     *            class to persist
     */
    public GenericHibernateDAO(final Class<T> persistentClass, final SessionFactory sessionFactory) {
        super();
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Persist the given entity to db.
     * @param entity
     *            entity.
     * @return <code>true</code> if succesfull.
     */
    @Transactional
    public boolean persist(final T entity) {
        boolean isSuccess = false;
        try {
            sessionFactory.getCurrentSession().persist(entity);
            isSuccess = true;
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
        }
        return isSuccess;
    }

    /**
     * Merge the given entity to db.
     * @param entity
     *            entity
     * @return <code>true</code> if successful.
     */
    @Transactional
    public boolean merge(final T entity) {
        boolean isSuccess = false;
        try {
            sessionFactory.getCurrentSession().merge(entity);
            isSuccess = true;
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
        }
        return isSuccess;
    }

    /**
     * Get entities by one or more criteria.
     * @param criterion
     *            set of {@link Criterion}
     * @return list of entities.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(final Criterion... criterion) {
        final Criteria crit = sessionFactory.getCurrentSession().createCriteria(persistentClass);
        if (criterion != null) {
            for (final Criterion c : criterion) {
                crit.add(c);
            }
        }

        return crit.list();
    }

    /**
     * Find an entity by id.
     * @param entityId
     *            id
     * @return entity.
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public T findById(final long entityId) {
        return (T) sessionFactory.getCurrentSession().load(persistentClass, entityId);

    }

}
