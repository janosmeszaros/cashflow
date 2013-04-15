package com.cashflow.dao;

import java.util.List;

import com.cashflow.domain.Category;

public interface CategoryDAO {

    /**
     * Persists values to the database.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(Category category);

    /**
     * Updates a row with specified id.
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(Category category, String id);

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    public List<Category> getAllCategories();

}