package com.cashflow.database.category;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.NULLABLE;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.TABLE_NAME;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.database.SQLiteDbProvider;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link CategoryDao} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class CategoryDaoTest {

    private CategoryDao underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase db;
    @Mock
    private ContentValues values;

    private Set<String> columnNames = new TreeSet<String>(Arrays.asList(COLUMN_NAME_CATEGORY_NAME));

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(db);
        when(provider.getReadableDb()).thenReturn(db);
        when(values.keySet()).thenReturn(columnNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new CategoryDao(null);
    }

    @Test
    public void testSaveWhenEverythingIsOkThenCallProperFunction() {
        underTest = new CategoryDao(provider);

        underTest.save(values);

        verify(db, times(1)).insert(TABLE_NAME, NULLABLE, values);
    }

    @Test
    public void testUpdateWhenEverythingIsOkThenCallProperFunction() {
        underTest = new CategoryDao(provider);
        String id = "id";

        underTest.update(values, id);

        verify(db, times(1)).update(TABLE_NAME, values, _ID + " = ?", new String[]{id});
    }

}
