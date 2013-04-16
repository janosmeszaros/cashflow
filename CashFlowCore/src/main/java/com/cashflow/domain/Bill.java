package com.cashflow.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cashflow.constants.RecurringInterval;

/**
 * Bill class.
 * @author Janos_Gyula_Meszaros
 */
public final class Bill {
    private final String billId;
    private final String amount;
    private final String date;
    private final String payedDate;
    private final String deadlineDate;
    private final String note;
    private final Category category;
    private final boolean payed;
    private final RecurringInterval interval;

    private Bill(final Builder builder) {
        super();
        billId = builder.billId;
        amount = builder.amount;
        date = builder.date;
        payedDate = builder.payedDate;
        deadlineDate = builder.deadlineDate;
        note = builder.note;
        category = builder.category;
        payed = builder.payed;
        interval = builder.interval;
    }

    public String getBillId() {
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
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Returns a builder for {@link Bill}.
     * @param amount
     *            amount of bill.
     * @param date
     *            date of bill.
     * @param deadlineDate
     *            deadline for the bill.
     * @return {@link Builder}.
     */
    public static Builder builder(final String amount, final String date, final String deadlineDate) {
        return new Builder(amount, date, deadlineDate);
    }

    /**
     * Builder class for bill.
     * @author Janos_Gyula_Meszaros
     */
    public static final class Builder {
        private String billId;
        private final String amount;
        private final String date;
        private String payedDate;
        private final String deadlineDate;
        private String note;
        private Category category;
        private boolean payed;
        private RecurringInterval interval;

        /**
         * Constructor.
         * @param amount
         *            amount
         * @param date
         *            date
         * @param deadlineDate
         *            deadline
         */
        private Builder(final String amount, final String date, final String deadlineDate) {
            this.amount = amount;
            this.date = date;
            this.deadlineDate = deadlineDate;
        }

        /**
         * Set ID for bill.
         * @param billId
         *            Bill id.
         * @return {@link Builder}
         */
        public Builder billId(final String billId) {
            this.billId = billId;
            return this;
        }

        /**
         * Set payed date for bill.
         * @param payedDate
         *            date.
         * @return {@link Builder}
         */
        public Builder payedDate(final String payedDate) {
            this.payedDate = payedDate;
            return this;
        }

        /**
         * Set note for bill.
         * @param note
         *            note.
         * @return {@link Builder}
         */
        public Builder note(final String note) {
            this.note = note;
            return this;
        }

        /**
         * Set category for bill.
         * @param category
         *            category.
         * @return {@link Builder}
         */
        public Builder category(final Category category) {
            this.category = category;
            return this;
        }

        /**
         * Set is payed for bill.
         * @param payed
         *            is payed.
         * @return {@link Builder}
         */
        public Builder isPayed(final boolean payed) {
            this.payed = payed;
            return this;
        }

        /**
         * Set interval for bill.
         * @param interval
         *            {@link RecurringInterval}
         * @return {@link Builder}
         */
        public Builder interval(final RecurringInterval interval) {
            this.interval = interval;
            return this;
        }

        /**
         * Builds the bill.
         * @return {@link Bill}
         */
        public Bill build() {
            return new Bill(this);
        }

    }
}
