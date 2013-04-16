package com.cashflow.category.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.TABLE_NAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Category;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DAO based on Android for {@link Category}.
 * @author Kornel_Refi
 */
@Singleton
public class AndroidCategoryDAO implements CategoryDAO {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidCategoryDAO.class);
    private static final String EQUALS = " = ?";
    private final SQLiteDbProvider provider;

    /**
     * Default constructor which get an Provider.
     * @param provider
     *            {@link SQLiteDbProvider} to get database.
     */
    @Inject
    public AndroidCategoryDAO(final SQLiteDbProvider provider) {
        Validate.notNull(provider);
        this.provider = provider;
    }

    @Override
    public boolean save(final Category category) {
        Validate.notNull(category);

        boolean isSuccessful;

        final long newRowId = provider.getWritableDb().insert(TABLE_NAME, null, createContentValues(category.getName()));

        if (newRowId >= 0) {
            isSuccessful = true;
            LOG.debug("New row created with row ID: " + newRowId);
        } else {
            isSuccessful = false;
        }

        return isSuccessful;
    }

    @Override
    public boolean update(final Category category, final String categoryId) {
        Validate.notNull(category);
        Validate.notEmpty(categoryId);

        final ContentValues values = createContentValues(category.getName());
        values.put(_ID, categoryId);

        boolean isSuccessful;
        final int update = provider.getWritableDb().update(TABLE_NAME, values, _ID + EQUALS, new String[] { categoryId });

        if (update > 0) {
            isSuccessful = true;
        } else {
            isSuccessful = false;
        }

        LOG.debug("Num of rows updated: " + update);
        return isSuccessful;
    }

    @Override
    public List<Category> getAllCategories() {
        final SQLiteDatabase database = provider.getReadableDb();
        final Cursor query = database.query(TABLE_NAME, PROJECTION, null, null, null, null, null);

        return createListFromCursor(query);
    }

    private ContentValues createContentValues(final String categoryName) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_CATEGORY_NAME, categoryName);

        LOG.debug("Content created: " + values);

        return values;
    }

    private List<Category> createListFromCursor(final Cursor cursor) {
        final List<Category> result = new ArrayList<Category>();

        while (cursor.moveToNext()) {
            final String categoryId = cursor.getString(cursor.getColumnIndexOrThrow(_ID));
            final String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_CATEGORY_NAME));

            final Category category = Category.builder(categoryName).categoryId(categoryId).build();
            result.add(category);
        }
        return result;
    }
}
