package com.cashflow.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
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
    public JPABasedCategoryDAO(@Qualifier("categoryGenericDAO") final GenericHibernateDAO<CategoryEntity> dao, final Mapper mapper) {
        super();
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public boolean save(final Category category) {
        final CategoryEntity categoryEntity = generateCategoryEntity(category);
        return dao.persist(categoryEntity);
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
        return mapper.map(entity, Category.class);
    }

}
