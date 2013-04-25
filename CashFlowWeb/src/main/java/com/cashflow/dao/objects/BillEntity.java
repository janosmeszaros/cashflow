package com.cashflow.dao.objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cashflow.domain.RecurringInterval;

/**
 * Entity class for bills.
 * @author Janos_Gyula_Meszaros
 */
@Entity
@Table(name = "bill")
public class BillEntity {
    @Id
    @GeneratedValue
    private long billId;
    private String amount;
    private String date;
    private String payedDate;
    private String deadlineDate;
    private String note;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private CategoryEntity category;
    private boolean payed;
    @Enumerated(EnumType.STRING)
    private RecurringInterval interval;

    public void setBillId(final long billId) {
        this.billId = billId;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setPayedDate(final String payedDate) {
        this.payedDate = payedDate;
    }

    public void setDeadlineDate(final String deadlineDate) {
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

    public long getBillId() {
        return billId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getPayedDate() {
        return payedDate;
    }

    public String getDeadlineDate() {
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
