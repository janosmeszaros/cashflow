package com.cashflow.category.database;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.AndroidParentDAO;
import com.cashflow.domain.Category;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the categories.
 * @author Kornel_Refi
 */
@Singleton
public class AndroidCategoryDAO extends AndroidParentDAO implements CategoryDAO {
    /**
     * Default constructor which get an Provider.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public AndroidCategoryDAO(final SQLiteDbProvider provider) {
        super(provider, new AbstractCategory());
    }

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    @Override
    public List<Category> getAllCategories() {
        final SQLiteDatabase database = provider.getReadableDb();
        return database.query(tableName, projection, null, null, null, null, null);
    }
}
