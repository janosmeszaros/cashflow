package com.cashflow.database.parentdao;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.NULLABLE;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TABLE_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.exceptions.IllegalTableException;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DaoParentTest {
    private static final Logger LOG = LoggerFactory.getLogger(DaoParentTest.class);
    private static final String ID = "1";
    private static final String EQUALS = " = ?";

    private DaoParent underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase databaseMock;
    @Mock
    private ContentValues values;
    @Mock
    private Cursor cursorMock;
    @Mock
    private Tables table;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(databaseMock);
        when(provider.getReadableDb()).thenReturn(databaseMock);

        underTest = new DaoParent(provider, AbstractStatement.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenProviderIsNullThenShouldThrowException() {
        underTest = new DaoParent(null, AbstractStatement.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenClazzIsNullThenShouldThrowException() {
        underTest = new DaoParent(provider, null);
    }

    @Test(expected = IllegalTableException.class)
    public void testConstructorWhenPROJECTIONFieldIsNotExistsInClassThenShouldThrowException() {
        //        try {
        //            when(table.getClass().getField("PROJECTION")).thenThrow(new NoSuchFieldException());
        //        } catch (final NoSuchFieldException e) {
        //            LOG.debug("Exception handling!");
        //        }
        final Tables clazz = new Tables() {
            //            public static final String PROJECTION = "name";
            //            private static final String TABLE_NAME = "12";
        };

        underTest = new DaoParent(provider, clazz.getClass());
    }

    @Test(expected = IllegalTableException.class)
    public void testConstructorWhenTABLENAMEFieldIsNotExistsInClassThenShouldThrowException() {
        try {
            when(table.getClass().getField("TABLE_NAME")).thenThrow(new NoSuchFieldException());
        } catch (final NoSuchFieldException e) {
            LOG.debug("Exception handling2!");
        }

        underTest = new DaoParent(provider, table.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsContainsWrongColumnNamesThenShouldThrowException() {
        underTest.save(values);
    }

    @Test
    public void testSaveWhenEverythingIsOkThenCallProperFunctionAndReturnTrue() {
        when(databaseMock.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(1L);
        setKeySet(AbstractStatement.class);

        final boolean result = underTest.save(values);

        verify(provider).getWritableDb();
        verify(databaseMock).insert(TABLE_NAME, NULLABLE, values);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testSaveWhenSomethinWrongWithDatabaseThenShouldReturnFalse() {
        when(databaseMock.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(-1L);
        setKeySet(AbstractStatement.class);

        final boolean result = underTest.save(values);

        verify(provider).getWritableDb();
        verify(databaseMock).insert(TABLE_NAME, NULLABLE, values);
        assertThat(result, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIdIsEmptyThenThrowException() {
        setKeySet(AbstractStatement.class);
        underTest.update(values, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamValuesIsNullThenThrowException() {
        underTest.update(null, ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIsContainsWrongColumnNamesThenShouldThrowException() {
        underTest.update(values, ID);
    }

    @Test
    public void testUpdateWhenEverythingIsOkThenShouldCallProperFunctionAndReturnTrue() {
        setKeySet(AbstractStatement.class);
        final String id = "id";
        when(databaseMock.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(1);

        final boolean result = underTest.update(values, id);

        verify(provider).getWritableDb();
        verify(databaseMock).update(TABLE_NAME, values, _ID + EQUALS, new String[]{id});
        assertThat(result, equalTo(true));
    }

    @Test
    public void testUpdateWhenSomethinWrongWithDatabaseThenShouldReturnFalse() {
        setKeySet(AbstractStatement.class);
        final String id = "2";
        when(databaseMock.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(0);

        final boolean result = underTest.update(values, id);

        verify(provider).getWritableDb();
        verify(databaseMock).update(TABLE_NAME, values, _ID + EQUALS, new String[]{id});
        assertThat(result, equalTo(false));
    }

    @Test
    public void testGetValuesWhenEverythingIsOkThenCallProperFunctionAndReturnCursor() {
        when(databaseMock.query(TABLE_NAME, PROJECTION, null, null, null, null, null)).thenReturn(cursorMock);

        final Cursor cursor = underTest.getValues();

        verify(provider).getReadableDb();
        verify(databaseMock).query(TABLE_NAME, PROJECTION, null, null, null, null, null);
        assertThat(cursor, equalTo(cursorMock));
    }

    @SuppressWarnings("unchecked")
    private void setKeySet(final Class<? extends Tables> clazz) {
        final Field[] fields = clazz.getFields();
        final List<Field> list = new ArrayList<Field>(Arrays.asList(fields));
        CollectionUtils.filter(list, new ColumnPredicate());
        final Set<String> columnNames = new TreeSet<String>(CollectionUtils.collect(list, new FieldToStringTransformer()));

        when(values.keySet()).thenReturn(columnNames);
    }
}
