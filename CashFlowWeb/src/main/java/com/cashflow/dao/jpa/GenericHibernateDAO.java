package com.cashflow.dao.jpa;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements basic DAO features with hibernate.
 * @author Janos_Gyula_Meszaros
 * @param <T>
 */
public class GenericHibernateDAO<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericHibernateDAO.class);

    private SessionFactory sessionFactory;
    private Class<T> persistentClass;

    /**
     * Default constructor. Needed because spring AOP.
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
        Validate.notNull(persistentClass);
        Validate.notNull(sessionFactory);

        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Persist the given entity to database.
     * @param entity
     *            entity.
     * @return <code>true</code> if successful.
     */
    @Transactional
    public boolean persist(final T entity) {
        Validate.notNull(entity);

        boolean isSuccess = true;
        try {
            getSession().persist(entity);
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * Merge the given entity to database.
     * @param entity
     *            entity
     * @return <code>true</code> if successful.
     */
    @Transactional
    public boolean merge(final T entity) {
        Validate.notNull(entity);

        boolean isSuccess = true;
        try {
            getSession().merge(entity);
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
            isSuccess = false;
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
        final Criteria crit = getSession().createCriteria(persistentClass);
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
        return (T) getSession().get(persistentClass, entityId);

    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
