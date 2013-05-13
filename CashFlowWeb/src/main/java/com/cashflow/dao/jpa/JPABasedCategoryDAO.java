package com.cashflow.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.objects.CategoryEntity;
import com.cashflow.domain.Category;

/**
 * Hibernate based category dao implementation.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedCategoryDAO implements CategoryDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPABasedCategoryDAO.class);
    private final Mapper mapper;
    private final GenericHibernateDAO<CategoryEntity> dao;

    /**
     * Constuctor.
     * @param dao
     *            {@link GenericHibernateDAO}
     * @param mapper
     *            {@link Mapper}
     */
    @Autowired
    public JPABasedCategoryDAO(final Mapper mapper, @Qualifier("categoryGenericDAO") final GenericHibernateDAO<CategoryEntity> dao) {
        Validate.notNull(mapper);
        Validate.notNull(dao);

        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public long save(final Category category) {
        final CategoryEntity categoryEntity = generateCategoryEntity(category);
        long newCategoryId;

        try {
            final CategoryEntity persistedCategory = dao.persist(categoryEntity);
            newCategoryId = persistedCategory.getCategoryId();
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
            newCategoryId = -1;
        }
        LOGGER.debug("Saved category id: " + newCategoryId);

        return newCategoryId;
    }

    private CategoryEntity generateCategoryEntity(final Category category) {
        return mapper.map(category, CategoryEntity.class);
    }

    @Override
    public boolean update(final Category category, final String categoryId) {
        final CategoryEntity categoryEntity = generateCategoryEntity(category);
        return dao.merge(categoryEntity);
    }

    @Override
    public List<Category> getAllCategories() {
        final List<CategoryEntity> entityList = dao.findByCriteria();
        return convertToCategoryList(entityList);
    }

    private List<Category> convertToCategoryList(final List<CategoryEntity> entityList) {
        final List<Category> categoryList = new ArrayList<Category>();

        for (final CategoryEntity entity : entityList) {
            final Category category = convertToCategory(entity);
            categoryList.add(category);
        }

        return categoryList;
    }

    private Category convertToCategory(final CategoryEntity entity) {
        return Category.builder(entity.getName()).categoryId(String.valueOf(entity.getCategoryId())).build();
    }

    @Override
    public Category getCategoryById(final String categoryid) {
        final CategoryEntity categoryEntity = dao.findById(Long.valueOf(categoryid));
        return convertToCategory(categoryEntity);
    }

}
