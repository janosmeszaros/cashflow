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
        final ContentValues values = createContentValues(category);
        return persistCategory(values);
    }

    private boolean persistCategory(final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final long newRowId = database.insert(TABLE_NAME, null, values);

        LOG.debug("New row created with row ID: " + newRowId);

        return isSuccessful(newRowId);
    }

    private boolean isSuccessful(final long newRowId) {
        return newRowId >= 0;
    }

    @Override
    public boolean update(final Category category, final String categoryId) {
        Validate.notNull(category);
        Validate.notEmpty(categoryId);

        final ContentValues values = createContentValues(category);
        return persistUpdate(categoryId, values);
    }

    private boolean persistUpdate(final String categoryId, final ContentValues values) {
        final SQLiteDatabase database = provider.getWritableDb();
        final int update = database.update(TABLE_NAME, values, _ID + EQUALS, new String[] { categoryId });
        LOG.debug("Num of rows updated: " + update);
        return isUpdateSuccessed(update);
    }

    private boolean isUpdateSuccessed(final int update) {
        return update > 0;
    }

    @Override
    public List<Category> getAllCategories() {
        final Cursor query = queryAllCategories();
        return createListFromCursor(query);
    }

    private Cursor queryAllCategories() {
        final SQLiteDatabase database = provider.getReadableDb();
        final Cursor query = database.query(TABLE_NAME, PROJECTION, null, null, null, null, null);
        return query;
    }

    private ContentValues createContentValues(final Category category) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_CATEGORY_NAME, category.getName());

        LOG.debug("Content created: " + values);

        return values;
    }

    private List<Category> createListFromCursor(final Cursor cursor) {
        final List<Category> result = new ArrayList<Category>();

        final int idIndex = cursor.getColumnIndexOrThrow(_ID);
        final int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CATEGORY_NAME);

        while (cursor.moveToNext()) {
            final String categoryId = cursor.getString(idIndex);
            final String categoryName = cursor.getString(nameIndex);

            final Category category = Category.builder(categoryName).categoryId(categoryId).build();
            result.add(category);
        }
        return result;
    }
}
