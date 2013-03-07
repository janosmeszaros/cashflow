package com.cashflow.database.category;

import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create category for DAO.
 * @author Kornel_Refi
 *
 */
@Singleton
public class CategoryPersistenceService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryPersistenceService.class);
    private final CategoryDao dao;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param dao
     *            {@link CategoryDao} to use to save data. Can't be null.
     * @throws IllegalArgumentException
     *             when DAO is null.
     */
    @Inject
    public CategoryPersistenceService(CategoryDao dao) {
        validateInput(dao);
        this.dao = dao;
    }

    private void validateInput(Object obj, String... params) {
        for (String string : params) {
            Validate.notEmpty(string);
        }
        Validate.notNull(obj);
    }

    private ContentValues createContentValue(String note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_CATEGORY_NAME, note);

        LOG.debug("Content created: " + values);

        return values;
    }

    private boolean checkIfNotEmptyString(String name) {
        boolean result = true;
        if ("".equals(name)) {
            result = false;
        }
        return result;
    }

    /**
     * Creates the category from data and then saves it to database.
     * @param name
     *            name of the category.
     * @return <code>true</code> if saving was successful and the amount wasn't zero, <code>false</code> otherwise.
     */
    public boolean saveCategory(String name) {
        validateInput(name);
        boolean result = false;

        if (checkIfNotEmptyString(name)) {
            ContentValues values = createContentValue(name);
            dao.save(values);
            result = true;
        }
        return result;
    }

    /**
     * Updates category at the specified id.
     * @param id
     *            id.
     * @param name
     *            new name for the category.
     * @return true if successful.
     */
    public boolean updateCategory(String id, String name) {
        validateInput(name, id);

        boolean result = true;

        ContentValues value = createContentValue(name);
        dao.update(value, id);

        return result;
    }
}
