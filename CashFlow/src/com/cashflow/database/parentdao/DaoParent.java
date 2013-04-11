package com.cashflow.database.parentdao;

import static android.provider.BaseColumns._ID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.exceptions.IllegalTableException;

/**
 * Parent class for all dao. It contains the basic functions, such as insert, update and getValues;
 * @author Janos_Gyula_Meszaros
 */
public class DaoParent {
    private static final String EQUALS = " = ?";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final Logger LOG = LoggerFactory.getLogger(DaoParent.class);
    private final SQLiteDbProvider provider;
    private String table;
    private String[] projection;
    private Set<String> columnNames;

    /**
     * Default constructor which gets a Provider.
     * @param provider Provider to get database. Can't be <code>null</code>.
     * @param clazz table. Can't be <code>null</code>.
     * @throws IllegalArgumentException when <code>provider</code> or <code>table</code> is <code>null</code>.
     */

    public DaoParent(final SQLiteDbProvider provider, final Class<? extends Tables> clazz) {
        nullCheck(provider);
        nullCheck(clazz);
        this.provider = provider;

        setFields(clazz);
    }

    /**
     * Persists values to the database.
     * @param values Values to save. Can not be <code>null</code>. Have to be consistent with the table columns.
     * @throws IllegalArgumentException when <code>values</code> is <code>null</code>.  Or <code>values</code> is <code>null</code> or do not contains proper column names.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(final ContentValues values) {
        validateContentValues(values);

        boolean isSuccessful = false;

        final long newRowId = provider.getWritableDb().insert(table, null, values);

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
    public boolean update(final ContentValues values, final String id) {
        validateUpdateParams(values, id);

        boolean isSuccessful = false;
        final int update = provider.getWritableDb().update(table, values, _ID + EQUALS, new String[]{id});

        if (update > 0) {
            isSuccessful = true;
        }

        LOG.debug("Num of rows updated: " + update);
        return isSuccessful;
    }

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    public Cursor getValues() {
        final SQLiteDatabase database = provider.getReadableDb();
        return database.query(table, projection, null, null, null, null, null);
    }

    private void setFields(final Class<? extends Tables> clazz) {
        setTableName(clazz);
        setProjection(clazz);
        setColumnNames(clazz);
    }

    private void setProjection(final Class<? extends Tables> clazz) {
        try {
            final Field field = clazz.getField("PROJECTION");
            projection = (String[]) field.get(null);
        } catch (final NoSuchFieldException e) {
            throw new IllegalTableException(clazz.getName(), "PROJECTION");
        } catch (final IllegalAccessException ex) {
            throw new IllegalTableException(clazz.getName(), "PROJECTION");
        }
    }

    private void setTableName(final Class<? extends Tables> clazz) {
        try {
            final Field field = clazz.getField(TABLE_NAME);
            table = (String) field.get(null);
        } catch (final NoSuchFieldException e) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        } catch (final IllegalAccessException ex) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        }
    }

    @SuppressWarnings("unchecked")
    private void setColumnNames(final Class<? extends Tables> clazz) {
        final Field[] fields = clazz.getFields();
        final List<Field> listOfFields = new ArrayList<Field>(Arrays.asList(fields));
        CollectionUtils.filter(listOfFields, new ColumnPredicate());
        final Collection<String> stringListOfFields = CollectionUtils.collect(listOfFields, new FieldToStringTransformer());
        columnNames = new TreeSet<String>(stringListOfFields);
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
