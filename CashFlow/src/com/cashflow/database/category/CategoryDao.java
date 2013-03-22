package com.cashflow.database.category;

import com.cashflow.database.DatabaseContracts.AbstractCategory;
import com.cashflow.database.ParentDao;
import com.cashflow.database.SQLiteDbProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic dao for the categories.
 * @author Kornel_Refi
 */
@Singleton
public class CategoryDao extends ParentDao {
    /**
     * Default constructor which get an Provider.
     * @param provider
     *            Provider to get database.
     */
    @Inject
    public CategoryDao(SQLiteDbProvider provider) {
        super(provider, AbstractCategory.class);
    }

        }
