package com.cashflow.database.category;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import com.cashflow.domain.Category;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to create {@link Category} for DAO.
 * @author Kornel_Refi
 *
 */
@Singleton
public class CategoryPersistenceService {
    private static final String EMPTY_STRING = "";
    private static final Logger LOG = LoggerFactory.getLogger(CategoryPersistenceService.class);
    private final CategoryDao dao;

    /**
     * Default constructor which gets a context for DbHelper.
     * @param dao
     *            {@link CategoryDao} to use to save data. Can't be <code>null</code>
     * @throws IllegalArgumentException
     *             when DAO is <code>null</code>
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
        if (EMPTY_STRING.equals(name)) {
            result = false;
        }
        return result;
    }

    /**
     * Creates the {@link Category} from data and then saves it to database.
     * @param name
     *            name of the category
     * @return <code>true</code> if saving was successful and the amount wasn't zero, otherwise <code>false</code>
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
     *            of the {@link Category}
     * @param name
     *            new name for the {@link Category}
     * @return <code>true</code> if successful
     */
    public boolean updateCategory(String id, String name) {
        validateInput(name, id);

        boolean result = true;

        ContentValues value = createContentValue(name);
        dao.update(value, id);

        return result;
    }

    /**
     * Get all categories.
     * @return List of {@link Category}
     */
    public List<Category> getCategories() {
        List<Category> list = new ArrayList<Category>();
        Cursor cursor = dao.getCategories();

        while (cursor.moveToNext()) {
            addNextCategoryToList(list, cursor);
        }

        return list;
    }

    private void addNextCategoryToList(List<Category> list, Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORY_NAME));

        list.add(new Category(id, name));
    }
}
