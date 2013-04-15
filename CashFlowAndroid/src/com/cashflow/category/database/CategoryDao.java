package com.cashflow.category.database;

import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.database.parentdao.DaoParent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the categories.
 * @author Kornel_Refi
 */
@Singleton
public class CategoryDao extends DaoParent {
    /**
     * Default constructor which get an Provider.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public CategoryDao(final SQLiteDbProvider provider) {
        super(provider, new AbstractCategory());
    }

}
