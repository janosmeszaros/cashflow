package com.cashflow.database.parentdao;

import static android.provider.BaseColumns._ID;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import com.cashflow.dao.ParentDAO;
import com.cashflow.database.SQLiteDbProvider;

/**
 * Parent class for all dao. It contains the basic functions, such as insert, update and getValues;
 * @author Janos_Gyula_Meszaros
 */
public class AndroidParentDAO implements ParentDAO {
    private static final String EQUALS = " = ?";
    private static final Logger LOG = LoggerFactory.getLogger(AndroidParentDAO.class);
    private final SQLiteDbProvider provider;
    private String tableName;
    private String[] projection;
    private Set<String> columnNames;

    /**
     * Default constructor which gets a Provider.
     * @param provider Provider to get database. Can't be <code>null</code>.
     * @param table table. Can't be <code>null</code>.
     * @throws IllegalArgumentException when <code>provider</code> or <code>table</code> is <code>null</code>.
     */
    public AndroidParentDAO(final SQLiteDbProvider provider, final Tables table) {
        nullCheck(provider);
        nullCheck(table);
        this.provider = provider;

        setFields(table);
    }

    /**
     * Persists values to the database.
     * @param values Values to save. Can not be <code>null</code>. Have to be consistent with the table columns.
     * @throws IllegalArgumentException when <code>values</code> is <code>null</code>.  Or <code>values</code> is <code>null</code> or do not contains proper column names.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    @Override
    public boolean save(final ContentValues values) {
        validateContentValues(values);

        boolean isSuccessful = false;

        final long newRowId = provider.getWritableDb().insert(tableName, null, values);

        if (newRowId >= 0) {
            isSuccessful = true;
            LOG.debug("New row created with row ID: " + newRowId);
        }

        return isSuccessful;
    }

    /**
     * Updates a row with specified id.
     * @param values data needs to be updated. Can't be <code>null</code>. Have to be consistent with the table columns.
     * @param id row id. Can't be <code>null</code> or empty.
     * @throws IllegalArgumentException if <code>id</code> is empty or <code>null</code>.  Or <code>values</code> is <code>null</code> or do not contains proper column names. 
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    @Override
    public boolean update(final ContentValues values, final String id) {
        validateUpdateParams(values, id);

        boolean isSuccessful = false;
        final int update = provider.getWritableDb().update(tableName, values, _ID + EQUALS, new String[]{id});

        if (update > 0) {
            isSuccessful = true;
        }

        LOG.debug("Num of rows updated: " + update);
        return isSuccessful;
    }

    private void setFields(final Tables table) {
        tableName = table.getTableName();
        projection = table.getProjection();
        columnNames = table.getColumns();
    }

    private void validateUpdateParams(final ContentValues values, final String id) {
        validateContentValues(values);
        idCheck(id);
    }

    @SuppressLint("NewApi")
    private void validateContentValues(final ContentValues values) {
        nullCheck(values);
        checkColumns(values.keySet());
    }

    private void nullCheck(final Object object) {
        Validate.notNull(object);
    }

    private void checkColumns(final Set<String> names) {
        if (!columnNames.equals(names)) {
            throw new IllegalArgumentException("Wrong column names!");
        }
    }

    private void idCheck(final String id) {
        Validate.notEmpty(id);
    }
}
