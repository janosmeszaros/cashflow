package com.cashflow.dao.objects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cashflow.domain.RecurringInterval;

/**
 * Entity class for bills.
 * @author Janos_Gyula_Meszaros
 */
@Entity
public class BillEntity {
    @Id
    @GeneratedValue
    private String billId;
    private String amount;
    private Date date;
    private Date payedDate;
    private Date deadlineDate;
    private String note;
    @ManyToOne
    private CategoryEntity category;
    private boolean payed;
    @Enumerated(EnumType.STRING)
    private RecurringInterval interval;

    public void setBillId(final String billId) {
        this.billId = billId;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setPayedDate(final Date payedDate) {
        this.payedDate = payedDate;
    }

    public void setDeadlineDate(final Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public void setCategory(final CategoryEntity category) {
        this.category = category;
    }

    public void setPayed(final boolean payed) {
        this.payed = payed;
    }

    public void setInterval(final RecurringInterval interval) {
        this.interval = interval;
    }

    public String getBillId() {
        return billId;
    }

    public String getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Date getPayedDate() {
        return payedDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public String getNote() {
        return note;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public boolean isPayed() {
        return payed;
    }

    public RecurringInterval getInterval() {
        return interval;
    }

}
