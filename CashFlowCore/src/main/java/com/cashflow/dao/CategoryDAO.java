package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Category;

/**
 * DAO Interface for {@link Category}.
 * @author Janos_Gyula_Meszaros
 */
public interface CategoryDAO {

    /**
     * Persists a {@link Category} into the database.
     * @param category
     *            {@link Category} for save.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    boolean save(Category category);

    /**
     * Updates a {@link Category} row with specified id.
     * @param category
     *            updated {@link Category}.
     * @param categoryId
     *            updatable {@link Category}'s id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    boolean update(Category category, String categoryId);

    /**
     * Returns all of the categories {@link Category}.
     * @return {@link List} of {@link Category} which contains all categories.
     */
    List<Category> getAllCategories();

}