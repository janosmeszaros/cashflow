package com.cashflow.dao.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cashflow.dao.objects.CategoryEntity;
import com.cashflow.domain.Category;

public class JPABasedCategoryDAOTest {
    private static final String CATEGORY_NAME = "category";
    private static final long CATEGORY_ID = 1L;
    private static final long FAILED = -1L;
    private static final String ID_STR = "0";
    @Mock
    private GenericHibernateDAO<CategoryEntity> dao;
    private final Mapper mapper = new DozerBeanMapper();
    private final Category category = Category.builder(CATEGORY_NAME).categoryId(ID_STR).build();
    private final CategoryEntity categoryEntity = createCategoryEntity(category);

    private JPABasedCategoryDAO underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        underTest = new JPABasedCategoryDAO(mapper, dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenMapperIsNullThenShouldThrowException() {
        new JPABasedCategoryDAO(null, dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenDAOIsNullThenShouldThrowException() {
        new JPABasedCategoryDAO(mapper, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothArgsAreNullThenShouldThrowException() {
        new JPABasedCategoryDAO(null, null);
    }

    @Test
    public void testSaveWhenCalledThenReturnTrueIfWasSuccess() {
        final CategoryEntity savedCategory = new CategoryEntity();
        savedCategory.setCategoryId(CATEGORY_ID);
        when(dao.persist(categoryEntity)).thenReturn(savedCategory);

        final long savedCategoryId = underTest.save(category);

        verify(dao).persist(categoryEntity);
        assertThat(savedCategoryId, equalTo(CATEGORY_ID));
    }

    @Test
    public void testSaveWhenCalledThenReturnFalseIfWasUnsuccess() {
        final CategoryEntity savedCategory = new CategoryEntity();
        savedCategory.setCategoryId(FAILED);
        when(dao.persist(categoryEntity)).thenReturn(savedCategory);

        final long savedCategoryId = underTest.save(category);

        verify(dao).persist(categoryEntity);
        assertThat(savedCategoryId, equalTo(FAILED));
    }

    @Test
    public void testUpdateWhenCalledThenReturnFalseIfWasUnsuccess() {
        when(dao.merge(categoryEntity)).thenReturn(false);

        final boolean isUpdated = underTest.update(category, ID_STR);

        verify(dao).merge(categoryEntity);
        assertThat(isUpdated, equalTo(false));
    }

    @Test
    public void testSavUpdateWhenCalledThenReturnTrueIfWasSuccess() {
        when(dao.merge(categoryEntity)).thenReturn(true);

        final boolean isUpdated = underTest.update(category, ID_STR);

        verify(dao).merge(categoryEntity);
        assertThat(isUpdated, equalTo(true));
    }

    @Test
    public void testGetAllBillsWhenCalledShouldReturnListOfBills() {
        final List<CategoryEntity> list = new ArrayList<CategoryEntity>();
        list.add(categoryEntity);
        when(dao.findByCriteria()).thenReturn(list);

        final List<Category> categories = underTest.getAllCategories();

        verify(dao).findByCriteria();
        assertThat(categories, contains(category));
    }

    @Test
    public void testGetCategoryByIdWhenCalledThenShouldReturnACategory() {
        when(dao.findById(Long.valueOf(ID_STR))).thenReturn(categoryEntity);

        final Category returnedCategory = underTest.getCategoryById(ID_STR);

        assertThat(returnedCategory, equalTo(category));
    }

    private CategoryEntity createCategoryEntity(final Category category) {
        final CategoryEntity entity = new CategoryEntity();
        entity.setCategoryId(Long.valueOf(category.getCategoryId()));
        entity.setName(category.getName());

        return entity;
    }
}
