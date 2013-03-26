package com.cashflow.domain;

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
    public Bill(final String amount, final String date, final String deadlineDate) {
        this.amount = amount;
        this.date = date;
        this.deadlineDate = deadlineDate;
    }

    public void setPayedDate(final String payedDate) {
        this.payedDate = payedDate;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public void setPayed(final boolean payed) {
        this.payed = payed;
    }

    public void setInterval(final RecurringInterval interval) {
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

}
