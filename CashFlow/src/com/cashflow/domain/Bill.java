package com.cashflow.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cashflow.constants.RecurringInterval;

/**
 * Bill class.
 * @author Janos_Gyula_Meszaros
 *
 */
public class Bill {
    private final String amount;
    private final String date;
    private String payedDate;
    private final String deadlineDate;
    private String note;
    private Category category;
    private boolean payed;
    private RecurringInterval interval;

    /**
     * Create a Bill.
     * @param amount amount
     * @param date creation date
     * @param deadlineDate deadline date
     */
    public Bill(String amount, String date, String deadlineDate) {
        this.amount = amount;
        this.date = date;
        this.deadlineDate = deadlineDate;
    }

    public void setPayedDate(String payedDate) {
        this.payedDate = payedDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public void setInterval(RecurringInterval interval) {
        this.interval = interval;
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

    public Category getCategory() {
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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
