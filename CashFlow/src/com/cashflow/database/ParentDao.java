package com.cashflow.database;

import static android.provider.BaseColumns._ID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.cashflow.exceptions.IllegalTableException;

/**
 * a
 * @author Janos_Gyula_Meszaros
 */
public class ParentDao {
    private static final String EQUALS = " = ?";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final Logger LOG = LoggerFactory.getLogger(ParentDao.class);
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

    public ParentDao(SQLiteDbProvider provider, Class<? extends Tables> clazz) {
        nullCheck(provider);
        this.provider = provider;

        setFields(clazz);
    }

    /**
     * Persists values to the database.
     * @param values Values to save. Can not be <code>null</code>. Have to be consistent with the table columns.
     * @throws IllegalArgumentException when <code>values</code> is <code>null</code>.
     * @return <code>true</code> if save was successful, <code>false</code> otherwise.
     */
    public boolean save(ContentValues values) {
        validateContentValues(values);

        boolean result = false;

        long newRowId = provider.getWritableDb().insert(table, null, values);

        if (newRowId >= 0) {
            result = true;
            LOG.debug("New row created with row ID: " + newRowId);
        }

        return result;
    }

    /**
     * Updates a row with specified id.
     * @param values data needs to be updated. Can't be <code>null</code>. Have to be consistent with the table columns.
     * @param id row id. Can't be <code>null</code> or empty.
     * @throws IllegalArgumentException if <code>id</code> is empty or <code>null</code>.  Or <code>values</code> is <code>null</code> or do not contains proper column names. 
     * @throws
     * @return <code>true</code> if one or more records updated, otherwise <code>false</code>
     */
    public boolean update(ContentValues values, String id) {
        validateUpdateParams(values, id);

        boolean result = false;
        int update = provider.getWritableDb().update(table, values, _ID + EQUALS, new String[]{id});

        if (update > 0) {
            result = true;
        }

        LOG.debug("Num of rows updated: " + update);
        return result;
    }

    /**
     * Returns all of the values in the given table.
     * @return Cursor which contains the data.
     */
    public Cursor getValues() {
        SQLiteDatabase db = provider.getReadableDb();
        Cursor cursor = db.query(table, projection, null, null, null, null, null);
        return cursor;
    }

    private void setFields(Class<? extends Tables> clazz) {
        setTableName(clazz);
        setProjection(clazz);
        setColumnNames(clazz);
    }

    private void setProjection(Class<? extends Tables> clazz) {
        try {
            Field field = clazz.getField("PROJECTION");
            projection = (String[]) field.get(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        } catch (IllegalAccessException ex) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        }
    }

    @SuppressWarnings("unchecked")
    private void setColumnNames(Class<? extends Tables> clazz) {
        Field[] fields = clazz.getFields();
        List<Field> list = new ArrayList<Field>(Arrays.asList(fields));
        CollectionUtils.filter(list, new ColumnPredicate());
        columnNames = new TreeSet<String>(CollectionUtils.collect(list, new FieldToStringTransformer()));
    }

    private void setTableName(Class<? extends Tables> clazz) {
        try {
            Field field = clazz.getField(TABLE_NAME);
            table = (String) field.get(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        } catch (IllegalAccessException ex) {
            throw new IllegalTableException(clazz.getName(), TABLE_NAME);
        }
    }

    private void validateUpdateParams(ContentValues values, String id) {
        validateContentValues(values);
        idCheck(id);
    }

    @SuppressLint("NewApi")
    private void validateContentValues(ContentValues values) {
        nullCheck(values);
        checkColumns(values.keySet());
    }

    private void nullCheck(Object object) {
        Validate.notNull(object);
    }

    private void checkColumns(Set<String> names) {
        if (!names.equals(columnNames)) {
            throw new IllegalArgumentException("Wrong column names!");
        }
    }

    private void idCheck(String id) {
        Validate.notEmpty(id);
    }
}
