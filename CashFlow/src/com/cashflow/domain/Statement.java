package com.cashflow.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.statement.database.StatementType;

/**
 * Simple class for statement. This will hold data for one statement.
 * @author Janos_Gyula_Meszaros
 *
 */
public final class Statement {
    private final String id;
    private final String amount;
    private final Category category;
    private final String note;
    private final String date;
    private final StatementType type;
    private final RecurringInterval recurringInterval;

    private Statement(final Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.category = builder.category;
        this.date = builder.date;
        this.note = builder.note;
        this.recurringInterval = builder.recurringInterval;
        this.type = builder.type;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public StatementType getType() {
        return type;
    }

    public RecurringInterval getRecurringInterval() {
        return recurringInterval;
    }

    public Category getCategory() {
        return category;
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
     * Builder for statement class. Required parameters are <code>amount</code> and <code>date</code>.
     * Default statement type is {@link StatementType.Income}.
     * Default {@link RecurringInterval} is none.
     * @author Janos_Gyula_Meszaros
     *
     */
    public static class Builder {
        private final String amount;
        private final String date;

        private String id = "";
        private String note = "";
        private StatementType type = StatementType.Income;
        private RecurringInterval recurringInterval = RecurringInterval.none;
        private Category category;

        /**
         * Create builder for {@link Statement} with required values <code>amount</code> and <code>date</code>.
         * @param amount the amount of {@link Statement}
         * @param date the date of {@link Statement}
         */
        public Builder(final String amount, final String date) {
            this.amount = amount;
            this.date = date;
        }

        /**
         * Set the note for the {@link Statement}.
         * @param note note for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setNote(final String note) {
            this.note = note;
            return this;
        }

        /**
         * Set type for the {@link Statement}.
         * @param type type for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setType(final StatementType type) {
            this.type = type;
            return this;
        }

        /**
         * Set interval for the {@link Statement}. 
         * @param interval interval for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setRecurringInterval(final RecurringInterval interval) {
            recurringInterval = interval;
            return this;
        }

        /**
         * Set id for the {@link Statement}.
         * @param id id for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }

        /**
         * Set category for the {@link Statement}.
         * @param category 
         * @return {@link Builder}
         */
        public Builder setCategory(final Category category) {
            this.category = category;
            return this;
        }

        /**
         * Build the {@link Statement}.
         * @return the {@link Statement} with the obligatory fields set
         */
        public Statement build() {
            return new Statement(this);
        }

    }

}
