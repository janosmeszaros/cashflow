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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.DatabaseContracts.AbstractStatement;
import com.cashflow.database.SQLiteDbProvider;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DaoParentTest {
    private static final String ID_STRING = "1";
    private static final String EQUALS = " = ?";

    private ParentDAO underTest;
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

        underTest = new AndroidParentDAO(provider, new AbstractStatement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenProviderIsNullThenShouldThrowException() {
        underTest = new AndroidParentDAO(null, new AbstractStatement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenClazzIsNullThenShouldThrowException() {
        underTest = new AndroidParentDAO(provider, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsContainsWrongColumnNamesThenShouldThrowException() {
        underTest.save(values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenContentValuesNotConsistentWithDatabaseThenShouldThrowException() {
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
    public void testSaveWhenSomethingWrongWithDatabaseThenShouldReturnFalse() {
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
        underTest.update(null, ID_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIsContainsWrongColumnNamesThenShouldThrowException() {
        underTest.update(values, ID_STRING);
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
