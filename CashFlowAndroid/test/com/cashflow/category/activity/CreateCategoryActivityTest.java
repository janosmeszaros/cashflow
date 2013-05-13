package com.cashflow.category.activity;

import static android.app.Activity.RESULT_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.widget.Button;
import android.widget.EditText;

import com.cashflow.R;
import com.cashflow.activity.testutil.ActivityModule;
import com.cashflow.activity.testutil.CreateCategoryActivityProvider;
import com.cashflow.activity.testutil.TestGuiceModule;
import com.cashflow.category.activity.CreateCategoryActivity.SubmitButtonOnClick;
import com.cashflow.category.database.AndroidCategoryDAO;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Category;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowButton;
import com.xtremelabs.robolectric.shadows.ShadowTextView;
import com.xtremelabs.robolectric.shadows.ShadowToast;

/**
 * {@link CreateCategoryActivity} test.
 * @author Janos_Gyula_Meszaros
 *
 */
@RunWith(RobolectricTestRunner.class)
public class CreateCategoryActivityTest {
    private static final String CATEGORY_NAME = "category name";
    private static final Category CATEGORY = Category.builder(CATEGORY_NAME).build();

    private CreateCategoryActivity underTest;

    @Mock
    private AndroidCategoryDAO categoryDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityModule module = new ActivityModule(new CreateCategoryActivityProvider());

        module.addBinding(CategoryDAO.class, categoryDAO);
        ActivityModule.setUp(this, module);
        underTest = new CreateCategoryActivity();
        underTest.onCreate(null);

    }

    @After
    public void tearDown() {
        TestGuiceModule.tearDown();
    }

    @Test
    public void testOnCreateWhenCalledThenShouldSetSubmitButtonOnClickListenerToSubmitButtonOnClickListener() {
        final Button submit = (Button) underTest.findViewById(R.id.createCategoryButton);
        final ShadowTextView shadowButton = Robolectric.shadowOf(submit);

        underTest.onCreate(null);

        assertThat(shadowButton.getOnClickListener(), instanceOf(SubmitButtonOnClick.class));
    }

    @Test
    public void testOnClickWhenCreateCategoryIsSuccessfulThenShouldSaveCategoryAndCloseActivity() {
        final ShadowActivity shadowActivity = Robolectric.shadowOf(underTest);
        final EditText categoryName = (EditText) underTest.findViewById(R.id.categoryNameText);
        final Button submit = (Button) underTest.findViewById(R.id.createCategoryButton);
        final ShadowButton submitShadow = (ShadowButton) Robolectric.shadowOf(submit);
        categoryName.setText(CATEGORY_NAME);
        when(categoryDAO.save(CATEGORY)).thenReturn(1L);

        submitShadow.performClick();

        verify(categoryDAO).save(CATEGORY);
        assertThat(underTest.isFinishing(), equalTo(true));
        assertThat(shadowActivity.getResultCode(), equalTo(RESULT_OK));
    }

    @Test
    public void testOnClickWhenCreateCategoryIsUnsuccessfulThenShouldShowToast() {
        final EditText categoryName = (EditText) underTest.findViewById(R.id.categoryNameText);
        final Button submit = (Button) underTest.findViewById(R.id.createCategoryButton);
        final ShadowButton submitShadow = (ShadowButton) Robolectric.shadowOf(submit);
        categoryName.setText(CATEGORY_NAME);
        when(categoryDAO.save(CATEGORY)).thenReturn(-1L);

        submitShadow.performClick();

        verify(categoryDAO).save(CATEGORY);
        assertThat(underTest.isFinishing(), equalTo(false));
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(underTest.getString(R.string.database_error)));
    }

    @Test
    public void testOnClickWhenNameIsEmptyThenShouldShowToast() {
        final Button submit = (Button) underTest.findViewById(R.id.createCategoryButton);
        final ShadowButton submitShadow = (ShadowButton) Robolectric.shadowOf(submit);

        submitShadow.performClick();

        assertThat(underTest.isFinishing(), equalTo(false));
        assertThat(ShadowToast.shownToastCount(), equalTo(1));
    }

}
