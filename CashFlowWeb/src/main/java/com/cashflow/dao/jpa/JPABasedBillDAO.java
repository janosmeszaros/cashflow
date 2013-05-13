package com.cashflow.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cashflow.dao.BillDAO;
import com.cashflow.dao.objects.BillEntity;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;

/**
 * Hibernate based implementation for bill DAO interface.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedBillDAO implements BillDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPABasedCategoryDAO.class);
    private final Mapper mapper;
    private final GenericHibernateDAO<BillEntity> dao;

    /**
     * Constructor
     * @param mapper
     *            mapper
     * @param dao
     *            dao
     */
    @Autowired
    public JPABasedBillDAO(final Mapper mapper, @Qualifier("billGenericDAO") final GenericHibernateDAO<BillEntity> dao) {
        Validate.notNull(mapper);
        Validate.notNull(dao);

        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public long save(final Bill bill) {
        final BillEntity billEntity = generateBillEntity(bill);
        long newBillId;
        try {
            final BillEntity persistedBill = dao.persist(billEntity);
            newBillId = persistedBill.getBillId();
        } catch (final HibernateException e) {
            LOGGER.error("An exception occured during the operations. Exception message: " + e.getMessage());
            newBillId = -1;
        }
        return newBillId;
    }

    private BillEntity generateBillEntity(final Bill bill) {
        return mapper.map(bill, BillEntity.class);
    }

    @Override
    public boolean update(final Bill bill, final String billId) {
        final BillEntity billEntity = generateBillEntity(bill);
        return dao.merge(billEntity);
    }

    @Override
    public List<Bill> getAllBills() {
        final List<BillEntity> entityList = dao.findByCriteria();
        return convertToBillList(entityList);
    }

    private List<Bill> convertToBillList(final List<BillEntity> entityList) {
        final List<Bill> billList = new ArrayList<Bill>();

        for (final BillEntity entity : entityList) {
            final Bill bill = convertToBill(entity);
            billList.add(bill);
        }

        return billList;
    }

    private Bill convertToBill(final BillEntity entity) {
        final Category category = Category.builder(entity.getCategory().getName()).categoryId(String.valueOf(entity.getCategory().getCategoryId()))
                .build();

        return Bill.builder(entity.getAmount(), entity.getDate(), entity.getDeadlineDate()).billId(String.valueOf(entity.getBillId()))
                .category(category).interval(entity.getRecurringInterval()).isPayed(entity.isPayed()).note(entity.getNote())
                .payedDate(entity.getPayedDate()).build();

    }

    @Override
    public Bill getBillById(final String billId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(final Bill bill) {
        // TODO Auto-generated method stub

    }
}
