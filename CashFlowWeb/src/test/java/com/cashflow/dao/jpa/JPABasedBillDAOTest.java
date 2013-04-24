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

import com.cashflow.dao.objects.BillEntity;
import com.cashflow.dao.objects.CategoryEntity;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;

public class JPABasedBillDAOTest {
    private static final String ID_STR = "0";
    private static final String AMOUNT_STR = "1234";
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_NAME = "cat";
    private static final String NOTE = "note";
    private static final String DATE_STR = "2012.01.01";

    private final Bill bill = Bill.builder(AMOUNT_STR, DATE_STR, DATE_STR).billId(ID_STR)
            .category(Category.builder(CATEGORY_NAME).categoryId(CATEGORY_ID).build()).note(NOTE).isPayed(false).build();

    private final BillEntity billEntity = createBillEntity(bill);

    @Mock
    private GenericHibernateDAO<BillEntity> dao;
    private final Mapper mapper = new DozerBeanMapper();
    private JPABasedBillDAO underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        underTest = new JPABasedBillDAO(mapper, dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenMapperIsNullThenShouldThrowException() {
        new JPABasedBillDAO(null, dao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenDAOIsNullThenShouldThrowException() {
        new JPABasedBillDAO(mapper, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothArgsAreNullThenShouldThrowException() {
        new JPABasedBillDAO(null, null);
    }

    @Test
    public void testSaveWhenCalledThenReturnTrueIfWasSuccess() {
        when(dao.persist(billEntity)).thenReturn(true);

        final boolean isSaved = underTest.save(bill);

        verify(dao).persist(billEntity);
        assertThat(isSaved, equalTo(true));
    }

    @Test
    public void testSaveWhenCalledThenReturnFalseIfWasUnsuccess() {
        when(dao.persist(billEntity)).thenReturn(false);

        final boolean isSaved = underTest.save(bill);

        verify(dao).persist(billEntity);
        assertThat(isSaved, equalTo(false));
    }

    @Test
    public void testUpdateWhenCalledThenReturnFalseIfWasUnsuccess() {
        when(dao.merge(billEntity)).thenReturn(false);

        final boolean isUpdated = underTest.update(bill, ID_STR);

        verify(dao).merge(billEntity);
        assertThat(isUpdated, equalTo(false));
    }

    @Test
    public void testSavUpdateWhenCalledThenReturnTrueIfWasSuccess() {
        when(dao.merge(billEntity)).thenReturn(true);

        final boolean isUpdated = underTest.update(bill, ID_STR);

        verify(dao).merge(billEntity);
        assertThat(isUpdated, equalTo(true));
    }

    @Test
    public void testGetAllBillsWhenCalledShouldReturnListOfBills() {
        final List<BillEntity> list = new ArrayList<BillEntity>();
        list.add(billEntity);
        when(dao.findByCriteria()).thenReturn(list);

        final List<Bill> bills = underTest.getAllBills();

        verify(dao).findByCriteria();
        assertThat(bills, contains(bill));
    }

    private BillEntity createBillEntity(final Bill bill) {
        final Category category = bill.getCategory();
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setCategoryId(Integer.parseInt(category.getCategoryId()));

        final BillEntity entity = new BillEntity();
        entity.setAmount(bill.getAmount());
        entity.setCategory(categoryEntity);
        entity.setDate(bill.getDate());
        entity.setNote(bill.getNote());
        entity.setInterval(bill.getInterval());
        entity.setBillId(Integer.parseInt(bill.getBillId()));
        entity.setDeadlineDate(bill.getDeadlineDate());
        entity.setPayed(bill.isPayed());
        entity.setPayedDate(bill.getPayedDate());

        return entity;
    }
}
