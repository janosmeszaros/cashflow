package com.cashflow.category.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.TABLE_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.database.SQLiteDbProvider;
import com.cashflow.domain.Category;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AndroidCategoryDAO} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class AndroidCategoryDAOTest {
    private static final String CATEGORY_ID = "0";
    private static final String CATEGORY_NAME = "category";
    private static final String EQUALS = " = ?";
    private static final Category CATEGORY = Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build();

    private CategoryDAO underTest;
    @Mock
    private SQLiteDbProvider provider;
    @Mock
    private SQLiteDatabase database;
    @Mock
    private Cursor cursorMock;
    @Mock
    private ContentValues values;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(provider.getWritableDb()).thenReturn(database);
        when(provider.getReadableDb()).thenReturn(database);

        underTest = new AndroidCategoryDAO(provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        new AndroidCategoryDAO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenParamIsNullThenShouldThrowException() {
        underTest.save(null);
    }

    @Test
    public void testSaveWhenCategoryIsOkThenShouldSaveToDatabase() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(1L);

        final boolean saved = underTest.save(CATEGORY);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(true));
    }

    @Test
    public void testSaveWhenCategoryIsOkButSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.insert(eq(TABLE_NAME), (String) eq(null), (ContentValues) anyObject())).thenReturn(-1L);

        final boolean saved = underTest.save(CATEGORY);

        verify(provider).getWritableDb();
        assertThat(saved, equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenParamIsNullThenShouldThrowException() {
        underTest.update(null, "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenIdIsEmptyThenShouldThrowException() {
        underTest.update(CATEGORY, "");
    }

    @Test
    public void testUpdateWhenCategoryIsOkThenShouldUpdateAndReturnTrue() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{CATEGORY.getId()}))).thenReturn(1);

        final boolean updated = underTest.update(CATEGORY, CATEGORY.getId());

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(true));
    }

    @Test
    public void testUpdateWhenSomethingHappensInInsertionThenShouldReturnFalse() {
        when(database.update(eq(TABLE_NAME), (ContentValues) anyObject(), eq(_ID + EQUALS), eq(new String[]{CATEGORY.getId()}))).thenReturn(-1);

        final boolean updated = underTest.update(CATEGORY, CATEGORY.getId());

        verify(provider).getWritableDb();
        assertThat(updated, equalTo(false));
    }

    @Test
    public void testGetAllCategoriesWhenCalledThenReturnListOfCategories() {
        when(database.query(TABLE_NAME, PROJECTION, null, null, null, null, null)).thenReturn(cursorMock);
        setupCursorMock();
        final List<Category> list = new ArrayList<Category>();
        list.add(CATEGORY);

        final List<Category> categories = underTest.getAllCategories();

        verify(provider).getReadableDb();
        verify(database).query(TABLE_NAME, PROJECTION, null, null, null, null, null);
        assertThat(list, equalTo(categories));
    }

    private void setupCursorMock() {
        when(cursorMock.moveToNext()).thenReturn(true, false);

        when(cursorMock.getColumnIndexOrThrow(_ID)).thenReturn(0);
        when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME_CATEGORY_NAME)).thenReturn(1);

        when(cursorMock.getString(0)).thenReturn(CATEGORY_ID);
        when(cursorMock.getString(1)).thenReturn(CATEGORY_NAME);
    }

}
