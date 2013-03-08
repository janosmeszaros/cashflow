package com.cashflow.activity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.widget.EditText;

import com.cashflow.R;
import com.cashflow.activity.util.ActivityModule;
import com.cashflow.activity.util.CreateCategoryActivityProvider;
import com.cashflow.activity.util.TestGuiceModule;
import com.cashflow.database.category.CategoryPersistenceService;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link CreateCategoryActivity} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class CreateCategoryActivityTest {
    private static final String EMPTY_STRING = "";

    private static final String CATEGORY_NAME = "category name";

    private CreateCategoryActivity underTest;

    @Mock
    private CategoryPersistenceService categoryPersistenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityModule module = new ActivityModule(new CreateCategoryActivityProvider());

        module.addBinding(CategoryPersistenceService.class, categoryPersistenceService);
        ActivityModule.setUp(this, module);
        underTest = new CreateCategoryActivity();
        underTest.onCreate(null);

    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test
    public void testCreateCategoryIsSuccessful() {
        EditText categoryName = (EditText) underTest.findViewById(R.id.categoryNameText);
        categoryName.setText(CATEGORY_NAME);

        when(categoryPersistenceService.saveCategory(CATEGORY_NAME)).thenReturn(true);
        underTest.createCategory(null);

        verify(categoryPersistenceService).saveCategory(CATEGORY_NAME);
        assertThat(underTest.isFinishing(), equalTo(true));
    }

    @Test
    public void testCreateCategoryIsNotSuccessful() {
        EditText categoryName = (EditText) underTest.findViewById(R.id.categoryNameText);
        categoryName.setText(EMPTY_STRING);

        when(categoryPersistenceService.saveCategory(EMPTY_STRING)).thenReturn(false);

        underTest.createCategory(null);

        verify(categoryPersistenceService).saveCategory(EMPTY_STRING);
        assertThat(underTest.isFinishing(), equalTo(false));
    }

}
