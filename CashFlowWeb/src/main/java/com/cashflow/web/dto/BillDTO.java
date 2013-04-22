package com.cashflow.web.dto;

import com.cashflow.domain.Bill;
import com.cashflow.domain.RecurringInterval;

/**
 * DTO for bill.
 * @author Janos_Gyula_Meszaros
 */
public class BillDTO {
    private String billId;
    private String amount;
    private String date;
    private String payedDate;
    private String deadlineDate;
    private String note;
    private CategoryDTO category = new CategoryDTO();
    private boolean payed;
    private RecurringInterval interval;

    /**
     * Convert to {@link Bill}.
     * @return {@link Bill}.
     */
    public Bill convert() {
        return Bill.builder(amount, date, deadlineDate).billId(billId).category(category.convert()).interval(interval).isPayed(payed)
                .note(note).payedDate(payedDate).build();
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(final String billId) {
        this.billId = billId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(final String payedDate) {
        this.payedDate = payedDate;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(final String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(final CategoryDTO category) {
        this.category = category;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(final boolean payed) {
        this.payed = payed;
    }

    public RecurringInterval getInterval() {
        return interval;
    }

    public void setInterval(final RecurringInterval interval) {
        this.interval = interval;
    }

}
