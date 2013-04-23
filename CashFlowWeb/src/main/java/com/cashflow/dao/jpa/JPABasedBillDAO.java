package com.cashflow.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cashflow.dao.BillDAO;
import com.cashflow.dao.objects.BillEntity;
import com.cashflow.domain.Bill;

/**
 * Hibernate based implementation for bill dao interface.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class JPABasedBillDAO implements BillDAO {
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
        super();
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public boolean save(final Bill bill) {
        final BillEntity billEntity = generateBillEntity(bill);
        return dao.persist(billEntity);
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
        return mapper.map(entity, Bill.class);
    }
}
