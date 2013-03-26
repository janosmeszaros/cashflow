package com.cashflow.category.database;

import static android.provider.BaseColumns._ID;
import static com.cashflow.database.DatabaseContracts.AbstractCategory.COLUMN_NAME_CATEGORY_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.cashflow.domain.Category;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link CategoryPersistenceService} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class CategoryPersistenceServiceTest {

    private static final String CATEGORY_NAME = "CategoryName";
    private static final String ID_STR = "0";
    private static final String EMPTY_STR = "";
    private CategoryPersistenceService underTest;
    @Mock
    private CategoryDao dao;
    @Mock
    private Cursor cursor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        underTest = new CategoryPersistenceService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveCategoryWhenNameIsNullThenShouldThrowException() {
        underTest = new CategoryPersistenceService(dao);

        underTest.saveCategory(null);
    }

    @Test
    public void testSaveCategoryWhenNameIsEmptyStringThenShouldReturnFalse() {
        underTest = new CategoryPersistenceService(dao);

        boolean saveWasSuccessful = underTest.saveCategory(EMPTY_STR);

        assertThat(saveWasSuccessful, equalTo(false));
    }

    @Test
    public void testSaveCategoryWhenEveryParameterIsFineThenShouldCallDaosSaveMethodAndReturnTrue() {
        underTest = new CategoryPersistenceService(dao);

        boolean saveWasSuccessful = underTest.saveCategory(CATEGORY_NAME);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).save(argument.capture());

        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY_NAME), equalTo(CATEGORY_NAME));

        assertThat(saveWasSuccessful, equalTo(true));
    }

    @Test
    public void testGetCategoriesShouldReturnCategoryList() {
        Category category = new Category(ID_STR, CATEGORY_NAME);
        List<Category> testList = new ArrayList<Category>();
        testList.add(category);
        MatrixCursor cursor = new MatrixCursor(new String[]{_ID, COLUMN_NAME_CATEGORY_NAME});
        cursor.addRow(new String[]{category.getId(), category.getName()});
        when(dao.getValues()).thenReturn(cursor);
        underTest = new CategoryPersistenceService(dao);

        List<Category> result = underTest.getCategories();

        verify(dao).getValues();
        assertThat(testList, equalTo(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCategorieWhenNameStrIsNullThenShouldThrowException() {
        underTest = new CategoryPersistenceService(dao);

        underTest.updateCategory(ID_STR, null);
    }

    @Test
    public void testUpdateCategoryWhenNameIsFineThenShouldCallDaosUpdateMethodAndReturnTrue() {
        underTest = new CategoryPersistenceService(dao);
        when(dao.update(Mockito.any(ContentValues.class), Mockito.eq(ID_STR))).thenReturn(true);

        boolean updateWasSuccesful = underTest.updateCategory(ID_STR, CATEGORY_NAME);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);
        verify(dao).update(argument.capture(), eq(ID_STR));
        assertThat((String) argument.getValue().get(COLUMN_NAME_CATEGORY_NAME), equalTo(CATEGORY_NAME));
        assertThat(updateWasSuccesful, equalTo(true));
    }
}
