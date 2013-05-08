package com.cashflow.bill.database;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import com.cashflow.dao.BillDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Basic bill functionality.
 * @author Janos_Gyula_Meszaros
 */
@Singleton
public class BillService {
    @Inject
    private BillDAO billDAO;
    @Inject
    private StatementDAO statementDAO;

    /**
     * Pay the current bill. In parallel it's create a new expense for it.
     * @param billId
     *            {@link Bill}'s id.
     */
    public void payBill(final String billId) {
        final Bill originalBill = billDAO.getBillById(billId);
        final Bill payedBill = createPayedBill(originalBill);
        billDAO.update(payedBill, payedBill.getBillId());
        final Statement statement = createStatement(payedBill);
        statementDAO.save(statement);
    }

    private Statement createStatement(final Bill payedBill) {
        return Statement.builder(payedBill.getAmount(), payedBill.getPayedDate()).category(payedBill.getCategory())
                .note(payedBill.getNote())
                .type(StatementType.Expense).build();
    }

    private Bill createPayedBill(final Bill bill) {
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final Calendar myCalendar = Calendar.getInstance();

        final Bill newBill =
                Bill.builder(bill.getAmount(), bill.getDate(), bill.getDeadlineDate()).billId(bill.getBillId())
                        .category(bill.getCategory())
                        .interval(bill.getInterval()).isPayed(true).note(bill.getNote())
                        .payedDate(dateFormatter.format(myCalendar.getTime()))
                        .build();
        return newBill;
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    /**
     * Delete the bill which is belongs to id.
     * @param billId
     *            {@link Bill}'s id.
     */
    public void delete(final String billId) {
        final Bill billToDelete = billDAO.getBillById(billId);
        billDAO.delete(billToDelete);
    }
}
