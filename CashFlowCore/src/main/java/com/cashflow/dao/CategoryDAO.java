package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Category;

/**
 * Interface for {@link CategoryDAO}.
 * @author Janos_Gyula_Meszaros
 */
public interface CategoryDAO {

    /**
     * Persists values to the database.
     * @param category
     *            category for save.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    boolean save(Category category);

    /**
     * Updates a row with specified id.
     * @param category
     *            updated {@link Category}.
     * @param id
     *            updatable {@link Category}'s id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    boolean update(Category category, String id);

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    List<Category> getAllCategories();

}